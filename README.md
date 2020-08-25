The Long Night Game
===================
This project is a **desktop application** game named “Long Night” which is like a clone to the vintage **Space Invaders** with a different theme.It is made using **Java Programming language** by using **JavaFX** library.

It’s a battle between **United knights** and the **dangerous wights.** The wights lead by **Nightking** tries to invade the whole Westeros(the humans land).



----------


Prerequisites:
-------------



To run this application **Java development Kit(jdk)** has to be installed in the  system and that’s it.Javafx run machine will be included in it. JDK can be installed from the Oracle’s official website under Java SE Development Kit (latest ver 13.0). 


----------


Into the Project
-------------------
The Entire project is made using **JavaFX** library and the fxml files are built using **Gluon SceneBuilder**.As this is a dynamic game,the **Animation timer** class available in JavaFX  is used to update every frame of the game accordingly .

The characters and weapons used by the characters are the instances of **Imageview** class which are added on a child node of a scene named **ground**.

----------
How to Play
-------------------
The controls of the game are the **horizantal arrow keys to move and the space bar to shoot (or use) the weapon.** 

There are **four knights** to fight the wights with their weapons. Each knight has same level of health bar but with different strengths to kill the wights.one might take one shot or two to kill the wights.
The knights have a dragon which breaths fire it can kill a massive amount of wights
but it can be used only limited times and the **dead army(wights)** also has a **dragon** but it  breathes **blue fire** which can kill a knight with one breath. It can attack at any time of the game.

The motive of the game is to **defend Westeros** from the dead army using the knights.

[This is the user interface of the game](https://drive.google.com/file/d/107SGAGzmuVCfV3Cuaw6x0rvqkXW2LPxd/view?usp=sharing)


----------
Running the Game
-------------------

The whole project is constitution of 2 java files and two fxml files Running the Main.java runs the game.It can be done through cmd in windows or terminal in linux.

**Running the Main.java file through command line** 
 
    
    javac Main.java
    
    java Main

----------
Running the Jar File
-------------------

Without running the program we can directly run the jar file of the program and  play the game.

**Running the jar file through command line**

    java -jar LambdaSpaceInvaders.jar

----------
Built With
-------------------

 - **Intellij IDEA** - Development environment used
 
 - **JavaFX**  - Library and Software Platform used.

 - **Gluon Scenebuilder**- Layout tool used to design the UI.

----------
Acknowledgments 
-------------------
- Hat tip to **[Almas Baimagambetov](https://www.youtube.com/channel/UCmjXvUa36DjqCJ1zktXVbUA)** from whom the guidance to use Animation          timer is taken.
- Theme inspiration from **[Game of thrones](https://en.wikipedia.org/wiki/Game_of_Thrones)** - an Epic fantasy TV Series.
- Thanks to [**removebg**](https://www.remove.bg/) for helping me by creating the background  less  png files from jpg  
- Thanks to **[StackEdit](https://stackedit.io/editor#the-long-night-game)** for helping me make this Readme file in the Markdown format.





----------
Table of Contents
-------------------

[TOC]


