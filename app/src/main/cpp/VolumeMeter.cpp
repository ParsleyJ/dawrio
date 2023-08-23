//
// Created by pj on 18/08/23.
//

#include <android/log.h>
#include "VolumeMeter.h"


void VolumeMeter::reset() {
    if (bufferSize_ > 0) {
        bzero(bufferL_, bufferSize_);
        bzero(bufferR_, bufferSize_);
    }
    outputs_[0] = outputs_[1] = 0.0f;
    circularPointer_ = 0;
    sumOfSquaresL_ = 0.0;
    sumOfSquaresR_ = 0.0;
}

void VolumeMeter::processState(int32_t sampleRate) {
    if (bufferSize_ == 0) {
        float dbL = 20 * log10(abs(readInput(0)));
        float dbR = 20 * log10(abs(readInput(1)));

        outputs_[0] = float(dbL);
        outputs_[1] = float(dbR);
        return;
    }

    if (isnan(sumOfSquaresL_)) {
        sumOfSquaresL_ = 0.0f;
    }

    if (isnan(sumOfSquaresR_)) {
        sumOfSquaresR_ = 0.0f;
    }

    auto inL = double(readInput(0));
    auto inR = double(readInput(1));

    double newestL = inL * inL;
    double newestR = inR * inR;
    auto oldestL = bufferL_[circularPointer_];
    auto oldestR = bufferR_[circularPointer_];

    bufferL_[circularPointer_] = newestL;
    bufferR_[circularPointer_] = newestR;

    ++circularPointer_;
    circularPointer_ %= bufferSize_;

    sumOfSquaresL_ += newestL - oldestL;
    sumOfSquaresR_ += newestR - oldestR;

    if (isnan(sumOfSquaresL_)) {
        sumOfSquaresL_ = 0.0f;
    }

    if (isnan(sumOfSquaresR_)) {
        sumOfSquaresR_ = 0.0f;
    }

    auto meanL = float(sumOfSquaresL_ / double(bufferSize_));
    auto meanR = float(sumOfSquaresR_ / double(bufferSize_));
    outputs_[0] = sqrtf(meanL>=0.0f?meanL:0.0f);
    outputs_[1] = sqrtf(meanR>=0.0f?meanR:0.0f);

}

float to_dB(float input) {
    return 20 * log10(input);
}


float VolumeMeter::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}

VolumeMeter::~VolumeMeter() {
    if (bufferSize_ > 0) {
        delete[] this->bufferL_;
        delete[] this->bufferR_;
    }

}

VolumeMeter::VolumeMeter(size_t bufferSize) {
    bufferSize_ = bufferSize;
    if (bufferSize > 0) {
        this->bufferL_ = new double[bufferSize];
        this->bufferR_ = new double[bufferSize];
    }
    reset();
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_element_VolumeMeter_00024Companion_createVolumeMeter(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jint buffer_size
) {
    return reinterpret_cast<jlong>(new VolumeMeter(buffer_size));
}