#include <jni.h>
#include <string>
#include "AudioEngine.h"

static AudioEngine *audioEngine = new AudioEngine();

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_beepEvent(JNIEnv *env, jobject jthis, jboolean on) {
    audioEngine->setToneOn(on);
}

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_startEngine(JNIEnv *env, jobject jthis) {
    audioEngine->start();
}

extern "C" JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_stopEngine(JNIEnv *env, jobject jthis) {
    audioEngine->start();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Element_00024Companion_destroy(JNIEnv *env, jobject thiz,
                                                            jlong device_address) {
    delete reinterpret_cast<Element *>(device_address);
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_Element_00024Companion_readElementOutput(JNIEnv *env, jobject thiz,
                                                                      jlong device_address,
                                                                      jint port_number) {
    return reinterpret_cast<Element *>(device_address)->emitOutput(port_number);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_Engine_setVoice(JNIEnv *env, jobject thiz, jlong address) {
    audioEngine->setVoice(reinterpret_cast<Voice *>(address));
}
