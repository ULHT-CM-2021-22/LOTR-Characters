# Characters of LOTR

This application shows all the characters of the Lord of the Rings books.

Uses this API: https://the-one-api.dev/documentation

## Methods

Implements 4 different methods to get the API data:

* URLConnection + JSONObject - Standard way, with no dependencies of external libraries
* OkHttp + JSONObject - Better communication layer but parsing still done "by hand"
* OkHttp + Gson - Higher level but still close to the "wire"
* Retrofit - Highest level

You can change the method that is used in LOTRViewModel

## Sync vs Async

To experience the differences between sync and async calls, there is a mixture of both types.

URLConnection + JSONObject and OkHttp + Gson are executed synchronously (i.e., the call 
blocks until it gets a response from the server)

OkHttp + JSONObject + Retrofit are executed asynchronously (i.e., the execution continues and 
a callback is called when it gets a response from the server)

You should always use async calls in Android applications, to prevent blocking the main thread.

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

* Uses suspend functions in retrofit, to simplify code and ease debugging. Requires retrofit 2.6.0+

* Uses mockwebserver for testing - you have to include this dependency in build.gradle

      testImplementation "com.squareup.okhttp3:mockwebserver:4.10.0"

* To use mockwebserver on instrumented tests, you have to include this line in androidmanifest.xml:

      android:usesCleartextTraffic="true"