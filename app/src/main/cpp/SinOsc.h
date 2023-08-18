
#ifndef DAWRIO_SINOSC_H
#define DAWRIO_SINOSC_H

#include <atomic>
#include <stdint.h>
#include "Element.h"

class SinOsc : public Element {
public:
    void processState(int32_t sampleRate) override;
    size_t getOutputsCount() override {
        return 2;
    };
    size_t getInputsCount() override {
        return 1;
    }
    float emitOutput(size_t index) override;


private:
    float outputs_[2];
    float x_ = 0;
};


#endif //DAWRIO_SINOSC_H
