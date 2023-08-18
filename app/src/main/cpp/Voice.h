//
// Created by pj on 13/08/23.
//

#ifndef DAWRIO_VOICE_H
#define DAWRIO_VOICE_H

#include <atomic>
#include <vector>
#include "Element.h"
#include "Route.h"

class Voice {
public:
    Voice();

    ~Voice();

    void start();

    void stop();

    void render(float *audioData, int32_t numFrames);

    void setSampleRate(int32_t sampleRate) {
        this->sampleRate_ = sampleRate;
    }

    int32_t getSampleRate() {
        return this->sampleRate_;
    }

    void setAmplitude(double amplitude) {
        this->amplitude_.load();
    }

    double getAmplitude() {
        return this->amplitude_.load();
    }

    bool isActive() {
        return this->isActive_.load();
    }

    size_t getDeviceCount() {
        return this->devicesAddressesLength_.load();
    }

    Element *getDevice(size_t index) {
        return reinterpret_cast<Element *>(this->devicesAddresses_.load()[index]);
    }

    jlong* getDevicesAddressRegion() {
        return this->devicesAddresses_.load();
    }

    jlong* getRoutesAddressRegion() {
        return reinterpret_cast<jlong*>(this->routes_.load());
    }

    size_t getRouteCount() {
        return this->routesLength_.load();
    }

    Element *getOutDevice() {
        return reinterpret_cast<Element *>(this->outDevice_.load());
    }


    void setLayout(
        jlong *devices,
        size_t devicesLength,
        jlong *routes,
        size_t routesLength,
        jlong outDevice
    );



private:
    std::atomic<Route **> routes_{nullptr};
    std::atomic<size_t> routesLength_{0};
    std::atomic<jlong *> devicesAddresses_{nullptr};
    std::atomic<size_t> devicesAddressesLength_{0};
    std::atomic<jlong> outDevice_ = 0;
    std::atomic<bool> isActive_{false};
    std::atomic<double> amplitude_{0.5};
    int32_t sampleRate_ = 0;
};


#endif //DAWRIO_VOICE_H
