# Kagel March Madness 2018

### Description 

This project uses historical results of Men's College Basketball gameOutcomes from the [`Google Cloud & NCAA® ML Competition`](https://www.kaggle.com/c/mens-machine-learning-competition-2018) in order to predict the results of the 2018 Mens March Madness College Basketball Turnament.

### Implementation

The project was implemented in java and uses gradle for build and execution. Regular Season Games are simulated and Teams are ranked using [`ELO Rating System`](https://en.wikipedia.org/wiki/Elo_rating_system) and updated after every gameOutcome.  

### Getting Started 
* Run `git clone https://github.com/te21wals/KagelMarchMadness.git` from the desired project parent directory to clone the repository

#### Gradle Tasks
* From the project directory run `./gradlew idea` to set up the `.idea/` directory
* From the project directory run `./gradlew build` to build the project and create `build/`
* From the project directory run `./gradlew clean` to clean the project and delete `build/`
* From the project directory run `./gradlew run` to run the `kmm.MarchMadness2018.java`

#### Docker Tasks 
``` Docker must be installed for these commands to work ```
* From the project directory run `./gradlew dockerPrepare` to set up the `build/docker` directory 
* From the project directory run `./gradlew dockerClean` to delete `build/docker` directory 
* From the project directory run `./gradlew docker` to build the docker image `hub.docker.com/te21wals/kmm:SNAPSHOT`
* From the project directory run `./gradlew dockeRun` to run the `hub.docker.com/te21wals/kmm:SNAPSHOT` image inside of a local docker container

### Results 
`2017 - 99th Percentile on ESPN March Madness`
