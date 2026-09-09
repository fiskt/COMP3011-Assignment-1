const inputType = "recording";

const recordBtn = document.getElementById("record-btn");
const audioPlayer = document.getElementById("audio-player");

const sendBtn = document.getElementById("send-btn");

const recordContainer = document.getElementById("record-input-container");

const audioContainer = document.querySelector(".audio-container");

const textOutput = document.querySelector(".text-output-box");

sendBtn.addEventListener("click", async () => {
    if (!recordedBlob) return;

    const data = new FormData();

    data.append(
        "audio-file",       // field name
        recordedBlob,       // contents
        "recording.webm"    // content file name
    );

    const response = await fetch("/api/send-audio", {
        method: "POST",
        body: data
    });

    var result = await response.text();
    textOutput.textContent = result;
});

let chunks = [];
let isRecording = false;
let recordedBlob = null;
let mediaRecorder = null;
const constraints = { audio: true };

recordBtn.addEventListener("click", async () => {
    if (!isRecording) {
        if (!mediaRecorder) {
            const stream = await navigator.mediaDevices.getUserMedia(constraints);
            mediaRecorder = new MediaRecorder(stream, { mimeType: "audio/webm" });
            mediaRecorder.addEventListener("dataavailable", (e) => {
                console.log("Data available");
                chunks.push(e.data);
            });
            mediaRecorder.addEventListener("stop", (e) => {
                console.log("onstop fired");

                recordedBlob = new Blob(chunks, { type: "audio/webm" });
                audioPlayer.src = window.URL.createObjectURL(recordedBlob);
            });
            mediaRecorder.addEventListener("error", (e) => {
                console.error("An error occurred:", e);
            });
        }
        isRecording = true;
        audioContainer.classList.add("recording");
        recordBtn.textContent = "Stop";
        chunks = [];
        mediaRecorder.start();
        console.log("recorder started");
    } else {
        isRecording = false;
        audioContainer.classList.remove("recording");
        recordBtn.textContent = "Record";
        mediaRecorder.stop();
        console.log("recorder stopped");
    }
})