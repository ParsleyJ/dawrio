//
// Created by pj on 14/08/23.
//

#include "Route.h"
#include <jni.h>

bool Route::isActive() const {
    return outDevice != 0 && inDevice != 0;
}

Route::Route(
    jlong outDevice,
    size_t outPort,
    jlong inDevice,
    size_t inPort) :
    outDevice(outDevice),
    outPort(outPort),
    inDevice(inDevice),
    inPort(inPort) {

}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_elementroute_Route_00024Companion_createRoute([[maybe_unused]] JNIEnv *env,
                                                                           [[maybe_unused]] jobject thiz,
                                                              jlong out_device, jint out_port,
                                                              jlong in_device, jint in_port) {
    return reinterpret_cast<jlong>(new Route(out_device, out_port, in_device, in_port));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_elementroute_Route_00024Companion_destroyRoute([[maybe_unused]] JNIEnv *env,
                                                                            [[maybe_unused]] jobject thiz,
                                                                            jlong route_address) {
    delete reinterpret_cast<Route *>(route_address);
}