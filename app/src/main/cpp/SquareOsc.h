//
// Created by pj on 15/08/23.
//

#ifndef DAWRIO_SQUAREOSC_H
#define DAWRIO_SQUAREOSC_H


#include "Element.h"
#include <cstdint>

class SquareOsc : public Element {
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
    float x_ = 0.0f;
};

#endif //DAWRIO_SQUAREOSC_H
