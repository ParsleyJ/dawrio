

#ifndef DAWRIO_ELEMENT_H
#define DAWRIO_ELEMENT_H

#include <stdint.h>
#include <string>
#include <unordered_map>
#include <vector>
#include <jni.h>
#include "Route.h"


class Element {
public:
    virtual void processState(int32_t sampleRate) = 0;

    virtual size_t getOutputsCount() = 0;

    virtual size_t getInputsCount() = 0;

    virtual float emitOutput(size_t index) = 0;

    virtual ~Element() {
        routes_.clear();
    }

    void updateRoutes(Route **routes, size_t length) {
        routes_.clear();
        long thisAddress = reinterpret_cast<jlong>(this);

        for (int i = 0; i < length; i++) {
            Route *route = routes[i];
            if (route->inDevice == thisAddress) {
                routes_.emplace_back(route);
            }
        }
    }


protected:
    Element *getOtherDevice(jlong address) {
        if (address == 0) {
            return nullptr;
        }
        Element *result = reinterpret_cast<Element *>(address);
        return result;
    }

    float readInput(size_t index) {
        long thisAddress = reinterpret_cast<jlong>(this);
        for (const auto &item: routes_) {
            if (!item->isActive()) {
                continue;
            }

            if (item->inDevice != thisAddress || item->inPort != index) {
                continue;
            }

            Element *device = getOtherDevice(item->outDevice);
            if (device == nullptr) {
                continue;
            }

            return device->emitOutput(item->outPort);
        }
        return 0.0f;
    }


    std::vector<Route *> routes_;

};

#endif //DAWRIO_ELEMENT_H
