# weather

This is a Kotlin based console application that queries upcoming forecast data from the Open Weather API, analyzes it based on some business rules, and prints out the best method of contact for the next 5 days. 

The easiest way to run the app is using the vendored gradle wrapper. The app expects to find an environment variable called `API_KEY` set with a valid Open Weather API key and will not run without it. 

```
API_KEY=<your key> ./gradlew :run
```
or on windows
```
set API_KEY=<your key> && gradlew.bat :run
```
