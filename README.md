# Voronoi 3D

A library for incrementally constructing 3 dimensional Delaunay tetrahedralizations.  The Voronoi network - the dual of the Delaunay - can be extracted from the system.  A graphical user interface for inspecting the voronoi/delaunay network is also provided.  

## Build Status
![Build Status](https://github.com/hellblazer/Voronoi-3D/actions/workflows/maven.yml/badge.svg)

___
This library is licensed under the AGPL v3.0, requires Java 20+ and is built with Maven 3.83+.  To build, cd into the root directory and do:

    mvn clean install

## Current Status
Raised from the dead.  The GUI is now a crude, but servicable Java/FX GUI.  Probably will work on cleaning that up and improving that, but...

The current implementation leaves flat tetrahedra when processing the cubic example test cases.  :: big sad :: My understanding is that these can be removed with 4 <-> 4 flips, so enjoy!

## Maven Artifacts
Currently, Voronoi-3D is snapshot development and does not publish to maven central.  Rather, periodic snapshots (and releases when they happen)
will be uploaded to the [repo-hell](https://raw.githubusercontent.com/Hellblazer/repo-hell/main/mvn-artifact) repository.  If you would like to 
use Voronoi-3D maven artifacts, you'll need to add the following repository declarations to your pom.xml  The maven coordinates for individual
artifacts are found below.
    
    <repositories>
        <repository>
            <id>hell-repo</id>
            <url>https://raw.githubusercontent.com/Hellblazer/repo-hell/main/mvn-artifact</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <pluginRepositories>
        <pluginRepository>
            <id>plugin-hell-repo</id>
            <url>https://raw.githubusercontent.com/Hellblazer/repo-hell/main/mvn-artifact</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>
 
### Voronoi 3D library

     <dependency>
         <groupId>com.hellblazer</groupId>
         <artifactId>voronoi-3d</artifactId>
         <version>0.1.0-SNAPSHOT</version>
     </dependency>
 
### Voronoi 3D GUI library

     <dependency>
         <groupId>com.hellblazer</groupId>
         <artifactId>voronoi-3d-gui</artifactId>
         <version>0.1.0-SNAPSHOT</version>
     </dependency>
     
