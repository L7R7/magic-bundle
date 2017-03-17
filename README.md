# magic-bundle
This project contains a couple of samples for usages of different OSGi concepts and technologies, most of it in relation to consuming services provided by another bundle.
It is intended to be used as a breakable toy in order to understand the magic OSGi uses for providing its functionality.

## Concepts covered
Currently there are examples for the following classes/concepts:
* `ServiceTracker`
* `ServiceListener`
* `ServiceTrackerCustomizer` (in combination with the `ServiceTracker`)
* Blueprint

## Provided services
To be able to see the stuff in action, there are two services that can be consumed:

* A [`RandomNumberGenerator`](https://github.com/L7R7/magic-bundle/blob/master/service-number-api/src/main/java/de/l7r7/proto/bundle/magic/number/api/RandomNumberGenerator.java) that generates a random integer value
* A [`RandomStringGenerator`](https://github.com/L7R7/magic-bundle/blob/master/service-string-api/src/main/java/de/l7r7/proto/bundle/magic/string/api/RandomStringGenerator.java) that generates a string containing a random Gaussian value

Each of the two services consists of two bundles, one with the API that will be consumed by others and another one containing the actual service implementation (which should be hidden from the consumers)

## Project structure

### General
The bundle structure is represented in the Gradle structure of the project: Each bundle gets its own module.
To simplify the addition of new bundles (in particular the generation of a `build.gradle` file), there's the [`osgi.gradle`](https://github.com/L7R7/magic-bundle/blob/master/gradle/osgi.gradle) file that includes the basic things needed in order to make a Gradle module being deployed to the OSGi container.
The main benefit of the file is that the `build.gradle` file of a bundle can look like this (assuming there is nothing like additional dependencies):

```groovy
group 'de.l7r7.proto'
version '0.1'

apply from: "$rootDir/gradle/osgi.gradle"
```

This is not the most elegant solution and should probably not be used directly in any productive project, but it makes the fast creation of new bundles without much overhead possible.

### Gradle modules
* consumer-blueprint-number
* consumer-blueprint-string
* consumer-listener
* consumer-listener-tracker
* consumer-multi-service
* consumer-pretty-listening-tracker
* consumer-tracker
* consumer-tracker-customizer
* consumer-tracker-customizer-filter
* consumer-util