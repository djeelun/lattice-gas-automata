from operator import ne
import numpy as np
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
folderPath2 = "C:/Users/Monkey D. Luffy/Documents/GitHub/lattice-gas-automata/figures/"
all_folders = ["FHP 2k", "FHP 3k", "FHP 5k", "FHP 10k", "FHP 20k", "FHP 50k", "FHP 75k", "FHP 100k", "FHP 115k",
               "HPP 2k", "HPP 3k", "HPP 5k", "HPP 10k", "HPP 20k", "HPP 50k", "HPP 75k"]

all_ns = [2000, 3000, 5000, 10000, 20000, 50000, 75000, 100000, 115247,
          2000, 3000, 5000, 10000, 20000, 50000, 75000]


# if len(all_ns) != len(all_folders):
#     print("List lengths must be the same")
#     exit()

# simulationRunner.runSingleSimulation(folderPath)

# for i in range(0, len(all_folders)):
#     folder_name = folderPath2 + all_folders[i]
#     model_name = all_folders[i].split(" ")[0]
#     GasPlotter.plotDataset(folder=folder_name, numParticles=all_ns[i], model=model_name)

# for i in range(0, len(all_folders)):
#     folder_name = folderPath2 + all_folders[i]
#     model_name = all_folders[i].split(" ")[0]
#     GasPlotter.plotDifference(folder=folder_name, numParticles=all_ns[i], model=model_name)

# GasPlotter.plotDataset(folder=folderPath2 + "FHP 115k" + "/FHPparticleDistributions(115247)", numParticles=115247)
aran = np.arange(5000, 75001, 5000)

# for num_particles in aran:
#     folder_name = folderPath + "/FHPparticleDistributions(" + str(num_particles) + ")"
#     model_name = "FHP"
#     GasPlotter.plotInterval(folder=folder_name, num_particles=num_particles, model=model_name)

GasPlotter.plotAllEquilibria(folderPath + "/FHPparticleDistributions", folderPath + "/HPPparticleDistributions", np.arange(5000, 75001, 5000), folderPath)
# GasPlotter.plotAllIntervals(folderPath + "/FHPparticleDistributions", folderPath + "/HPPparticleDistributions", np.arange(5000, 75001, 5000), folderPath)

# GasPlotter.plotEquilibrium(folder=folderPath2 + "FHP 100k" + "/particleDistributions(100000)", num_particles=100000)

# GasPlotter.plotFrechetDist(folderPath + "/FHPparticleDistributions", folderPath + "/HPPparticleDistributions", np.arange(5000, 75001, 5000), folderPath)

# li = [11, 22, 33, 44, 55, 66, 77]
# input_p = [list(a) for a in zip(range(0, len(li)), li)]

# print(input_p)