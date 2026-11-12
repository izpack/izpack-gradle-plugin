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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Available compressions.
 */
public enum Compression {
    /** Use default compression */
    DEFAULT("default"),
    /** Use deflate compression */
    DEFLATE("deflate"),
    /** Use raw data without compression */
    RAW("raw");

    /** The name passed to the IzPack compiler for this compression. */
    private final String name;

    /**
     * Creates a compression value with the specified IzPack name.
     *
     * @param name the name passed to the IzPack compiler
     */
    Compression(String name) {
        this.name = name;
    }

    /**
     * Returns the compression matching the specified name.
     * If no matching compression is found, returns {@link #DEFAULT}.
     *
     * @param name the compression name to match
     * @return the matching compression, or {@link #DEFAULT} if no match is found
     */
    public static Compression forName(String name) {
        return Stream.of(values()).filter(compression -> compression.name.equals(name)).findFirst().orElse(DEFAULT);
    }

    /**
     * Returns the names of all compressions.
     *
     * @return the names of all compressions
     */
    public static Set<String> getNames() {
        return Stream.of(values()).map(Compression::getName).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the name of this compression.
     *
     * @return the name of this compression
     */
    public String getName() {
        return name;
    }
}