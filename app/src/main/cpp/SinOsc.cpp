//
// Created by pj on 13/08/23.
//

#include "SinOsc.h"
#include <cmath>
#include "Device.h"
#include <jni.h>


constexpr const double two_pi = M_PI * 2.0;

void SinOsc::processState(uintmax_t t, int32_t sampleRate) {
    double time = (double) t / (double) sampleRate;
    float frequency = this->readInput(0);
    this->outputs_[0] = this->outputs_[1] = (float) sin(two_pi * frequency * time);
}



float SinOsc::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Device_00024Companion_createSinOsc(JNIEnv *env, jobject thiz) {
    return reinterpret_cast<jlong>(new SinOsc());
}