
#include <android/log.h>
#include "AudioEngine.h"
#include <thread>
#include <mutex>
#include "Voice.h"

constexpr int32_t kBufferSizeInBursts = 2;

aaudio_data_callback_result_t dataCallback(
    [[maybe_unused]] AAudioStream *stream,
    void *userData,
    void *audioData,
    int32_t numFrames
) {

    (reinterpret_cast<Voice *>(userData))->render(static_cast<float *>(audioData), numFrames);
    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

void errorCallback(
    [[maybe_unused]] AAudioStream *stream,
    void *userData,
    aaudio_result_t error
) {
    if (error == AAUDIO_ERROR_DISCONNECTED) {
        std::function<void(void)> restartFunction =
            [audioEngine = static_cast<AudioEngine *>(userData)]() -> void {
                audioEngine->restart();
            };
        new std::thread(restartFunction);
    }
}

bool AudioEngine::start() {
    AAudioStreamBuilder *streamBuilder;
    AAudio_createStreamBuilder(&streamBuilder);
    AAudioStreamBuilder_setFormat(streamBuilder, AAUDIO_FORMAT_PCM_FLOAT);
    AAudioStreamBuilder_setChannelCount(streamBuilder, 1);
    AAudioStreamBuilder_setPerformanceMode(streamBuilder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setDataCallback(streamBuilder, ::dataCallback, voice_.load());
    AAudioStreamBuilder_setErrorCallback(streamBuilder, ::errorCallback, this);

    aaudio_result_t result = AAudioStreamBuilder_openStream(streamBuilder, &stream_);
    if (result != AAUDIO_OK) {
        __android_log_print(
            ANDROID_LOG_ERROR,
            "AudioEngine",
            "Error opening stream %s",
            AAudio_convertResultToText(result)
        );
        return false;
    }

    int32_t sampleRate = AAudioStream_getSampleRate(stream_);
    voice_.load()->setSampleRate(sampleRate);

    AAudioStream_setBufferSizeInFrames(
        stream_,
        AAudioStream_getFramesPerBurst(stream_) * kBufferSizeInBursts
    );

    result = AAudioStream_requestStart(stream_);
    if (result != AAUDIO_OK) {
        __android_log_print(
            ANDROID_LOG_ERROR,
            "AudioEngine",
            "Error starting stream %s",
            AAudio_convertResultToText(result)
        );
        return false;
    }

    AAudioStreamBuilder_delete(streamBuilder);
    return true;
}

void AudioEngine::restart() {
    static std::mutex restartingLock;
    if (restartingLock.try_lock()) {
        stop();
        start();
        restartingLock.unlock();
    }
}

void AudioEngine::stop() {
    if (stream_ != nullptr) {
        AAudioStream_requestStop(stream_);
        AAudioStream_close(stream_);
    }
}

bool AudioEngine::isSoundOn() {

    Voice *pVoice = this->voice_.load();
    if(pVoice == nullptr){
        return false;
    }
    return pVoice->isActive();
}

void AudioEngine::setSoundOn(bool flag) {
    Voice *pVoice = voice_.load();
    if(pVoice == nullptr){
        return;
    }
    if (flag) {
        pVoice->start();
    } else {
        pVoice->stop();
    }
}

void AudioEngine::setVoice(Voice *voice) {
    this->voice_.store(voice);
}

