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

package de.l7r7.proto.bundle.magic.consumer.tracker.customizer.filter;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;
import de.l7r7.proto.bundle.magic.string.api.RandomStringGenerator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private ServiceTracker<Object, Object> serviceTracker;
    private ServiceReference<Object> randomNumberGeneratorServiceReference;
    private ServiceReference<Object> randomStringGeneratorServiceReference;
    private boolean servicesConsumed = false;

    @Override
    public void start(BundleContext context) throws Exception {
        String filterString = String.format("(|(objectClass=%s)(objectClass=%s))", RandomNumberGenerator.class.getName(), RandomStringGenerator.class.getName());
        serviceTracker = new ServiceTracker<>(context, context.createFilter(filterString), new ServiceTrackerCustomizer<Object, Object>() {
            @Override
            public Object addingService(ServiceReference<Object> reference) {
                setServiceReference(reference);
                consumeServicesIfPresent(context, randomNumberGeneratorServiceReference, randomStringGeneratorServiceReference);
                return context.getService(reference);
            }

            @Override
            public void modifiedService(ServiceReference<Object> reference, Object service) {

            }

            @Override
            public void removedService(ServiceReference<Object> reference, Object service) {
                unsetServiceReference(reference);
            }
        });
        serviceTracker.open();

        ServiceReference<Object>[] serviceReferences = serviceTracker.getServiceReferences();
        if (serviceReferences != null) {
            Arrays.stream(serviceReferences).forEach(this::setServiceReference);
        } else {
            log.info("serviceReferences is null");
        }

        consumeServicesIfPresent(context, randomNumberGeneratorServiceReference, randomStringGeneratorServiceReference);
    }

    private void setServiceReference(ServiceReference<Object> reference) {
        String objectClass = ((String[]) reference.getProperty(Constants.OBJECTCLASS))[0]; // TODO: 16.03.2017 when will this array have more than one element?

        if (RandomStringGenerator.class.getName().equals(objectClass))
            randomStringGeneratorServiceReference = reference;
        else if (RandomNumberGenerator.class.getName().equals(objectClass))
            randomNumberGeneratorServiceReference = reference;
    }

    private void unsetServiceReference(ServiceReference<Object> reference) {
        String objectClass = ((String[]) reference.getProperty(Constants.OBJECTCLASS))[0]; // TODO: 16.03.2017 when will this array have more than one element?

        if (RandomStringGenerator.class.getName().equals(objectClass))
            randomStringGeneratorServiceReference = null;
        else if (RandomNumberGenerator.class.getName().equals(objectClass))
            randomNumberGeneratorServiceReference = null;
    }

    private void consumeServicesIfPresent(BundleContext context, ServiceReference<Object> numberServiceRef, ServiceReference<Object> stringServiceRef) {
        if (numberServiceRef != null && stringServiceRef != null) {
            final RandomNumberGenerator randomNumberGenerator = (RandomNumberGenerator) context.getService(numberServiceRef);
            final RandomStringGenerator randomStringGenerator = (RandomStringGenerator) context.getService(stringServiceRef);
            if (randomNumberGenerator != null && randomStringGenerator != null) {
                if (!servicesConsumed) {
                    servicesConsumed = true;
                    log.info("both services are available! {} -- {}", randomNumberGenerator.generateNumber(), randomStringGenerator.generateString());
                }
            }
        } else {
            log.info("the services aren't fully available");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        serviceTracker.close();
        if (randomNumberGeneratorServiceReference != null) {
            context.ungetService(randomNumberGeneratorServiceReference);
        }
        if (randomStringGeneratorServiceReference != null) {
            context.ungetService(randomStringGeneratorServiceReference);
        }
    }
}
