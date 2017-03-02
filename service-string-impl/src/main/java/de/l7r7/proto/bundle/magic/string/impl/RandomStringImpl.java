package de.l7r7.proto.bundle.magic.string.impl;

import de.l7r7.proto.bundle.magic.string.api.RandomStringGenerator;

import java.util.Random;

public class RandomStringImpl implements RandomStringGenerator {
    private final Random random = new Random();

    @Override
    public String generateString() {
        return "Gaussian random string: " + random.nextGaussian();
    }
}
