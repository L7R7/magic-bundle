package de.l7r7.proto.bundle.magic.number.impl;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;

import java.util.Random;

public class RandomNumberImpl implements RandomNumberGenerator {
    private final Random random = new Random();

    @Override
    public int generateNumber() {
        return random.nextInt();
    }
}
