package comp3011.assignment1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

// https://api.openai.com/v1/audio/transcriptions 

@RestController
public class AudioController {
    String api_endpoint = "https://api.openai.com/v1/audio/transcriptions";

    // if server gets a POST request at /api/send-audio then run this
    @PostMapping("/api/send-audio")
    public String receivedAudio(
        @RequestParam("audio-file") MultipartFile file
        // take the "audio-file" object from incoming request
        // and store it in a MultipartFile variable
    ) throws IOException, InterruptedException {
        if (file.isEmpty()) return "Empty audio file.";
        System.out.println("Audio Received: " + file.getOriginalFilename());
        System.out.println("File Size: " + file.getSize());

        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null) {
            System.out.println("No key");
        } else {
            System.out.println("Yes key");
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(api_endpoint))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
        return "";
    }
}
