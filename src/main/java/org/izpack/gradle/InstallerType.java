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
 * Available installer types.
 */
public enum InstallerType {
    /** Use default installer type */
    STANDARD("standard"),
    /** Use web installer type */
    WEB("web");

    /** The name passed to the IzPack compiler for this installer type. */
    private final String name;

    /**
     * Creates an installer type with the specified IzPack name.
     *
     * @param name the name passed to the IzPack compiler
     */
    InstallerType(String name) {
        this.name = name;
    }

    /**
     * Returns the installer type matching the specified name.
     * If no matching installer type is found, returns {@link #STANDARD}.
     *
     * @param name the installer type name to match
     * @return the matching installer type, or {@link #STANDARD} if no match is found
     */
    public static InstallerType forName(String name) {
        return Stream.of(values()).filter(compression -> compression.name.equals(name)).findFirst().orElse(STANDARD);
    }

    /**
     * Returns the names of all installer types.
     *
     * @return the names of all installer types
     */
    public static Set<String> getNames() {
        return Stream.of(values()).map(InstallerType::getName).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the name of this installer type.
     *
     * @return the name of this installer type
     */
    public String getName() {
        return name;
    }
}