# NeuroEvolutionVehicles

This Repository contains a Demo of Vehicles learning to drive on any given track being trained with a genetic algorithm.
The Vehicles use a Neural Network which can be mutated randomly. The genetic algorithm then determines the best vehicle
and generates a new population for the next generation. In most cases the vehicles have developed a basic understanding
of the track in less than 10 generations.

## Plugin System

This Demo is written in a way that it can be easily expanded by plugins. Currently only plugins for tracks are
supported. An example plugin can be seen [here](https://github.com/schlunzis/neuroevolutionvehiclespluginsample).  
The plugin-APIs can be found somewhere, when we deploy it again.

## Installation

In order to run the application just clone the repository and execute the `mvn clean package` command. After this, you
will find the executable jar file in the target folder of the NeuroEvolutionDemo directory.  
By the first start, the application will create a plugins folder next to the jar itself. In this folder you can place
any number of track-plugins. By the next startup, the application will scan through this folder and loads any compatible
plugin.

## Logging

The applications default logging level ist info. To get the debug logging level, run the application with
`-Dlogging.level=DEBUG` as vm argument.

> **NOTE**
>
> Make sure you have `LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu` exported as environment variable!
