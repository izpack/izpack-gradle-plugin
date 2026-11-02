package com.bmuschko.gradle.izpack

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

abstract class IzPackPluginSpecification extends Specification {
    @TempDir
    Path tempDir
    File projectDir
    File buildFile
    File settingsFile

    def setup() {
        projectDir = tempDir.toFile()
        buildFile = tempDir.resolve('build.gradle').toFile()
        settingsFile = tempDir.resolve('settings.gradle').toFile()
        settingsFile << settingsFile()
    }

    def "can create installer with default settings"() {
        given:
        buildFile << buildFileBase() + buildFileIzPackDependency()
        def installerDir = Files.createDirectories(tempDir.resolve('src/main/izpack')).toFile()
        new File(installerDir, 'install.xml') << installationFile()
        Files.createDirectories(tempDir.resolve('build/assemble/izpack'))

        when:
        build('izPackCreateInstaller', '-s', '-i')

        then:
        new File(projectDir, 'build/distributions/myizpack-1.0-installer.jar').isFile()
    }

    def "can create installer with custom settings"() {
        given:
        buildFile << buildFileBase() + buildFileIzPackDependency() + buildFileCustomSettings()
        def installerDir = Files.createDirectories(tempDir.resolve('installer/izpack')).toFile()
        new File(installerDir, 'installer.xml') << installationFile()
        Files.createDirectories(tempDir.resolve('build/my/izpack'))

        when:
        build('izPackCreateInstaller', '-s', '-i')

        then:
        new File(projectDir, 'build/out/griffon-1.0-installer.jar').isFile()
    }

    private BuildResult build(String... arguments) {
        createAndConfigureGradleRunner(arguments).build()
    }

    private GradleRunner createAndConfigureGradleRunner(String... arguments) {
        GradleRunner.create().withProjectDir(projectDir).withArguments(arguments).withPluginClasspath()
    }

    private static String buildFileBase() {
        """
        plugins {
            id 'java'
            id 'org.izpack.gradle'
        }
        
        version = '1.0'
        
        repositories {
            mavenCentral()
            maven { url = 'https://central.sonatype.com/repository/maven-snapshots/' }
        }
        """
    }

    private static String buildFileCustomSettings() {
        """
        izpack {
            baseDir =  layout.buildDirectory.dir('my/izpack')
            installFile = file('installer/izpack/installer.xml')
            outputFile = layout.buildDirectory.file("out/griffon-\${version}-installer.jar")
            compression = 'deflate'
            compressionLevel = 9
            appProperties = ['app.group': 'Griffon', 'app.name': 'griffon', 'app.title': 'Griffon',
                             'app.version': version, 'app.subpath': "Griffon-\$version"]
        }
        """
    }


    private static String settingsFile() {
        """
        rootProject.name = 'myizpack'
        """
    }

    protected abstract String buildFileIzPackDependency();

    protected abstract String installationFile();
}
