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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;

/**
 * <p>A {@link org.gradle.api.Plugin} that provides tasks for packaging, distributing and deploying applications for the
 * Java platform with IzPack.</p>
 */
public abstract class IzPackPlugin implements Plugin<Project> {
    public static final String IZPACK_CONFIGURATION_NAME = "izpack";
    public static final String IZPACK_EXTENSION_NAME = "izpack";

    @Override
    public void apply(Project project) {
        final Configuration izpackConfiguration = project.getConfigurations().maybeCreate(IZPACK_CONFIGURATION_NAME).setTransitive(true)
                .setDescription("The IzPack standalone compiler libraries to be used for this project.");

        IzPackPluginExtension izPackExtension = project.getExtensions().create(IZPACK_EXTENSION_NAME, IzPackPluginExtension.class, project);
        project.getTasks().register("izPackCreateInstaller", CreateInstallerTask.class, project, izPackExtension);
    }
}