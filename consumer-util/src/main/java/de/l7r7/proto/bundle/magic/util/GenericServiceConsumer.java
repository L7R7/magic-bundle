package de.l7r7.proto.bundle.magic.util;

public interface GenericServiceConsumer<T> {
    void serviceAvailable(T service);

    void serviceUnavailable();
}
