//
// Created by pj on 14/08/23.
//

#ifndef DAWRIO_SAWOSC_H
#define DAWRIO_SAWOSC_H


#include "Device.h"
#include <cstdint>

class SawOsc : public Device{
    void processState(uintmax_t t, int32_t sampleRate) override;
    size_t getOutputsCount() override {
        return 2;
    };
    size_t getInputsCount() override {
        return 1;
    }
    float emitOutput(size_t index) override;

private:
    float outputs_[2];
};


#endif //DAWRIO_SAWOSC_H
