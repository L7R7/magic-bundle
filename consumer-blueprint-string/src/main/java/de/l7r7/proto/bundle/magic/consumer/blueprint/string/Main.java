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
