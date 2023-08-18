//
// Created by pj on 13/08/23.
//

#include "SinOsc.h"
#include <cmath>
#include "Element.h"
#include <jni.h>
#include "dsp.h"

void SinOsc::processState(int32_t sampleRate) {
    float frequency = this->readInput(0);
    float xInc = 1.0f * frequency / (float) sampleRate;
    this->x_ += xInc;
    float discard;
    this->x_ = std::modf(this->x_, &discard);

    this->outputs_[0] = this->outputs_[1] = (float) sin(two_pi * this->x_);
}


float SinOsc::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Element_00024Companion_createSinOsc(JNIEnv *env, jobject thiz) {
    return reinterpret_cast<jlong>(new SinOsc());
}