package comp3011.assignment1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;


// https://api.openai.com/v1/audio/transcriptions 

@RestController
public class AudioController {
    private final TokenCounter tokenCounter;
    public AudioController(TokenCounter tokenCounter) {
        this.tokenCounter = tokenCounter;
    }

    // if server gets a POST request at /api/send-audio then run this
    @PostMapping("/api/send-audio")
    public String receivedAudio(
        @RequestParam("audio-file") MultipartFile file
        // take the "audio-file" object from incoming request
        // and store it in a MultipartFile variable
    ) throws IOException, InterruptedException {
        if (file.isEmpty()) return "Empty audio file.";

        OpenAIClient client = OpenAIOkHttpClient.fromEnv(); 
        var result = client
            .audio()
            .transcriptions()
            .create(
                TranscriptionCreateParams.builder()
                    .file(file.getInputStream())
                    .model("gpt-4o-mini-transcribe")
                    .build());

        
        long inputTokens = result.asTranscription().usage().orElseThrow().asTokens().inputTokens(); 
        long outputTokens = result.asTranscription().usage().orElseThrow().asTokens().outputTokens(); 

        tokenCounter.addTokens(inputTokens, outputTokens);
        String output = result.asTranscription().text();
        return output;
    }
}
