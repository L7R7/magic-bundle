package de.l7r7.proto.bundle.magic.util;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

public class CustomGenericDefaultServiceObservingProvidility<T> {
    private BundleContext context;
    private Class<T> clazz;
    private GenericServiceConsumer<T> consumer;
    private ServiceListener listener;
    private ServiceTracker<T, T> serviceTracker;
    private ServiceReference<T> serviceReference;

    public void start(BundleContext bundleContext, Class<T> clazz, GenericServiceConsumer<T> consumer) throws InvalidSyntaxException {
        this.context = bundleContext;
        this.clazz = clazz;
        this.consumer = consumer;

        serviceTracker = new ServiceTracker<>(bundleContext, clazz.getName(), null);
        serviceTracker.open();
        serviceReference = serviceTracker.getServiceReference();
        if (serviceReference != null) {
            final T service = bundleContext.getService(serviceReference);
            if (service != null) {
                consumer.serviceAvailable(service);
            } else {
                consumer.serviceUnavailable();
            }
        } else {
            consumer.serviceUnavailable();
        }

        listener = event -> {
            switch (event.getType()) {
                case ServiceEvent.REGISTERED:
                    if (serviceReference == null) {
                        serviceReference = (ServiceReference<T>) event.getServiceReference();
                    }
                    final T service = bundleContext.getService(serviceReference);
                    if (service != null) {
                        consumer.serviceAvailable(service);
                    } else {
                        consumer.serviceUnavailable();
                    }
                    break;
                case ServiceEvent.UNREGISTERING:
                    cleanup(bundleContext);
                    break;
            }
        };
        bundleContext.addServiceListener(listener, "(objectclass=" + clazz.getName() + ")");
    }

    private void cleanup(BundleContext context) {
        consumer.serviceUnavailable();
        if (serviceReference != null) {
            context.ungetService(serviceReference);
            serviceReference = null;
        }
    }

    public void stop() {
        context.removeServiceListener(listener);
        cleanup(context);
        serviceTracker.close();
    }
}
