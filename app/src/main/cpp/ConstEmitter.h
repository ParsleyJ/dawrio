//
// Created by pj on 14/08/23.
//

#ifndef DAWRIO_CONSTEMITTER_H
#define DAWRIO_CONSTEMITTER_H


#include "Device.h"

class ConstEmitter : public Device {
public:
    ConstEmitter(float f){
        emitted_.store(f);
    }

    void processState(uintmax_t t, int32_t sampleRate) override;

    size_t getOutputsCount() override {
        return 1;
    }

    size_t getInputsCount() override {
        return 0;
    }

    float emitOutput(size_t index) override;

    float getEmittedValue(){
        return emitted_.load();
    }

    void setEmittedValue(float v){
        this->emitted_ = v;
    }

private:
    std::atomic<float> emitted_{0.0};
};


#endif //DAWRIO_CONSTEMITTER_H
