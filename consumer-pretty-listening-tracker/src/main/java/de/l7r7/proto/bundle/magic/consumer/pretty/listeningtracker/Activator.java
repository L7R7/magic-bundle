package de.l7r7.proto.bundle.magic.consumer.pretty.listeningtracker;

import de.l7r7.proto.bundle.magic.api.RandomNumberGenerator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Activator implements BundleActivator, GenericServiceConsumer<RandomNumberGenerator> {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final CustomGenericDefaultServiceObservingProvidility<RandomNumberGenerator> providility = new CustomGenericDefaultServiceObservingProvidility();
    private Optional<RandomNumberGenerator> randomNumberGenerator = Optional.empty();

    @Override
    public void start(BundleContext context) throws Exception {
        providility.start(context, RandomNumberGenerator.class, this);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        providility.stop();
    }

    @Override
    public void serviceAvailable(RandomNumberGenerator service) {
        reassignRandomNumberGenerator(service);
        log.info("---------------------- service prettily available");
        randomNumberGenerator.ifPresent(rng -> log.info("----------------- pretty random: {}", rng.generateNumber()));
    }

    @Override
    public void serviceUnavailable() {
        log.info("---------------------- service prettily UNavailable");
        reassignRandomNumberGenerator(null);
    }

    private void reassignRandomNumberGenerator(RandomNumberGenerator service) {
        synchronized (lock) {
            randomNumberGenerator = service != null ? Optional.of(service) : Optional.empty();
        }
    }
}
