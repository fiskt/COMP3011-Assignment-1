package comp3011.assignment1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.io.IOException;
import java.io.InputStream;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.MultipartField;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import java.nio.file.Path;

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

        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        var result =
            client
                .audio()
                .transcriptions()
                .create(
                    TranscriptionCreateParams.builder()
                        .file(file.getInputStream())
                        .model("gpt-4o-mini-transcribe")
                        .build());

        System.out.println(result.asTranscription().text());
        return "";
    }
}
