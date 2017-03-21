/*
 *  Copyright 2017 Leonhard Riedißer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.l7r7.proto.bundle.magic.util;

import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Optional;
import java.util.function.Consumer;

public class CustomGenericDefaultServiceObservingProvidility<T> {
    private BundleContext context;
    private Consumer<Optional<T>> consumer;
    private ServiceListener listener;
    private ServiceTracker<T, T> serviceTracker;
    private ServiceReference<T> serviceReference;

    public void start(BundleContext bundleContext, Class<T> clazz, Consumer<Optional<T>> consumer) throws InvalidSyntaxException {
        this.context = bundleContext;
        this.consumer = consumer;

        serviceTracker = new ServiceTracker<>(bundleContext, clazz.getName(), null);
        serviceTracker.open();
        serviceReference = serviceTracker.getServiceReference();
        if (serviceReference != null) {
            final T service = bundleContext.getService(serviceReference);
            consumer.accept(Optional.ofNullable(service));
        } else {
            consumer.accept(Optional.empty());
        }

        listener = event -> {
            switch (event.getType()) {
                case ServiceEvent.REGISTERED:
                    if (serviceReference == null) {
                        serviceReference = (ServiceReference<T>) event.getServiceReference();
                    }
                    final T service = bundleContext.getService(serviceReference);
                    consumer.accept(Optional.ofNullable(service));
                    break;
                case ServiceEvent.UNREGISTERING:
                    cleanup(bundleContext);
                    break;
            }
        };
        bundleContext.addServiceListener(listener, "(objectclass=" + clazz.getName() + ")");
    }

    private void cleanup(BundleContext context) {
        consumer.accept(Optional.empty());
        if (serviceReference != null) {
            context.ungetService(serviceReference);
            serviceReference = null;
        }
    }

    public void stop() {
        context.removeServiceListener(listener);
        cleanup(context);
        serviceTracker.close();
    }
}
