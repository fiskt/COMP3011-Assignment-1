const inputType = "recording";

const recordBtn = document.getElementById("record-btn");
const audioPlayer = document.getElementById("audio-player");
const fileInput = document.getElementById("file-input");
const downloadBtn = document.getElementById("download-btn");

const recordContainer = document.getElementById("record-input-container");
const uploadContainer = document.getElementById("upload-input-container");

const openRecordBtn = document.getElementById("open-record-btn");
const openUploadBtn = document.getElementById("open-upload-btn");

openRecordBtn.addEventListener("click", () => {
    openRecordBtn.disabled = true;
    openUploadBtn.disabled = false;

    recordContainer.hidden = false;
    uploadContainer.hidden = true;
});

openUploadBtn.addEventListener("click", () => {
    openRecordBtn.disabled = false;
    openUploadBtn.disabled = true;

    recordContainer.hidden = true;
    uploadContainer.hidden = false;
});

let chunks = [];
let isRecording = false;
let recordingURL = null;
let mediaRecorder = null;
const constraints = { audio: true };

downloadBtn.addEventListener("click", () => {
    if (!recordingURL) return;

    const downloadLink = document.createElement("a");

    downloadLink.href = recordingURL;
    downloadLink.download = "recording.webm";

    downloadLink.click();
});

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

                const blob = new Blob(chunks, { type: "audio/webm" });
                if (recordingURL) URL.revokeObjectURL(recordingURL);
                recordingURL = URL.createObjectURL(blob);
                downloadBtn.disabled = false;

                handleAudio(blob);
            });
            mediaRecorder.addEventListener("error", (e) => {
                console.error("An error occurred:", e);
            });
        }
        isRecording = true;
        recordBtn.textContent = "Stop";
        chunks = [];
        mediaRecorder.start();
        console.log("recorder started");
    } else {
        isRecording = false;
        recordBtn.textContent = "Record";
        mediaRecorder.stop();
        console.log("recorder stopped");
    }
})

const audioFileInput = document.getElementById("audioFile");
const fileName = document.getElementById("fileStatus");

function handleAudio(audio) {
    fileName.textContent = `Selected: ${audio.name}`;
    audioPlayer.src = window.URL.createObjectURL(audio);
}

fileInput.addEventListener("change", function () {

    const file = fileInput.files[0];

    if (file) {
        handleAudio(file);
    }
});

