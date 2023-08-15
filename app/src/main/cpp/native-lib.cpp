#include <jni.h>
#include <string>
#include "AudioEngine.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_parsleyj_dawrio_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

static AudioEngine *audioEngine = new AudioEngine();

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_MainActivity_beepEvent(JNIEnv *env, jobject jthis, jboolean on) {
    audioEngine->setToneOn(on);
}

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_MainActivity_startEngine(JNIEnv *env, jobject jthis) {
    audioEngine->start();
}

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_MainActivity_stopEngine(JNIEnv *env, jobject jthis) {
    audioEngine->start();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Device_00024Companion_destroy(JNIEnv *env, jobject thiz,
                                                           jlong device_address) {
    delete reinterpret_cast<Device *>(device_address);
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_Device_00024Companion_readDeviceOutput(JNIEnv *env, jobject thiz,
                                                                    jlong device_address,
                                                                    jint port_number) {
    return reinterpret_cast<Device *>(device_address)->emitOutput(port_number);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_MainActivity_setVoice(JNIEnv *env, jobject thiz, jlong address) {
    audioEngine->setVoice(reinterpret_cast<Voice *>(address));
}