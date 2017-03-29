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

public class StringConcatenator {
    private final StringToLower stringToLower;

    public StringConcatenator(StringToLower stringToLower) {
        this.stringToLower = stringToLower;
    }

    public String getConcatenated() {
        return stringToLower.getLowerStrings().reduce("", (s, s2) -> s + " " + s2).trim();
    }
}
