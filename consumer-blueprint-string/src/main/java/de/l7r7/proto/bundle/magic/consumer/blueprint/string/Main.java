package de.l7r7.proto.bundle.magic.consumer.blueprint.string;

import de.l7r7.proto.bundle.magic.string.api.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private RandomStringGenerator randomStringGenerator;

    public void setRandomStringGenerator(RandomStringGenerator randomStringGenerator) {
        log.info("+++++++++++++ setRandomStringGenerator: {}", randomStringGenerator);
        this.randomStringGenerator = randomStringGenerator;
        log.info("+++++++++++++ blueprinted random: {}", this.randomStringGenerator.generateString());
    }

    public void unsetRandomStringGenerator(RandomStringGenerator randomNumberGenerator) {
        log.info("+++++++++++++ unsetRandomStringGenerator: {}", randomNumberGenerator);
        this.randomStringGenerator = null;
    }
}
