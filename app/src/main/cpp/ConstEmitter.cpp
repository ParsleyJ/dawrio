#include "ConstEmitter.h"
#include <jni.h>


float ConstEmitter::emitOutput(size_t index) {
    if(index >= 1){
        return 0.0f;
    }
    return this->emitted_.load();
}

void ConstEmitter::processState(uintmax_t t, int32_t sampleRate) {
    //Do nothing.
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Device_00024Companion_createConstEmitter(JNIEnv *env, jobject thiz,
                                                                      jfloat value) {
    return reinterpret_cast<jlong>(new ConstEmitter(value));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_ConstEmitter_00024Companion_setValue(JNIEnv *env, jobject thiz,
                                                                  jlong handle, jfloat f) {
    reinterpret_cast<ConstEmitter *>(handle)->setEmittedValue(f);
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_ConstEmitter_00024Companion_getValue(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    return reinterpret_cast<ConstEmitter *>(handle)->getEmittedValue();
}