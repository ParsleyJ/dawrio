//
// Created by pj on 18/08/23.
//

#ifndef DAWRIO_VOLUMEMETER_H
#define DAWRIO_VOLUMEMETER_H


#include "Element.h"
#include <strings.h>


class VolumeMeter :
    public Element {
public:
    VolumeMeter(size_t bufferSize);

    ~VolumeMeter();

    void reset();

    virtual void processState(int32_t sampleRate);

    virtual size_t getOutputsCount() {
        return 1; //dB L/R
    }

    virtual size_t getInputsCount() {
        return 2; //Audio L/R
    }

    virtual float emitOutput(size_t index);


private:
    size_t bufferSize_;
    double *bufferL_;
    double *bufferR_;
    float outputs_[2];
    size_t recorded_ = 0;
    size_t circularPointer_ = 0;
    double sumOfSquaresL_ = 0.0;
    double sumOfSquaresR_ = 0.0;
};


#endif //DAWRIO_VOLUMEMETER_H
