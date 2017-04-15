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
