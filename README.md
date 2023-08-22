<a name="readme-top"></a>


<!-- PROJECT SHIELDS -->
[![Kotlin][Kotlin]][Kotlin-url]
[![Cpp][Cpp]][Cpp-url]
[![Android][Android]][Android-url]
[![Compose][Compose]][Compose-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">

  <a href="https://github.com/ParsleyJ/dawrio">
    <img src="dawrio_logo.png" alt="Logo" width="128" height="128">
  </a>


<h3 align="center">Dawrio</h3>

  <p align="center">
    The missing DAW for Android.
    <!--
    <br />
    <a href="https://github.com/ParsleyJ/dawrio"><strong>Explore the docs »</strong></a>
    -->
    <br />
    <a href="https://github.com/ParsleyJ/dawrio/issues">Report Bug/Request Feature</a>
  </p>
</div>




<!-- ABOUT THE PROJECT -->

## About the project

The *Dawrio* (Prototype) was my personal one-week long (August 14th to 21st, 2023) summer project. I started it to learn modern Android development technologies and practices, such as Jetpack Compose and the MVVM architectural pattern. 
This is also my first experience in writing algorithms for digital signal processing with the Android AAudio framework. 

In this week I managed to develop a prototype UI for the core technology of the app, which is the *Voice Editor*. To make it fully functional, I also developed the audio engine and the core components for audio processing in C++.




In Dawrio, Voices are similar to what are usually referred as *tracks* in popular DAWs. In the future, the app will support the creation of multiple Voices in the same Dawrio *Project*, each of which will generate audio streams which could be mixed together to produce music.

In Dawrio, a *Voice* is a set of interconnected *Devices*, where each Device can transmit signals to other devices. In this prototype version, there are two types of signals - *Modulation* and *Audio* - and two types of devices - *LFO* (Low Frequency Oscillators) and *SawOSC* (Sawtooth-wave-based Oscillator).

Each device in the Voice editor has parameters that can be changed with dragging gestures on knobs. Some knobs (highlighted by a different color at the center) can be configured to receive Modulation signals from other devices. 

These first two devices and their abilities alone already provide the means to synthesize some interesting sounds.



<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag `enhancement`.

<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE.txt` for more information.




<!-- CONTACT -->

## Contact

Giuseppe Petrosino - parsleyjoe@gmail.com

Project Link: [https://github.com/ParsleyJ/dawrio](https://github.com/ParsleyJ/dawrio)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->

[contributors-url]: https://github.com/ParsleyJ/dawrio/graphs/contributors

[forks-url]: https://github.com/ParsleyJ/dawrio/network/members

[stars-url]: https://github.com/ParsleyJ/dawrio/stargazers

[issues-url]: https://github.com/ParsleyJ/dawrio/issues

[license-shield]: https://img.shields.io/github/license/ParsleyJ/dawrio?style=for-the-badge

[license-url]: https://github.com/ParsleyJ/dawrio/blob/master/LICENSE.txt


[Kotlin]: https://img.shields.io/badge/kotlin-20232A?style=for-the-badge&logo=kotlin

[Kotlin-url]: https://kotlinlang.org/

[Cpp]: https://img.shields.io/badge/C++-20232A?style=for-the-badge&logo=c%2B%2B

[Cpp-url]: https://cplusplus.com/

[Android]: https://img.shields.io/badge/Android-20232A?style=for-the-badge&logo=Android

[Android-url]: https://developer.android.com/

[Compose]: https://img.shields.io/badge/Jetpack%20Compose-20232A?style=for-the-badge&logo=jetpack-compose

[Compose-url]: https://github.com/gram-js/gramjs