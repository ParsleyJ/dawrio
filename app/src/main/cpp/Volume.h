//
// Created by pj on 15/08/23.
//

#ifndef DAWRIO_VOLUME_H
#define DAWRIO_VOLUME_H


#include "Element.h"

class Volume:public Element {
public:
    Volume(float am): amount_(am){}

    virtual void processState(int32_t sampleRate);

    virtual size_t getOutputsCount(){
        return 2;
    }

    virtual size_t getInputsCount(){
        return 3; //Audio L/R, Amount
    }

    virtual float emitOutput(size_t index);

    float getAmount(){
        return this->amount_;
    }

    void setAmount(float amount){
        this->amount_ = amount;
    }

private:
    float amount_ = 1.0;
    float outputs_[2];

};


#endif //DAWRIO_VOLUME_H
