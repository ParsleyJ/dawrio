#include "Voice.h"
#include <jni.h>

Voice::Voice() = default;

Voice::~Voice() {
    //Resets all routes
    setLayout(
        this->getDevicesAddressRegion(),
        this->getElementCount(),
        this->getRoutesAddressRegion(),
        0,
        this->outElement_
    );

    size_t routeCount = this->getElementCount();
    for (size_t i = 0; i < routeCount; i++) {
        delete this->routes_.load()[i];
    }

    size_t deviceCount = this->getElementCount();
    for (size_t i = 0; i < deviceCount; i++) {
        delete this->getElement(i);
    }
}

void Voice::start() {
    this->isActive_.store(true);
}

void Voice::stop() {
    this->isActive_.store(false);
}

void Voice::render(float *audioData, int32_t numFrames) {
    size_t devCount = this->getElementCount();

    for (int i = 0; i < numFrames; i++) {
        audioData[i] = 0.0f;
        if (this->isActive()) {
            for (int d_i = 0; d_i < devCount; ++d_i) {
                Element *device = getElement(d_i);
                if (device == nullptr) {
                    continue;
                }
                device->processState(sampleRate_);
            }


            Element *outDevice = this->getOutDevice();
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
}

void Voice::setLayout(
    jlong *devices,
    size_t devicesLength,
    jlong *routes,
    size_t routesLength,
    jlong outDevice
) {
    delete[] (this->elementsAddresses_.load());
    delete[] (this->routes_.load());

    this->elementsAddresses_.store(devices);
    this->elementsAddressesLength_.store(devicesLength);
    auto **routesCasted = reinterpret_cast<Route **>(routes);
    this->routes_.store(routesCasted);
    this->routesLength_.store(routesLength);
    this->outElement_.store(outDevice);

    for (int i = 0; i < devicesLength; i++) {
        auto d = reinterpret_cast<Element *>(devices[i]);
        d->updateRoutes(routesCasted, routesLength);
    }
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_createVoice(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz
) {
    return reinterpret_cast<jlong>(new Voice());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_startVoice(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong address
) {
    reinterpret_cast<Voice *>(address)->start();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_stopVoice(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong address
) {
    reinterpret_cast<Voice *>(address)->stop();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_destroyVoice(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong address
) {
    delete reinterpret_cast<Voice *>(address);
}



extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_updateNativeLayout(
    JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong address,
    jlongArray devices,
    jlongArray routes,
    jlong outDeviceAddress
) {
    auto voice = reinterpret_cast<Voice *>(address);
    auto devicesLength = env->GetArrayLength(devices);
    auto routesLength = env->GetArrayLength(routes);
    auto devicesArr = new jlong[devicesLength];
    env->GetLongArrayRegion(devices, 0, devicesLength, devicesArr);
    auto routesArr = new jlong[routesLength];
    env->GetLongArrayRegion(routes, 0, routesLength, routesArr);
    voice->setLayout(devicesArr, devicesLength, routesArr, routesLength, outDeviceAddress);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_getElementCount(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr
) {
    return (jint) reinterpret_cast<Voice *>(addr)->getElementCount();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_getElements(
    JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr,
    jlongArray result_array
) {
    auto voice = reinterpret_cast<Voice *>(addr);
    auto count = voice->getElementCount();
    env->SetLongArrayRegion(result_array, 0, (jsize) count, voice->getDevicesAddressRegion());
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_getRoutesCount(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr
) {
    return (jint) reinterpret_cast<Voice *>(addr)->getRouteCount();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_getRoutes(
    JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr,
    jlongArray result_array
) {
    auto voice = reinterpret_cast<Voice *>(addr);
    auto count = voice->getRouteCount();
    env->SetLongArrayRegion(result_array, 0, (jsize) count, voice->getRoutesAddressRegion());
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_getOutElement(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr
) {
    return reinterpret_cast<jlong>(reinterpret_cast<Voice *>(addr)->getOutDevice());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_destroyRoute(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr
) {
    delete reinterpret_cast<Route *>(addr);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_parsleyj_dawrio_daw_Voice_00024Companion_destroyElement(
    [[maybe_unused]] JNIEnv *env,
    [[maybe_unused]] jobject thiz,
    jlong addr
) {
    delete reinterpret_cast<Route *>(addr);
}