import pygame
from pygame.locals import *
import random
import GasDrawer
import time
import FilesReader

paths = ["../../particles2000", "../../particles3000", "../../particles5000"]

for path in paths:
    reader = FilesReader.FilesReader(path)

    chunkSize, map = reader.readStatic()

    drawer = GasDrawer.GasDrawer(map, chunkSize, "N = " + filter(str.isdigit, path))

    velocities, maxVel = reader.readNextTime()

    while len(velocities) > 0:
        drawer.update(velocities, maxVel)
        velocities, maxVel = reader.readNextTime()
