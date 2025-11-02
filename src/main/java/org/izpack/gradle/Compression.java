/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.izpack.gradle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Available compressions.
 */
public enum Compression {
    DEFAULT("default"),
    DEFLATE("deflate"),
    RAW("raw");

    private static final Map<String, Compression> COMPRESSIONS;

    static {
        COMPRESSIONS = new HashMap<>();

        for (Compression compression : values()) {
            COMPRESSIONS.put(compression.name, compression);
        }
    }

    private final String name;

    Compression(String name) {
        this.name = name;
    }

    public static Compression getCompressionForName(String name) {
        return COMPRESSIONS.get(name);
    }

    public static Set<String> getNames() {
        return COMPRESSIONS.keySet();
    }

    public String getName() {
        return name;
    }
}