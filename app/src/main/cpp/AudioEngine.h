//
// Created by pj on 13/08/23.
//

#ifndef DAWRIO_AUDIOENGINE_H
#define DAWRIO_AUDIOENGINE_H

#include "SinOsc.h"
#include "Voice.h"
#include <aaudio/AAudio.h>


class AudioEngine {
public:
    bool start();
    void stop();
    void restart();
    void setSoundOn(bool flag);
    bool isSoundOn();
    void setVoice(Voice* voice);
private:
    std::atomic<Voice*> voice_{nullptr};
    AAudioStream *stream_;
};


#endif //DAWRIO_AUDIOENGINE_H
