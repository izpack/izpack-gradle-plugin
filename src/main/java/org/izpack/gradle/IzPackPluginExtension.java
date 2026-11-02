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

import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.jspecify.annotations.NonNull;

/**
 * Defines IzPack extension.
 */
public class IzPackPluginExtension {
    private final DirectoryProperty baseDir;
    private final RegularFileProperty installFile;
    private final DirectoryProperty outputDir;
    private final Property<String> outputFileName;
    private final RegularFileProperty outputFile;
    private final Property<InstallerType> installerType;
    private final Property<Compression> compression;
    private final Property<Integer> compressionLevel;
    private final Property<Boolean> inheritAll;
    private final MapProperty<String, Object> appProperties;

    /**
     * Creates a new IzPack plugin extension.
     *
     * @param project the project
     */
    public IzPackPluginExtension(@NonNull Project project) {
        ObjectFactory objectFactory = project.getObjects();
        baseDir = objectFactory.directoryProperty();
        installFile = objectFactory.fileProperty();
        outputDir = objectFactory.directoryProperty();
        outputFileName = objectFactory.property(String.class);
        outputFile = objectFactory.fileProperty();
        installerType = objectFactory.property(InstallerType.class);
        compression = objectFactory.property(Compression.class);
        compressionLevel = objectFactory.property(Integer.class);
        inheritAll = objectFactory.property(Boolean.class);
        appProperties = objectFactory.mapProperty(String.class, Object.class);
        // defaults
        baseDir.convention(project.getLayout().getBuildDirectory().dir("assemble/izpack"));
        installFile.convention(project.getLayout().getProjectDirectory().file("src/main/izpack/install.xml"));
        outputDir.convention(project.getLayout().getBuildDirectory().dir("distributions"));
        outputFileName.convention(project.provider(() -> {
            StringBuilder outputFileResult = new StringBuilder();
            outputFileResult.append(project.getName());
            if (project.getVersion() != Project.DEFAULT_VERSION) {
                outputFileResult.append("-").append(project.getVersion());
            }
            outputFileResult.append("-installer.jar");
            return outputFileResult.toString();
        }));
        outputFile.convention(outputDir.file(outputFileName));
        installerType.convention(project.provider(() -> InstallerType.STANDARD));
        compression.convention(Compression.DEFAULT);
        compressionLevel.convention(9);
        inheritAll.convention(false);
    }

    /**
     * Returns the base directory property used by the IzPack plugin extension.
     *
     * @return the base directory property
     */
    public DirectoryProperty getBaseDir() {
        return baseDir;
    }

    /**
     * Returns the installer type property used by the IzPack plugin extension.
     *
     * @return the installer type property
     */
    public Property<InstallerType> getInstallerType() {
        return installerType;
    }

    /**
     * Sets the installer type property used by the IzPack plugin extension.
     *
     * @param installerType the installer type property
     */
    public void setInstallerType(String installerType) {
        this.installerType.set(InstallerType.forName(installerType));
    }

    /**
     * Returns the install file property used by the IzPack plugin extension.
     *
     * @return the install file property
     */
    public RegularFileProperty getInstallFile() {
        return installFile;
    }

    /**
     * Returns the output directory property used by the IzPack plugin extension.
     *
     * @return the output directory property
     */
    public DirectoryProperty getOutputDir() {
        return outputDir;
    }

    /**
     * Returns the output file name property used by the IzPack plugin extension.
     *
     * @return the output file name property
     */
    public Property<String> getOutputFileName() {
        return outputFileName;
    }

    /**
     * Returns the output file property used by the IzPack plugin extension.
     *
     * @return the output file property
     */
    public RegularFileProperty getOutputFile() {
        return outputFile;
    }

    /**
     * Returns the compression property used by the IzPack plugin extension.
     *
     * @return the compression property
     */
    public Property<Compression> getCompression() {
        return compression;
    }

    /**
     * Returns the compression level property used by the IzPack plugin extension.
     *
     * @return the compression level property
     */
    public Property<Integer> getCompressionLevel() {
        return compressionLevel;
    }

    /**
     * Sets the compression property used by the IzPack plugin extension.
     *
     * @param compression the compression property
     */
    public void setCompression(String compression) {
        this.compression.set(Compression.forName(compression));
    }

    /**
     * Returns the inherit all property used by the IzPack plugin extension.
     *
     * @return the inherit all property
     */
    public Property<Boolean> getInheritAll() {
        return inheritAll;
    }

    /**
     * Returns the application properties map property used by the IzPack plugin extension.
     *
     * @return the application properties map property
     */
    public MapProperty<String, Object> getAppProperties() {
        return appProperties;
    }
}