# COMP3011 Cloud & Concurrent Programming Assignment 1

## Project Overview

This project is a Java web application that allows users to record an audio clip and get the transcription. The recording is sent from the browser to Java, then sent to OpenAI for transcription.

## Features

- Audio recording in the browser, with visual indicators for recording status
- Audio playback
- Transcription for the recorded audio
- Server uptime
- Graceful shutdown for server
- Global token usage (input and output tokens)
- Ability to handle over 200 concurrent requests
- Dynamically obtain OpenAI API key

## Endpoints

POST request for sending audio file to Java

```
/api/v1/transcribe
```

GET request for server uptime

```
/api/v1/admin/uptime
```

POST request for server graceful shutdown

```
/api/v1/admin/shudown
```

GET request for token usage

```
/api/v1/global/stats
```

## Testing

### Admin Endpooints Tests

The admin API endpoints were tested to ensure that the response bodies were correct. The test was run using:

```Shell
./mvnw -Dtest=AdminControllerTests test > adminTests.txt
```

### Concurrency Tests

The application was tested with 300 concurrent requests and had 300 successful results. The test was run using:

```Shell
./mvnw -Dtest=ConcurrencyTests test > concurrencyTests.txt
```

### API Key Test

The output of the application was tested using:

```Shell
OPENAI_API_KEY="API_KEY_123456" ./mvnw spring-boot:run > apiKeyExposureTests.txt
```

OPENAI_API_KEY was no in the output file, meaning that the application does not expose the API key.

## Running

### Build JAR

```Shell
./mvnw clean package
```

### Run JAR

```Shell
java -jar target/COMP3011-Assignment-1-0.0.1-SNAPSHOT.jar
```

### Run with Springboot

```Shell
./mvnw spring-boot:run
```
