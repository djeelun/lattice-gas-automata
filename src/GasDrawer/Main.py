import pygame
from pygame.locals import *
import random
import GasDrawer
import time
import FilesReader
from SimulationRunner import SimulationRunner
import GasPlotter

paths = ["../../particles2000", "../../particles3000", "../../particles5000"]

simulationRunner = SimulationRunner()

GasPlotter.runPlotter()

#simulationRunner.runSingleSimulation(paths[2])
simulationRunner.runMultipleSimulations(paths)
