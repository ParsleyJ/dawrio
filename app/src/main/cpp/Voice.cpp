#include "Voice.h"
#include <jni.h>

Voice::Voice() = default;

Voice::~Voice() = default;

void Voice::start() {
    this->isActive_.store(true);
}

void Voice::stop() {
    this->isActive_.store(false);
}

void Voice::render(float *audioData, int32_t numFrames) {
    size_t devCount = this->getDeviceCount();

    for (int i = 0; i < numFrames; i++) {
        for (int d_i = 0; d_i < devCount; ++d_i) {
            Device *device = getDevice(d_i);
            if (device == nullptr) {
                continue;
            }
            device->processState(sampleRate_);
        }

        audioData[i] = 0.0f;
        Device *outDevice = this->getOutDevice();
        if (outDevice == nullptr) {
            continue;
        }

        size_t outsCount = outDevice->getOutputsCount();
        if (this->isActive() && outsCount > 0) {
            float outputL = outDevice->emitOutput(0);
            //TODO stereoAudio
//            float outputR;
//            if(outsCount < 2){
//                outputR = outputL;
//            }else{
//                outputR = outDevice->emitOutput(1);
//            }
            audioData[i] = (float) (outputL * this->amplitude_);
        }
    }
}

void Voice::setLayout(
    jlong *devices,
    size_t devicesLength,
    jlong *routes,
    size_t routesLength,
    jlong outDevice
) {
    delete[] (this->devicesAddresses_.load());
    delete[] (this->routes_.load());

    this->devicesAddresses_.store(devices);
    this->devicesAddressesLength_.store(devicesLength);
    auto **routesCasted = reinterpret_cast<Route **>(routes);
    this->routes_.store(routesCasted);
    this->routesLength_.store(routesLength);
    this->outDevice_.store(outDevice);

    for (int i = 0; i < devicesLength; i++) {
        auto d = reinterpret_cast<Device *>(devices[i]);
        d->updateRoutes(routesCasted, routesLength);
    }
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_createVoice(
    JNIEnv *env,
    jobject thiz
) {
    return reinterpret_cast<jlong>(new Voice());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_startVoice(
    JNIEnv *env,
    jobject thiz,
    jlong address
) {
    reinterpret_cast<Voice *>(address)->start();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_stopVoice(JNIEnv *env, jobject thiz,
                                                            jlong address) {
    reinterpret_cast<Voice *>(address)->stop();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_destroyVoice(JNIEnv *env, jobject thiz,
                                                               jlong address) {
    delete reinterpret_cast<Voice *>(address);
}



extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_setLayout(JNIEnv *env, jobject thiz,
                                                            jlong address, jlongArray devices,
                                                            jlongArray routes, jlong outDeviceAddress) {
    auto voice = reinterpret_cast<Voice *>(address);
    auto devicesLength = env->GetArrayLength(devices);
    auto routesLength = env->GetArrayLength(routes);
    auto devicesArr = new jlong[devicesLength];
    env->GetLongArrayRegion(devices, 0, devicesLength, devicesArr);
    auto routesArr = new jlong[routesLength];
    env->GetLongArrayRegion(routes, 0, routesLength, routesArr);
    voice->setLayout(devicesArr, devicesLength, routesArr, routesLength, outDeviceAddress);
}