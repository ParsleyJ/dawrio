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
    An experimental DAW for Android
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

*Dawrio* (Prototype) represents my personal week-long summer project (August 14th to 21st, 2023). 
Its primary objective was to explore and learn modern Android development technologies and 
practices, including Jetpack Compose and the MVVM architectural pattern.
This was also my first experience in writing algorithms for digital signal processing with the 
Android AAudio framework. 

In this one-week timeframe, I developed a prototype UI for the core technology of the app, the 
*Voice Editor*. 

(**WARNING: VOLUME**)

https://github.com/ParsleyJ/dawrio/assets/3945726/9f558dcf-a8d3-411e-8c2c-3c64ad1b38fe

To ensure full functionality, I also created the audio engine and essential components for audio 
processing in C++.

In Dawrio, Voices are similar to what are usually referred as *tracks* in popular DAWs. 
Looking ahead, the app will support the creation and playback of multiple Voices in the same 
Dawrio *Project*. 
Each voice of will produce audio streams which can be mixed together to create music.

In Dawrio, a *Voice* is a set of interconnected *Devices*, each capable of transmitting signals to 
other devices. In this prototype version of the app, 
there are two types of signals - *Modulation* and *Audio* - and two types of devices - *LFO* 
(Low Frequency Oscillators) and *SawOSC* (Sawtooth-wave-based Oscillators).

Every device in the Voice editor displays adjustable parameters manipulated through dragging 
gestures on knobs. Some knobs (indicated by a distinct color at the center) can be configured to 
receive Modulation signals from other devices. 

Even with just these initial two devices and their capabilities, Dawrio provides the means to 
synthesize some interesting sounds.

https://github.com/ParsleyJ/dawrio/assets/3945726/f4a13aaf-532d-4d47-a04a-fa4257e3f0aa



<!-- CONTRIBUTING -->

## Contributing

If you have a suggestion that would make this better,
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
