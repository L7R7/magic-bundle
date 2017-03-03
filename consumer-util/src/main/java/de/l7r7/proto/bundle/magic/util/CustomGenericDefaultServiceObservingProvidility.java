package de.l7r7.proto.bundle.magic.util;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Optional;
import java.util.function.Consumer;

public class CustomGenericDefaultServiceObservingProvidility<T> {
    private BundleContext context;
    private Consumer<Optional<T>> consumer;
    private ServiceListener listener;
    private ServiceTracker<T, T> serviceTracker;
    private ServiceReference<T> serviceReference;

    public void start(BundleContext bundleContext, Class<T> clazz, Consumer<Optional<T>> consumer) throws InvalidSyntaxException {
        this.context = bundleContext;
        this.consumer = consumer;

        serviceTracker = new ServiceTracker<>(bundleContext, clazz.getName(), null);
        serviceTracker.open();
        serviceReference = serviceTracker.getServiceReference();
        if (serviceReference != null) {
            final T service = bundleContext.getService(serviceReference);
            consumer.accept(Optional.ofNullable(service));
        } else {
            consumer.accept(Optional.empty());
        }

        listener = event -> {
            switch (event.getType()) {
                case ServiceEvent.REGISTERED:
                    if (serviceReference == null) {
                        serviceReference = (ServiceReference<T>) event.getServiceReference();
                    }
                    final T service = bundleContext.getService(serviceReference);
                    consumer.accept(Optional.ofNullable(service));
                    break;
                case ServiceEvent.UNREGISTERING:
                    cleanup(bundleContext);
                    break;
            }
        };
        bundleContext.addServiceListener(listener, "(objectclass=" + clazz.getName() + ")");
    }

    private void cleanup(BundleContext context) {
        consumer.accept(Optional.empty());
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
