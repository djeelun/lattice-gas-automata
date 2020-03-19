import pygame
from pygame.locals import *
import random
import GasDrawer
import time
import FilesReader

reader = FilesReader.FilesReader()

chunkSize, map = reader.readStatic()

drawer = GasDrawer.GasDrawer(map, chunkSize)
velocities, maxVel = reader.readNextTime()
drawer.drawChunkArrows(velocities, maxVel)

while True:
    velocities, maxVel = reader.readNextTime()
    drawer.update(velocities, maxVel)
