//
// Created by pj on 15/08/23.
//

#include "LFO.h"
#include "dsp.h"
#include "android/log.h"

float LFO::emitOutput(size_t index) {
    return index == 0 ? output_ : 0.0f;
}

void LFO::processState(int32_t sampleRate) {
    float frequency = this->readInput(0);
    auto type = static_cast<LFO::LFOType>((int) this->readInput(1) % lfoTypes);
    float minValue = this->readInput(2);
    float maxValue = this->readInput(3);
    float range = maxValue - minValue;

    float xInc = 1.0f * frequency / (float) sampleRate;
    this->x_ += xInc;
    float discard;
    this->x_ = std::modf(this->x_, &discard);

    float out;
    if (type == Sin) {
        out = (float) (0.5*sin(two_pi * this->x_)+0.5);
    } else {
        if (type == Saw) {
            out = this->x_;
        }else if(type == SawDesc) {
            out = 1.0f -this->x_;
        }else if (type == Square) {
            out = (float) (this->x_ >= 0.5f);
        } else {
            out = 0.0f;
        }
    }

    this->output_ = out * range + minValue;
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Element_00024Companion_createLFO(JNIEnv *env, jobject thiz) {
    return reinterpret_cast<jlong>(new LFO());
}