//
// Created by pj on 15/08/23.
//

#include "Volume.h"

void Volume::processState(int32_t sampleRate) {
    auto inputL = readInput(0);
    auto inputR = readInput(1);
    auto amount = readInput(2);

    this->outputs_[0] = amount * inputL;
    this->outputs_[1] = amount * inputR;
}

float Volume::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_element_Element_00024Companion_createVolume([[maybe_unused]] JNIEnv *env,
                                                                         [[maybe_unused]] jobject thiz, jfloat amount) {
    return reinterpret_cast<jlong>(new Volume(amount));
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_element_Volume_00024Companion_getAmount([[maybe_unused]] JNIEnv *env,
                                                                     [[maybe_unused]] jobject thiz,
                                                                    jlong addr) {
    return reinterpret_cast<Volume *>(addr)->getAmount();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_element_Volume_00024Companion_setAmount([[maybe_unused]] JNIEnv *env,
                                                                     [[maybe_unused]] jobject thiz,
                                                                    jlong addr, jfloat amount) {
    reinterpret_cast<Volume *>(addr)->setAmount(amount);
}