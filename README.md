# Kagel March Madness 2018  

### Description 

This project uses historical results of Men's College Basketball games from the [Google Cloud & NCAA® ML Competition](https://www.kaggle.com/c/mens-machine-learning-competition-2018) in order to predict the results of the 2018 Mens March Madness College Basketball Turnament.

### Implementation

The project was implemented in java and uses gradle for build and execution. Regular Season Games are simulated and Teams are ranked using [ELO Rating System](https://en.wikipedia.org/wiki/Elo_rating_system) and updated after every game.  

### Getting Started 
`These directions have only been tested on Linux Mac OS. Users may need to seek other options to install gradle. `
* Download [`HomeBrew`](https://brew.sh/) by following directions on website
* Install gradle by running the command ```brew install gradle```
* Run `git clone` https://github.com/te21wals/KagelMarchMadness.git from the desired project parent directory to clone the repository
* From the project directory run `./gradle idea` to set up the `/.idea` folder
* From the project directory run `./gradle install` to build the gradle wrapper for KagelMarchMadness project
* From the project directory run `./gradle build` to build the project and create `build/`
* From the project directory run `./gradle clean` to clean the project and delete `build/`
* From the project directory run `./gradle run` to run the `kmm.MarchMadness2018.java`


### Results 
``2017 - 99th Percentile on ESPN March Madness```
