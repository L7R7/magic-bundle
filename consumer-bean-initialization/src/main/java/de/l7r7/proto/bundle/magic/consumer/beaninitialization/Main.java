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

package de.l7r7.proto.bundle.magic.consumer.beaninitialization;

import de.l7r7.proto.bundle.magic.number.api.RandomNumberGenerator;
import de.l7r7.proto.bundle.magic.string.api.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private final StringConcatenator stringConcatenator;
    private RandomNumberGenerator randomNumberGenerator;
    private RandomStringGenerator randomStringGenerator;

    public Main(StringConcatenator stringConcatenator) {
        this.stringConcatenator = stringConcatenator;
    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
        consumeServicesIfPresent();
    }

    public void unsetRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = null;
    }

    public void setRandomStringGenerator(RandomStringGenerator randomStringGenerator) {
        this.randomStringGenerator = randomStringGenerator;
        consumeServicesIfPresent();
    }

    public void unsetRandomStringGenerator(RandomStringGenerator randomStringGenerator) {
        this.randomStringGenerator = null;
    }

    private void consumeServicesIfPresent() {
        if (randomNumberGenerator != null && randomStringGenerator != null) {
            log.info("------------ output of bean initialized ------------");
            log.info(String.valueOf(randomNumberGenerator.generateNumber()));
            log.info(randomStringGenerator.generateString());
            log.info(stringConcatenator.getConcatenated());
            log.info("------------ end of output of bean initialized ------------");
        }
    }
}
