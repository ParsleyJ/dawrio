

#include <math.h>
#include "ValueAdder.h"

void ValueAdder::processState(int32_t sampleRate) {
    float value =
        readInput(0) +in2Scale_ * readInput(1) + in2Offset_;

    if(clip){
        float minRange = fmin(totalClipRangeStart_, totalClipRangeEnd_);
        float maxRange = fmax(totalClipRangeStart_, totalClipRangeEnd_);
        value = fmax(minRange, value);
        value = fmin(maxRange, value);
    }

    this->output_ = value;
}

float ValueAdder::emitOutput(size_t index) {
    if (index > 0) {
        return 0.0f;
    }
    return this->output_;
}

float ValueAdder::getIn2Offset() const {
    return ValueAdder::in2Offset_;
}

void ValueAdder::setIn2Offset(float in2Offset) {
    ValueAdder::in2Offset_ = in2Offset;
}

float ValueAdder::getIn2Scale() const {
    return in2Scale_;
}

void ValueAdder::setIn2Scale(float in2Scale) {
    ValueAdder::in2Scale_ = in2Scale;
}



bool ValueAdder::isClip() const {
    return clip;
}

void ValueAdder::setClip(bool clip) {
    ValueAdder::clip = clip;
}

float ValueAdder::getTotalClipRangeStart() const {
    return totalClipRangeStart_;
}

void ValueAdder::setTotalClipRangeStart(float totalClipRangeStart) {
    totalClipRangeStart_ = totalClipRangeStart;
}

float ValueAdder::getTotalClipRangeEnd() const {
    return totalClipRangeEnd_;
}

void ValueAdder::setTotalClipRangeEnd(float totalClipRangeEnd) {
    totalClipRangeEnd_ = totalClipRangeEnd;
}

ValueAdder::ValueAdder(float in2Scale, float in2Offset, bool clip, float totalClipRangeStart,
                       float totalClipRangeEnd) : in2Scale_(in2Scale), in2Offset_(in2Offset),
                                                  clip(clip),
                                                  totalClipRangeStart_(totalClipRangeStart),
                                                  totalClipRangeEnd_(totalClipRangeEnd) {}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_element_ValueAdder_00024Companion_createValueAdder(
    JNIEnv *env,
    jobject thiz,
    jfloat scale,
    jfloat offset,
    jboolean clip,
    jfloat clipStart,
    jfloat clipEnd
) {
    return reinterpret_cast<jlong>(new ValueAdder(scale, offset, clip, clipStart, clipEnd));
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_element_ValueAdder_00024Companion_getScale2(
    JNIEnv *env,
    jobject thiz,
    jlong address
) {
    return reinterpret_cast<ValueAdder *>(address)->getIn2Scale();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_element_ValueAdder_00024Companion_setScale2(
    JNIEnv *env,
    jobject thiz,
    jlong address,
    jfloat value
) {
    reinterpret_cast<ValueAdder *>(address)->setIn2Scale(value);
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_parsleyj_dawrio_daw_element_ValueAdder_00024Companion_getOffset2(
    JNIEnv *env,
    jobject thiz,
    jlong address
) {
    return reinterpret_cast<ValueAdder *>(address)->getIn2Offset();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_element_ValueAdder_00024Companion_setOffset2(
    JNIEnv *env,
    jobject thiz,
    jlong address,
    jfloat value
) {
    reinterpret_cast<ValueAdder *>(address)->setIn2Offset(value);
}