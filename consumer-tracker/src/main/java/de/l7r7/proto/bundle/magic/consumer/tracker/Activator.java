package de.l7r7.proto.bundle.magic.consumer.tracker;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private ServiceTracker<RandomNumberGenerator, RandomNumberGenerator> randomNumberServiceTracker;
    private ServiceReference<RandomNumberGenerator> serviceReference;

    @Override
    public void start(BundleContext context) throws Exception {
        randomNumberServiceTracker = new ServiceTracker<>(context, RandomNumberGenerator.class.getName(), null);
        randomNumberServiceTracker.open();
        serviceReference = randomNumberServiceTracker.getServiceReference();
        if (serviceReference != null) {
            final RandomNumberGenerator service = context.getService(serviceReference);
            if (service != null) {
                log.info("############ tracked random: {} with service {}", service.generateNumber(), service);
            } else {
                log.info("############# no tracked random because service is null");
            }
        } else {
            log.info("############# no tracked random because serviceReference is null");
        }

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        randomNumberServiceTracker.close();
        if (serviceReference != null) {
            context.ungetService(serviceReference);
            serviceReference = null;
        }
    }
}
