# Characters of LOTR

This application shows all the characters of the Lord of the Rings books.

Uses this API: https://the-one-api.dev/documentation

## Methods

Implements 4 different methods to get the API data:

* `URLConnection + JSONObject` - Standard way, with no dependencies of external libraries
* `OkHttp + JSONObject` - Better communication layer but parsing still done "by hand"
* `OkHttp + Gson` - Higher level but still close to the "wire"
* `Retrofit` - Highest level

You can change the method that is used in LOTRViewModel

## Sync vs Async

To experience the differences between sync and async calls, there is a mixture of both types.

`URLConnection + JSONObject` and `OkHttp + Gson` are executed synchronously (i.e., the call 
blocks until it gets a response from the server)

`OkHttp + JSONObject` and `Retrofit` are executed asynchronously (i.e., the execution continues and 
a callback is called when it gets a response from the server)

Note: You should always use async calls in Android applications, to prevent blocking the main thread.

## Automated tests

Uses MockWebserver (okhttp) to mock the real server

### Unit tests

*Located on app/src/test*

Includes JUnit tests for the 4 methods. Since these are standard JUnit tests, you can run them 
without running the emulator!

### Instrumentation tests

*Located on app/src/androidTest*

These tests launch the application on an emulator and simulate clicking the "Get Characters" button 
and checking the resulting list.

The tricky part is passing the mock web server to the activity. This is done within the onActivity() 
callback, from which we have access to the actual Activity instance and can inject stuff.

## Github actions

This project is automatically checked on each push, using [github actions](https://github.com/features/actions).

You can check the current configuration on `.github/workflows.build.yml`.

There are two jobs:

* checks_and_tests - runs `gradlew check`, which compiles and runs all the unit tests
* integration_tests - runs `gradlew checkConnected`, which runs all instrumentation tests. Since these
tests need to run in an emulator, I use the fantastic 
[reactivecircus/android-emulator-runner](https://github.com/ReactiveCircus/android-emulator-runner).

## Some remarks

* Uses view binding - you have to include in build.gradle:

      buildFeatures {
         viewBinding true
      }

* Uses parcelize - you have to include in build.gradle:

      plugins {
         ...
         id 'kotlin-parcelize'
      }

* Remote calls receive 3 callbacks (only the first is mandatory):
  * onFinished - called when the server returned the message and it was ok
  * onError - called when there was an error communicating with the server or the response was not ok
  * onLoading - called just before initiating the remote request (useful to show a progress indicator)

* Uses suspend functions in retrofit, to simplify code and ease debugging. Requires retrofit 2.6.0+

* Uses mockwebserver for testing - you have to include this dependency in build.gradle

      testImplementation "com.squareup.okhttp3:mockwebserver:4.10.0"

* To use mockwebserver on instrumented tests, you have to include this line in androidmanifest.xml:

      android:usesCleartextTraffic="true"