# XKVisual
A project undertaken in the Spring Semester of 2017.
It is an audio-waveform visualizer which manipulates artistic renderings of music based on the audio playing.

### Requirements for viewing

* JDK 8
* JavaFX 2.2

Java 8 is the only verified sdk installation which works with JavaFX 2.2, which is used for the frontend UI for this application.
Oracle discontinued JavaFX after this installation and unknown errors
may occur if a different installation is used.

Download JavaFX 2.2 [here](https://www.oracle.com/java/technologies/javafx2-archive-downloads.html)

After doing so, be sure the `lib` directory in Java FX 2.2 runtime is added to the list of libraries in your project.

If there is an error pertaining to the icon files upon build, try copying and pasting the icon files found in the clone into wherever
the build folder for this project is within your machine [Typically in /NetbeansProjects/XKVisual/Build].  The code is designed to work
in this way.

### Modes

There are three primary modes for this application.  All of which can be selected from a dropdown menu selection.
* RunByAudio
* RunByTime
* RunByUser

##### RunByAudio
In this mode the application checks the magnitude of the bass every ten seconds and makes a decision whether or not to change the current
running animation based on this magnitude.  By default, every time new media is selected this mode runs.  Also, the waveform animation
always run first by default in this mode.

##### RunByTime
Animations change randomly every fifteen seconds based on no criteria.  I created this mode so that a user is practically guaranteed a
unique experience to a particular song.

##### RunByUser
Once this button is selected from the `Mode Select` menu dropdown button, the User can then select any of the animations created from the
`Animation Select` menu dropdown.  Take care to note that you can not select an animation if you have not yet pressed the Run By User selection in Mode Select.

### Instructions for Proper Enjoyment

Begin by selecting a file.  The application will then automatically begin in RunByAudioMode, autoplaying the selected media.  To pause
the media and watch the animations fade way, hit the pause button.  To continue, press the play button.  To set the media back to the
beginning of the song hit the stop button, and press play again to start the music back up.  To change mode, select an option from the 
dropdown `Mode Select` button.  To select an animation while in User Mode, select an option from the dropdown `Animation Select` button.

To open a new file, click the open file button and you will be prompted to select a .mp3 from your music folder once again.

Enjoy :)
