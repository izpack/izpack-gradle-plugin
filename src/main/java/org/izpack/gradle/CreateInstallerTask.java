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

import org.gradle.api.AntBuilder;
import org.gradle.api.DefaultTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Handler;

import static org.izpack.gradle.IzPackPlugin.IZPACK_CONFIGURATION_NAME;

/**
 * IzPack compilation task.
 */
@CacheableTask
public class CreateInstallerTask extends DefaultTask {
    private final IzPackPluginExtension extension;
    private final FileCollection classpath;

    @Inject
    public CreateInstallerTask(Project project, IzPackPluginExtension extension) {
        this.extension = extension;
        this.classpath = project.getConfigurations().getByName(IZPACK_CONFIGURATION_NAME).getAsFileTree();
    }

    @TaskAction
    public void start() {
        validateConfiguration();
        compile();
    }

    public void validateConfiguration() {
        final InstallerType installerType = getInstallerType();
        if (installerType == null) {
            throw new InvalidUserDataException("Installer type unavailable. Please pick a valid one: " + InstallerType.getNames());
        } else {
            getLogger().info("Installer type = {}", installerType);
        }
        final Compression compression = getCompression();
        if (compression == null) {
            throw new InvalidUserDataException("Compression unavailable. Please pick a valid one: " + Compression.getNames());
        } else {
            getLogger().info("Compression = {}", compression);
        }
        final Integer compressionLevel = getCompressionLevel();
        if (compressionLevel != null && (compressionLevel < -1 || compressionLevel > 9)) {
            throw new InvalidUserDataException("Unsupported compression level: " + compressionLevel +
                    ". Please pick a value between -1 and 9!");
        } else {
            getLogger().info("Compression level = {}", compressionLevel);
        }
    }

    public void compile() {
        final ClassLoader storedClassLoader = Thread.currentThread().getContextClassLoader();
        try (URLClassLoader urlClassLoader = new URLClassLoader(getClasspath().getFiles().stream().map(this::toURL).toArray(URL[]::new))) {
            final File installFile = getInstallFile();
            getLogger().info("Starting to create IzPack installer from base directory '" +
                    getBaseDir().getCanonicalPath() + "' and install file '" + installFile.getCanonicalPath() + "'.");

            final Class<?> runableClass = urlClassLoader.loadClass("com.izforge.izpack.ant.IzpackAntRunnable");
            final Constructor<?> constructor = runableClass.getConstructor(String.class, String.class,
                    String.class, String.class, String.class, String.class, Boolean.TYPE, Integer.TYPE, Properties.class,
                    Boolean.class, Map.class, String.class, Handler.class);
            final Handler logHandler = new LogHandler(getLogger());
            final Properties installerProps = new Properties();
            getAppProperties().getOrElse(Map.of()).forEach((key, value) -> installerProps.put(key, value.toString()));
            final Runnable instance = (Runnable) constructor.newInstance(getCompression().getName(), getInstallerType().getName(),
                    installFile.toString(), null, getBaseDir().toString(), getOutputFile().toString(), Boolean.TRUE,
                    getCompressionLevel(), installerProps, getInheritAll(), getProject().getProperties(),
                    getBaseDir().toString(), logHandler);
            Thread.currentThread().setContextClassLoader(urlClassLoader);
            instance.run();
            getLogger().info("Finished creating IzPack installer.");
        } catch (IOException | ReflectiveOperationException e) {
            throw new TaskExecutionException(this, e);
        } finally {
            Thread.currentThread().setContextClassLoader(storedClassLoader);
        }
    }

    private URL toURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new TaskExecutionException(this, e);
        }
    }

    @InputFiles
    @Classpath
    public FileCollection getClasspath() {
        return classpath;
    }

    @OutputFile
    public File getOutputFile() {
        return extension.getOutputFile().getAsFile().get();
    }

    @InputDirectory
    @Optional
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getBaseDir() {
        return extension.getBaseDir().get().getAsFile();
    }

    @Input
    public InstallerType getInstallerType() {
        return extension.getInstallerType().get();
    }

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    public File getInstallFile() {
        return extension.getInstallFile().getAsFile().get();
    }

    @Input
    public Compression getCompression() {
        return extension.getCompression().get();
    }

    @Input
    public Integer getCompressionLevel() {
        return extension.getCompressionLevel().get();
    }

    @Input
    public Boolean getInheritAll() {
        return extension.getInheritAll().get();
    }

    @Input
    public MapProperty<String, Object> getAppProperties() {
        return extension.getAppProperties();
    }
}