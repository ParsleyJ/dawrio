#include <jni.h>
#include <string>
#include "AudioEngine.h"

static AudioEngine *audioEngine = new AudioEngine();

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_setSoundOn(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject jthis,
    jboolean on
) {
    audioEngine->setSoundOn(on);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_parsleyj_dawrio_Engine_isSoundOn(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz
) {
    return audioEngine->isSoundOn();
}

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_startEngine(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject jthis
) {
    audioEngine->start();
}

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_stopEngine(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject jthis
) {
    audioEngine->start();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_element_Element_00024Companion_destroy(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong device_address
) {
    delete reinterpret_cast<Element *>(device_address);
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_element_Element_00024Companion_readElementOutput(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong device_address,
    jint port_number
) {
    return reinterpret_cast<Element *>(device_address)->emitOutput(port_number);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_setVoice(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong address
) {
    audioEngine->setVoice(reinterpret_cast<Voice *>(address));
}

