//
// Created by pj on 15/08/23.
//

#ifndef DAWRIO_LFO_H
#define DAWRIO_LFO_H


#include "Element.h"
#include <cstdint>


class LFO :
    public Element {

    static const int lfoTypes = 3;
    enum LFOType {
        Sin, Saw, SawDesc, Square
    };


    void processState(int32_t sampleRate) override;

    size_t getOutputsCount() override {
        return 1; // Output
    };

    size_t getInputsCount() override {
        return 4; // Frequency, Type, MinimumValue, MaximumValue
        // lather: Phase
    }

    float emitOutput(size_t index) override;

private:
    float output_ = 0.0f;
    float x_ = 0.0f;
};


#endif //DAWRIO_LFO_H
