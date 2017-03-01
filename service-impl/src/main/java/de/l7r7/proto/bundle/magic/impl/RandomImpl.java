package de.l7r7.proto.bundle.magic.impl;

import de.l7r7.proto.bundle.magic.api.RandomNumberGenerator;

import java.util.Random;

public class RandomImpl implements RandomNumberGenerator {
    private final Random random = new Random();

    @Override
    public int generateNumber() {
        return random.nextInt();
    }
}
