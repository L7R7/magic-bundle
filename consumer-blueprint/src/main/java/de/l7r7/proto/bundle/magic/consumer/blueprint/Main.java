package de.l7r7.proto.bundle.magic.consumer.blueprint;

import de.l7r7.proto.bundle.magic.api.RandomNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private RandomNumberGenerator randomNumberGenerator;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        log.info("+++++++++++++ setRandomNumberGenerator: {}", randomNumberGenerator);
        this.randomNumberGenerator = randomNumberGenerator;
        log.info("+++++++++++++ blueprinted random: {}", this.randomNumberGenerator.generateNumber());
    }

    public void unsetRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        log.info("+++++++++++++ unsetRandomNumberGenerator: {}", randomNumberGenerator);
        this.randomNumberGenerator = null;
    }
}
