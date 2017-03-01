package de.l7r7.proto.bundle.magic.consumer.pretty.listeningtracker;

public interface GenericServiceConsumer<T> {
    void serviceAvailable(T service);

    void serviceUnavailable();
}
