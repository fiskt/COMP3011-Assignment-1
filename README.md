# COMP3011 Cloud & Concurrent Programming Assignment 1

## Project Overview

This project is a Java web application that allows users to record an audio clip and get the transcription. The recording is sent from the browser to Java, which is then sent to OpenAI for the transcription.

## Features

- Audio recording in the browser, with visual indicators for recording status
- Audio playback
- Transcription for the recorded audio
- Server uptime
- Graceful shutdown for server
- Global token usage (input and output tokens)
- Ability to handle over 200 concurrent requests

## Endpoints

POST request for sending audio to Java

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

### Concurrency Tests

The application was tested with 300 concurrent requests and had 300 successful results. 

### API Key Test

The output of the application was tested using:

```Shell
OPENAI_API_KEY="TEST_KEY_123" ./mvnw spring-boot:run > output.txt
```

OPENAI_API_KEY was no in the output file, meaning that the application does not expose the API key.

## Running

### Build JAR

### Run with Springboot

### Run with localhost
