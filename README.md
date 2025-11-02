# Gradle IzPack plugin [![Build [Linux]](https://github.com/bmuschko/gradle-izpack-plugin/actions/workflows/linux-build.yaml/badge.svg)](https://github.com/bmuschko/gradle-izpack-plugin/actions/workflows/linux-build.yaml)

![IzPack Logo](https://izpack.org/img-izpack/logo-medium.png)

The plugin provides a one-stop solution for packaging, distributing and deploying applications for the Java platform
using [IzPack](https://izpack.org/).

## Usage

To use the IzPack plugin, include in your build script:

    plugins {
        id 'org.izpack' version '3.2.2'
    }

The plugin JAR needs to be defined in the classpath of your build script. It is directly available on the
[Gradle plugin portal](https://plugins.gradle.org/plugin/org.izpack). The following code snippet shows a usage example:

    buildscript {
        repositories {
            gradlePluginPortal()
        }

        dependencies {
            classpath 'org:gradle-izpack-plugin:3.2.2'
        }
    }

    apply plugin: 'org.izpack'

To define the IzPack standalone compiler dependency please use the `izpack` configuration name in your `dependencies` closure.

For IzPack v5

    dependencies {
        izpack 'org.codehaus.izpack:izpack-ant:5.2.4'
    }

or for IzPack v4

    dependencies {
        izpack 'org.codehaus.izpack:izpack-standalone-compiler:4.3.5'
    }

## Tasks

The IzPack plugin defines the following tasks:

* `izPackCreateInstaller`: Creates an IzPack-based installer.

## Convention properties

The IzPack plugin defines the following convention properties in the `izpack` closure:

* `baseDir`: The base directory of compilation process (defaults to `build/assemble/izpack`).
* `installerType`: The installer type (defaults to `standard`). You can select between `standard` and `web`.
* `installFile`: The location of the [IzPack installation file](https://izpack.atlassian.net/wiki/spaces/IZPACK/pages/491709/Writing+Installation+Descriptions) (defaults to `src/main/izpack/install.xml`).
* `outputFile`: The installer output directory and filename (defaults to `build/distributions/<projectname>-<version>-installer.jar`).
* `compression`: The compression of the installation (defaults to `default`). You can select between `default`, `deflate` and `raw`.
* `compressionLevel`: The compression level of the installation (defaults to -1 for no compression). Valid values are -1 to 9.
* `appProperties`: The `Map` of application properties to be used for the compilation process (defaults to empty `Map`).

### Example

    izpack {
        baseDir = file("$buildDir/assemble/izpack")
        installFile = file('installer/izpack/installer.xml')
        outputFile = file("$buildDir/distributions/griffon-${version}-installer.jar")
        compression = 'deflate'
        compressionLevel = 9
        appProperties = ['app.group': 'Griffon', 'app.name': 'griffon', 'app.title': 'Griffon',
                         'app.version': version, 'app.subpath': "Griffon-$version"]
    }