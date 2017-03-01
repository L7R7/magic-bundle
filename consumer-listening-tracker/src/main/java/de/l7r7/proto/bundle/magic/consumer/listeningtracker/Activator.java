package de.l7r7.proto.bundle.magic.consumer.listeningtracker;

import de.l7r7.proto.bundle.magic.api.RandomNumberGenerator;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private ServiceTracker<RandomNumberGenerator, RandomNumberGenerator> randomNumberServiceTracker;
    private ServiceReference<RandomNumberGenerator> serviceReference;
    private ServiceListener listener;

    @Override
    public void start(BundleContext context) throws Exception {
        randomNumberServiceTracker = new ServiceTracker<>(context, RandomNumberGenerator.class.getName(), null);
        randomNumberServiceTracker.open();
        serviceReference = randomNumberServiceTracker.getServiceReference();
        if (serviceReference != null) {
            final RandomNumberGenerator service = context.getService(serviceReference);
            if (service != null) {
                log.info("~~~~~~~~~~~~~ listened tracked random: {} with service {}", service.generateNumber(), service);
            } else {
                log.info("~~~~~~~~~~~~~ no listened tracked random because service is null");
            }
        } else {
            log.info("~~~~~~~~~~~~~ no listened tracked random because serviceReference is null");
        }

        listener = event -> {
            switch (event.getType()) {
                case ServiceEvent.REGISTERED:
                    log.info("Listened tracked Service Event registered");
                    if (serviceReference == null) {
                        serviceReference = (ServiceReference<RandomNumberGenerator>) event.getServiceReference();
                    }
                    final RandomNumberGenerator service = context.getService(serviceReference);
                    log.info("~~~~~~~~~~~~~ listened tracked random: {} with service {}", service.generateNumber(), service);
                    break;
                case ServiceEvent.UNREGISTERING:
                    ungetAndRemoveServiceReference(context);
                    break;
            }
        };
        context.addServiceListener(listener, "(objectclass=" + RandomNumberGenerator.class.getName() + ")");

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context.removeServiceListener(listener);
        randomNumberServiceTracker.close();

        ungetAndRemoveServiceReference(context);
    }

    private void ungetAndRemoveServiceReference(BundleContext context) {
        if (serviceReference != null) {
            context.ungetService(serviceReference);
            serviceReference = null;
        }
        log.info(" ~~~~~~~~~~~~~ Listened tracked Service Event unregistered");
    }
}
