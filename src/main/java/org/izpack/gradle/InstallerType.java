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
 * Available installer types.
 */
public enum InstallerType {
    STANDARD("standard"), WEB("web");

    public static final Map<String, InstallerType> INSTALLER_TYPES = new HashMap<>();

    static {
        for (InstallerType installerType : values()) {
            INSTALLER_TYPES.put(installerType.name, installerType);
        }
    }

    private final String name;

    InstallerType(String name) {
        this.name = name;
    }

    public static InstallerType getInstallerTypeForName(String name) {
        return INSTALLER_TYPES.get(name);
    }

    public static Set<String> getNames() {
        return INSTALLER_TYPES.keySet();
    }

    public String getName() {
        return name;
    }
}