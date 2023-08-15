//
// Created by pj on 14/08/23.
//

#ifndef DAWRIO_ROUTE_H
#define DAWRIO_ROUTE_H

#include <cstdint>
#include <jni.h>

class Route {
public:
    Route(jlong outDevice, size_t outPort, jlong inDevice, size_t inPort);

    jlong outDevice;
    size_t outPort;
    jlong inDevice;
    size_t inPort;
    bool isActive();
};


#endif //DAWRIO_ROUTE_H
