package comp3011.assignment1;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioController {

    // if server gets a POST request at /api/send-audio then run this
    @PostMapping("/api/send-audio")
    public String receivedAudio(
        @RequestParam("audio-file") MultipartFile file
        // take the "audio-file" object from incoming request
        // and store it in a MultipartFile variable
    ) {
        if (file.isEmpty()) return "Empty audio file.";

        System.out.println("Audio Received: " + file.getOriginalFilename());
        System.out.println("File Size: " + file.getSize());

        return "";
    }
}
