import pygame
from pygame.locals import *
import random
import GasDrawer
import time
import FilesReader
from SimulationRunner import SimulationRunner
import GasPlotter

simulationRunner = SimulationRunner()

folderPath = "C:/Users/Monkey D. Luffy/Documents/GitHub/lattice-gas-automata/src/results"

# simulationRunner.runSingleSimulation(folderPath)

GasPlotter.plotDataset(folderPath)