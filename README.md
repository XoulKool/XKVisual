# XKVisual
A project undertaken in the Spring Semester of 2017.
It is an audio-waveform visualizer which manipulates artistic renderings of music and adds effects to the music you are listening to.

### Requirements for viewing

* Netbeans IDE v8.2 w/ JavaFx

This is the exact IDE which was this application was built on.  [Click here for a link to install Java SE version.](https://netbeans.org/downloads/)

Once the installation process is completed, clone this repository in and save it as a project.  All the necessary files are already
within the build, so there should be no problems at this point.  Then run the XKVisualTest class as your main to get started!

### ModeStates

There are three primary modes for this application.  All of which can be selected from a dropdown menu selection.
* RunByAudio
* RunByTime
* RunByUser

##### RunByAudio
In this mode the application checks the magnitude of the bass every ten seconds and makes a decision whether or not to change the current
running animation based on this magnitude.  By default, every time new media is selected this mode runs.  Also, the waveform animation always run first by default in this mode.

### Instructions for Proper Enjoyment

Begin by selecting a file.  The application will then automatically begin in RunByAudioMode
