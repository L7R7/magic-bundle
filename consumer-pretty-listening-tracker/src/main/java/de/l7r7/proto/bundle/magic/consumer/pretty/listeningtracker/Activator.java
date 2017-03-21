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

package de.l7r7.proto.bundle.magic.consumer.pretty.listeningtracker;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;
import de.l7r7.proto.bundle.magic.util.CustomGenericDefaultServiceObservingProvidility;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Activator implements BundleActivator {
    private static final Logger log = LoggerFactory.getLogger(Activator.class);
    private final CustomGenericDefaultServiceObservingProvidility<RandomNumberGenerator> providility = new CustomGenericDefaultServiceObservingProvidility<>();
    private Optional<RandomNumberGenerator> randomNumberGenerator = Optional.empty();

    @Override
    public void start(BundleContext context) throws Exception {
        providility.start(context, RandomNumberGenerator.class, service -> {
            randomNumberGenerator = service;
            randomNumberGenerator.ifPresent(rng -> log.info("----------------- pretty random: {}", rng.generateNumber()));
        });
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        providility.stop();
    }
}
