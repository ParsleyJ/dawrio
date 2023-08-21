//
// Created by pj on 18/08/23.
//

#include "VolumeMeter.h"


VolumeMeter::~VolumeMeter() {
    if (bufferSize_ > 0) {
        delete[] this->bufferL_;
        delete[] this->bufferR_;
    }
}

VolumeMeter::VolumeMeter(size_t bufferSize) : bufferSize_(bufferSize) {
    if (bufferSize > 0) {
        this->bufferL_ = new double[bufferSize];
        this->bufferR_ = new double[bufferSize];
    }
    this->outputs_[0] = 0.0f;
    this->outputs_[1] = 0.0f;
}


void VolumeMeter::reset() {
    if (bufferSize_ > 0) {
        bzero(bufferL_, bufferSize_);
        bzero(bufferR_, bufferSize_);
    }
    outputs_[0] = outputs_[1] = 0;
    recorded_ = 0;
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

    bool maxed = recorded_ >= bufferSize_;
    double inputL = readInput(0);
    double inputR = readInput(1);

    bufferL_[circularPointer_] = inputL;
    bufferR_[circularPointer_] = inputR;
    circularPointer_ = (circularPointer_ + 1) % bufferSize_;
    recorded_++;
    if (recorded_ > bufferSize_) {
        recorded_ = bufferSize_;
    }

    if (!maxed) {
        outputs_[0] = outputs_[1] = 0.0f;
        return;
    }


    float sumL = 0.0f;
    float sumR = 0.0f;

    for(int i = 0; i < bufferSize_; i++){
        sumL += bufferL_[i]*bufferL_[i];
        sumR += bufferR_[i]*bufferR_[i];
    }

    outputs_[0] = sqrt(sumL / float(bufferSize_));
    outputs_[1] = sqrt(sumR / float(bufferSize_));

}

void VolumeMeter::algo1() {
    if (bufferSize_ == 0) {
        float dbL = 20 * log10(abs(readInput(0)));
        float dbR = 20 * log10(abs(readInput(1)));

        outputs_[0] = float(dbL);
        outputs_[1] = float(dbR);
        return;
    }

    bool maxed = recorded_ >= bufferSize_;
    double inputL = readInput(0);
    double inputR = readInput(1);


    double sumL = sumOfSquaresL_;
    double sumR = sumOfSquaresR_;
    if (maxed) {
        size_t prevPointer = (circularPointer_ - 1) % bufferSize_;
        sumL -= bufferL_[prevPointer];
        sumR -= bufferR_[prevPointer];
    }

    double squareL = inputL * inputL;
    double squareR = inputR * inputR;

    sumL += squareL;
    sumR += squareR;

    bufferL_[circularPointer_] = squareL;
    bufferR_[circularPointer_] = squareR;

    sumOfSquaresL_ = sumL;
    sumOfSquaresR_ = sumR;

    double rmsL = sqrt(sumL / double(recorded_));
    double rmsR = sqrt(sumR / double(recorded_));

//    double dbL = 20 * log10(rmsL);
//    double dbR = 20 * log10(rmsR);

    outputs_[0] = float(rmsL);
    outputs_[1] = float(rmsR);

    circularPointer_ = (circularPointer_ + 1) % bufferSize_;

    recorded_++;
    if (recorded_ > bufferSize_) {
        recorded_ = bufferSize_;
    }
}

float VolumeMeter::emitOutput(size_t index) {
    if (index >= this->getOutputsCount()) {
        return 0.0f;
    }
    return this->outputs_[index];
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_element_VolumeMeter_00024Companion_createVolumeMeter(
    JNIEnv *env,
    jobject thiz,
    jint buffer_size
) {
    return reinterpret_cast<jlong>(new VolumeMeter(buffer_size));
}