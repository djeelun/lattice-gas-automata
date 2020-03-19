import pygame
from pygame.locals import *
import random
import GasDrawer
import time
import FilesReader
from SimulationRunner import SimulationRunner

paths = ["../../particles2000", "../../particles3000", "../../particles5000"]

simulationRunner = SimulationRunner()

# simulationRunner.runSingleSimulation(paths[0])
simulationRunner.runMultipleSimulations(paths)