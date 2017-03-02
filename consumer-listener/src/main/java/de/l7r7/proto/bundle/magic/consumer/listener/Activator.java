package de.l7r7.proto.bundle.magic.consumer.listener;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);

    private ServiceReference<RandomNumberGenerator> randomNumberGeneratorServiceReference;
    private ServiceListener listener;

    @Override
    public void start(BundleContext context) throws Exception {
        listener = event -> {
            randomNumberGeneratorServiceReference = (ServiceReference<RandomNumberGenerator>) event.getServiceReference();
            switch (event.getType()) {
                case ServiceEvent.REGISTERED:
                    log.info("Listened Service Event registered");
                    final RandomNumberGenerator service = context.getService(randomNumberGeneratorServiceReference);
                    log.info("################ listened random: {} with service {}", service.generateNumber(), service);
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
        ungetAndRemoveServiceReference(context);
    }

    private void ungetAndRemoveServiceReference(BundleContext context) {
        if (randomNumberGeneratorServiceReference != null) {
            context.ungetService(randomNumberGeneratorServiceReference);
            randomNumberGeneratorServiceReference = null;
        }
        log.info("Listened Service Event unregistered");
    }
}
