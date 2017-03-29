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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class StringProvider {
    private List<String> strings;

    public StringProvider() {
        strings = new ArrayList<>();
        strings.add(UUID.randomUUID().toString());
        strings.add(UUID.randomUUID().toString());
        strings.add(UUID.randomUUID().toString());
    }

    public Stream<String> getStrings() {
        return strings.stream();
    }
}