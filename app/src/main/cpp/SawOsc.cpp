//
// Created by pj on 14/08/23.
//

#include "SawOsc.h"
#include "dsp.h"

void SawOsc::processState(int32_t sampleRate) {
    float frequency = this->readInput(0);
    float xInc = 1.0f * frequency / (float) sampleRate;
    this->x_ += xInc;
    float discard;
    this->x_ = std::modf(this->x_, &discard);
    auto value = 2.0f * this->x_ - 1.0f;
    this->outputs_[0] = this->outputs_[1] = (float) value;
}


float SawOsc::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_element_Element_00024Companion_createSawOsc(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz) {
    return reinterpret_cast<jlong>(new SawOsc());
}