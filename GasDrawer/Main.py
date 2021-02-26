import pygame
from pygame.locals import *
import random
import GasDrawer
import time
import FilesReader
from SimulationRunner import SimulationRunner
import GasPlotter

simulationRunner = SimulationRunner()

simulationRunner.runSingleSimulation('./results')