//
// Created by pj on 15/08/23.
//

#include "SquareOsc.h"

void SquareOsc::processState(int32_t sampleRate) {
    float frequency = this->readInput(0);
    float xInc = 1.0f * frequency / (float) sampleRate;
    this->x_ += xInc;
    float discard;
    this->x_ = std::modf(this->x_, &discard);
    this->outputs_[0] = this->outputs_[1] = (float) ((this->x_ >= 0.5f) - (this->x_ < 0.5f));
}

float SquareOsc::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0;
    }
    return this->outputs_[index];
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Element_00024Companion_createSquareOsc(JNIEnv *env, jobject thiz) {
    return reinterpret_cast<jlong>(new SquareOsc());
}