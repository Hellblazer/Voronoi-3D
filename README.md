# Voronoi 3D

A library for incrementally constructing 3-dimensional Delaunay tetrahedralizations.  The Voronoi network - the dual of the Delaunay - can be extracted from the system.  A graphical user interface for inspecting the voronoi/delaunay network is also provided.  

## Build Status
![Build Status](https://github.com/hellblazer/Voronoi-3D/actions/workflows/maven.yml/badge.svg)

___
This library is licensed under the AGPL v3.0, requires Java 22+ and is built using Maven.
This repository includes the Maven Wrapper, so you do not need to install maven to build.

To build cd into the root directory and do:

    ./mvnw clean install

## Current Status
Raised from the dead.

The GUI is now a very crude, but serviceable Java/FX GUI.
Probably will work on cleaning that up and improving that, but...

The current implementation leaves flat tetrahedra when processing the cubic example test cases.  :: big sad :: My understanding is that these can be removed with 4 <-> 4 flips, so enjoy!

## Maven Artifacts
Currently, Voronoi-3D is snapshot development and does not publish to maven central.
     
