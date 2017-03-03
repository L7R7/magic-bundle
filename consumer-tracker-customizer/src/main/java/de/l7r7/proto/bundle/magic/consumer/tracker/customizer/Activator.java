package de.l7r7.proto.bundle.magic.consumer.tracker.customizer;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private ServiceTracker<RandomNumberGenerator, RandomNumberGenerator> randomNumberServiceTracker;
    private ServiceReference<RandomNumberGenerator> serviceReference;

    @Override
    public void start(BundleContext context) throws Exception {
        randomNumberServiceTracker = new ServiceTracker<>(context, RandomNumberGenerator.class.getName(), new ServiceTrackerCustomizer<RandomNumberGenerator, RandomNumberGenerator>() {
            @Override
            public RandomNumberGenerator addingService(ServiceReference<RandomNumberGenerator> reference) {
                log.info(">>>>>>>>>>>> customizer#addingService");
                setServiceReference(reference);
                RandomNumberGenerator service = context.getService(reference);
                consumeService(service);
                return service;
            }

            @Override
            public void modifiedService(ServiceReference<RandomNumberGenerator> reference, RandomNumberGenerator service) {
                log.info(">>>>>>>>>>>> customizer#modifiedService");
                unsetServiceReference(context);
            }

            @Override
            public void removedService(ServiceReference<RandomNumberGenerator> reference, RandomNumberGenerator service) {
                log.info(">>>>>>>>>>>> customizer#removedService");
                unsetServiceReference(context);
            }
        });
        randomNumberServiceTracker.open();

        final ServiceReference<RandomNumberGenerator> reference = randomNumberServiceTracker.getServiceReference();
        if (reference != null) {
            consumeService(context.getService(reference));
        }

    }

    private void setServiceReference(ServiceReference<RandomNumberGenerator> reference) {
        serviceReference = reference;
    }

    private void unsetServiceReference(BundleContext context) {
        if (serviceReference != null) {
            if (context != null) {
                context.ungetService(serviceReference);
            }
            serviceReference = null;
        }
    }

    private void consumeService(RandomNumberGenerator service) {
        if (service != null) {
            log.info(">>>>>>>>>>>> customized tracked random: {} with service {}", service.generateNumber(), service);
        } else {
            log.info(">>>>>>>>>>>> no customized tracked random because serviceReference is null");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        randomNumberServiceTracker.close();
        unsetServiceReference(context);
    }
}
