//
// Created by pj on 19/08/23.
//

#ifndef DAWRIO_VALUEADDER_H
#define DAWRIO_VALUEADDER_H

#include "Element.h"

class ValueAdder :
    public Element {
public:

    ValueAdder(float in2Scale, float in2Offset, bool clip, float totalClipRangeStart,
               float totalClipRangeEnd);

    virtual void processState(int32_t sampleRate) override;

    virtual size_t getOutputsCount() override {
        return 1;
    };

    virtual size_t getInputsCount() override {
        return 2;
    }

    virtual float emitOutput(size_t index) override;

    float getIn2Scale() const;

    void setIn2Scale(float in1Scale);

    float getIn2Offset() const;

    void setIn2Offset(float in2Offset);

    bool isClip() const;

    void setClip(bool clip);

    float getTotalClipRangeStart() const;

    void setTotalClipRangeStart(float totalClipRangeStart);

    float getTotalClipRangeEnd() const;

    void setTotalClipRangeEnd(float totalClipRangeEnd);

private:
    float output_ = 0.0f;
    float in2Scale_ = 1.0f;
    float in2Offset_ = 0.0f;
    bool isClippingInRange_ = true;
    float totalClipRangeStart_ = 0.0f;
    float totalClipRangeEnd_ = 0.0f;
};


#endif //DAWRIO_VALUEADDER_H
