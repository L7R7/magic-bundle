# magic-bundle
This project contains a couple of samples for usages of different OSGi concepts and technologies, most of them in relation to consuming services provided by another bundle.
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
The main benefit of the file is that the `build.gradle` file of a bundle will look like this (assuming there is nothing like additional dependencies):

```groovy
group 'de.l7r7.proto'
version '0.1'

apply from: "$rootDir/gradle/osgi.gradle"
```

This is not the most elegant solution and should probably not be used directly in any production project, but it allows the fast creation of new bundles without much overhead.

### Gradle modules
* **consumer-blueprint-number:*** A `RandomNumberGenerator` service is provided via blueprint
* **consumer-blueprint-string:** A `RandomStringGenerator` service is provided via blueprint
* **consumer-listener:** The bundle's Activator will register a `ServiceListener` to get a `RandomNumberGenerator` service instance.
The problem with this approach is, that it won't get a service that is present before the bundle itself is started.
* **consumer-listener-tracker:** This bundle combines a `ServiceTracker` and a `ServiceListener` to get a `RandomNumberGenerator` service instance.
Basically, this bundle combines the functionality of the *consumer-tracker* bundle with the *consumer-listener* bundle.
* **consumer-multi-service:** *not implemented yet*
* **consumer-pretty-listening-tracker:** This bundle does the same thing as the *consumer-listener-tracker* bundle but hides away the OSGi complexity in a utility class called [`CustomGenericDefaultServiceObservingProvidility`](https://github.com/L7R7/magic-bundle/blob/master/consumer-util/src/main/java/de/l7r7/proto/bundle/magic/util/CustomGenericDefaultServiceObservingProvidility.java). 
* **consumer-tracker:** The bundle's Activator will use a `ServiceTracker` to get a `RandomNumberGenerator` service instance.
The caveat here is that the tracker isn't capable of handling services that appear and disappear at runtime with this implementation.
* **consumer-tracker-customizer:** This bundle provides a `RandomNumberGenerator` service instance by using a `ServiceTracker` with a `ServiceTrackerCustomizer`.
With the Customizer it is possible to handle services that come and go at runtime.
The `ServiceTrackerCustomizer` seems to be the intended way to handle dynamic service instances properly (besides Blueprint)
* **consumer-tracker-customizer-filter:** This bundle makes use of the same basic principle as the consumer-tracker-customizer but the `ServiceTracker` is tracking both the `RandomNumberGenerator` and the `RandomStringGenerator` by specifying a [`Filter`](https://osgi.org/javadoc/r4v42/org/osgi/framework/Filter.html) for the `ServiceTracker`.
The services are only consumed if both of them are available (this assumption adds some more complexity and it is done on purpose to simulate a real use case).
Since the two services are of different types, the tracker (and the customizer of course) have to be more generic to be able to deal with the different types.
In the example, the common type of the two services is `Object`.
This requires a `instanceof` check every time a service appears or disappears.
Apart from that a further check is necessary whenever a `ServiceReference` is used: A `ServiceReference` is generic and wraps a service instance of a certain type.
Due to the nature of the JVM, the type of a Generic object can't be determined at runtime.
To get the type (more specifically the class name) of the referenced service, the `ServiceReference` has a property that can be accessed like this: `String objectClass = ((String[]) reference.getProperty(Constants.OBJECTCLASS))[0];`<sup id="a1">[1](#f1)</sup>
An alternative approach for the problem of having to track multiple services is to use an individual `ServiceTracker` for each service.
However, since there has to be a `ServiceTrackerCustomizer` for each tracker, the code becomes confusing pretty quickly. This approach becomes much more complicated when the number of services increases. 
As a rule of thumb: If there are more than two services (and especially if all of them are required), use a Filter. Otherwise use individual trackers.
* **consumer-util:** This bundle contains the [`CustomGenericDefaultServiceObservingProvidility`](https://github.com/L7R7/magic-bundle/blob/master/consumer-util/src/main/java/de/l7r7/proto/bundle/magic/util/CustomGenericDefaultServiceObservingProvidility.java) class used by the consumer-pretty-listening-tracker bundle.
* **service-number-api:** This bundle contains the interface for the `RandomNumberGenerator`.
The service interfaces are separated from their implementation to keep them stable and to hide implementation details.
* **service-number-impl:** This bundle contains the implementation of the `RandomNumberGenerator` interface. The wiring is done using blueprint.
* **service-string-api:** This bundle contains the interface for the `RandomStringGenerator`.
* **service-string-impl:** This bundle contains the implementation of the `RandomStringGenerator` interface. As in the service-number-impl bundle, the implementation is bound to the interface by using blueprint. 
* **servlet-filter:** Here you can find an approach on registering a servlet filter to a servlet.

## So what's the learning?
* ...

---

<b id="f1">1</b> At the moment I'm not sure if there is a case where the array of objectClasses contains more than one element. [↩](#a1)