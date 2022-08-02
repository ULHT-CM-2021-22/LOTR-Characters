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

Includes JUnit tests for the 4 methods. You can test the actual API request without running the emulator!

Disclaimer: The API calls should be mocked but, in this case, the idea is to 
experiment with the real API. And the number of characters in Tolkien's books shouldn't 
change in the future, anyway :)

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