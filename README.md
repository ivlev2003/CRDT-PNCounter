# CRDT-PNCounter

This is a project written in Kotlin that implements the CRDT (Conflict-Free Replicated Data Type) Positive-Negative Counter. The CRDT Positive-Negative Counter is a type of counter that supports increment and decrement operations and can be replicated across multiple devices without conflicting.

## Build with Gradle

To build this project with Gradle, follow these steps:

1. Clone the repository to your local machine:
```
   git clone git@github.com:ivlev2003/CRDT-PNCounter.git
```
2. Navigate to the root directory of the project.
3. Build the project using Gradle:
```
./gradlew build
```
This will compile the project and run all the tests.

## Usage

To use the CRDT Positive-Negative Counter in your Kotlin project, add the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation 'com.github.ivlev2003:CRDT-PNCounter:1.0.0'
}
```
Then, you can create a new PNCounter object and use the inc() and dec() methods to modify the counter:
```kotlin
// Create a new PNCounter with a unique ID
val counter1 = PNCounter(0, 1)

// Increment the counter by 3
counter1.inc(3)

// Decrement the counter by 2
counter1.dec(2)

// Get the current value of the counter
val value = counter1.value

println("Counter value: $value")
```
You can also merge two counters together by calling the merge() method:
```kotlin
// Create two PNCounters
val counter1 = PNCounter(0, 2)
val counter2 = PNCounter(1, 2)

// Increment counter1 by 2
counter1.inc(2)

// Increment counter2 by 3
counter2.inc(3)

// Merge counter2 into counter1
counter1.merge(counter2)

// Get the current value of the counter
val value = counter1.value

println("Counter value: $value")
    
```
In this example, the value of counter1 will be 5 after the merge operation.
