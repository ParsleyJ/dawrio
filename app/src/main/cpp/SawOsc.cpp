//
// Created by pj on 14/08/23.
//

#include "SawOsc.h"

constexpr auto two_pi = M_PI * 2.0;

void SawOsc::processState(uintmax_t t, int32_t sampleRate) {
    double time = (double) t / (double) sampleRate;
    float frequency = this->readInput(0);
    auto normalizedT = (float) (time * (double) frequency);
    auto value = 2.0f * (normalizedT - floor(0.5 + normalizedT));

    this->outputs_[0] = this->outputs_[1] = (float)value;
}


float SawOsc::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Device_00024Companion_createSawOsc(JNIEnv *env, jobject thiz) {
    return reinterpret_cast<jlong>(new SawOsc());
}