
#ifndef DAWRIO_SINOSC_H
#define DAWRIO_SINOSC_H

#include <atomic>
#include <stdint.h>
#include "Device.h"

class SinOsc : public Device {
public:
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


#endif //DAWRIO_SINOSC_H
