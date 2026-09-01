const inputType = "recording";

const recordBtn = document.getElementById("record-btn");
const audioPlayer = document.getElementById("audio-player");
const fileInput = document.getElementById("file-input");
const downloadBtn = document.getElementById("download-btn");

const sendBtn = document.getElementById("send-btn");

const recordContainer = document.getElementById("record-input-container");
const uploadContainer = document.getElementById("upload-input-container");

const openRecordBtn = document.getElementById("open-record-btn");
const openUploadBtn = document.getElementById("open-upload-btn");

const audioContainer = document.querySelector(".audio-container");

const textOutput = document.querySelector("text-output-box");

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

    result = await response.text();
    if (result != "") textOutput.textContent = result;

    console.log(result);
});

let chunks = [];
let isRecording = false;
let recordedBlob = null;
let mediaRecorder = null;
const constraints = { audio: true };

downloadBtn.addEventListener("click", () => {
    if (!recordedBlob) return;

    const downloadLink = document.createElement("a");

    downloadLink.href = recordedBlob;
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

                recordedBlob = new Blob(chunks, { type: "audio/webm" });
                downloadBtn.disabled = false;

                handleAudio(recordedBlob);
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

const audioFileInput = document.getElementById("audioFile");
const fileName = document.getElementById("fileStatus");

function handleAudio(audio) {
    fileName.textContent = (recordedBlob) 
        ? `Selected: New Recording`
        : `Selected: ${audio.name}`;
    audioPlayer.src = window.URL.createObjectURL(audio);
}

fileInput.addEventListener("change", function () {
    const file = fileInput.files[0];

    if (file) {
        handleAudio(file);
    }
});

