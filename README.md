# Lattice Gas Automata

## About
This project was made as an assignment for a course called 'Simulación de Sistemas' (Systems simulation).

2D gas simulation inside two containers joined by a hole using **Lattice Gas Cellular Automaton** models. The project follows closely the implementation specified in the document [Lattice Gas Models](https://github.com/Fastiz/lattice-gas-automata/blob/master/02_Lattice%20Gas%20Models.pdf).

## Project content

The project contains the simulator that is written in java that saves the simulation data inside files and then a drawer implemented in python/pygame for displaying the results.

## How to run

### Simulation
For running the simulation mark *src* as sources root and then run *src/Main.java* with the following arguments and format (all optional):

* **width** - the total width of the two containers (defaults 200)
* **height** - the height of the containers (defaults 200)
* **numberOfParticles** - the number of particles in the simulation (defaults 5000)
* **numberOfIterations** - number of time steps to be computed (defaults 3000)
* **chunkSize** - when representing the velocity in each position with take an average of the velocities near the position. The chunkSize then defines the size of the recint that takes the average from. (defaults 5)

All arguments should be specified in the format *key=value* where key may be one of the previous values.

Arguments example: 
'width=100 numberOfParticles=10000'

After the simulation ends a folder called 'results' will be created in the root directory with two files, 'dynamicFile' and 'staticFile'.

### Displaying the simulation
For display python must be installed with [pygame](https://www.pygame.org/) dependency.

After the dependency is install just run:
python GasDrawer/Main.py

## Demo

[Video of a simulation with 5000 particles](https://www.youtube.com/watch?v=KlcD4SNd7eY)

[Video of a simulation with 10000 particles](https://www.youtube.com/watch?v=TCwIQ9LxB0s)

## Aditional information

For the assigment it was required to make a presentation about our implementation and to showcase the results obtained. In the presentation we define the criteria for the system to be in equilibrium and then we analyze the time steps required.

[Link to the presentation](https://github.com/Fastiz/lattice-gas-automata/blob/master/TP2%20-%20FHP%20-%20PRESENTACION.pdf) (in spanish)

