from __future__ import division
import pygame
from pygame.locals import *
import numpy as np
import random
import math
import time
import pygame.gfxdraw
from matplotlib import pyplot as plt
from matplotlib import colors
from matplotlib import cm as cmx

import os
x = 100
y = 0
os.environ['SDL_VIDEO_WINDOW_POS'] = "%d,%d" % (x,y)


class GasDrawer:
    DISPLAY = 0
    xResolution = 1000
    yResolution = 800
    BACKGROUND = (108, 181, 245)
    BLACK = (40, 40, 40)
    boxSize = 0
    map = None
    chunkSize = 0
    SCALE_DISPLAY_SIZE = 150
    title = None
    num_particles = 0
    frameNr = 0

    def __init__(self, map, chunkSize, model, num_particles):
        pygame.init()
        pygame.display.set_caption('Simulation display')
        self.chunkSize = chunkSize
        self.map = map
        self.calculateSizes()
        self.SCALE_DISPLAY_SIZE = self.xResolution - ((len(map[0]) + 2) * self.boxSize)
        self.title = "Model: " + model
        self.num_particles = "n=" + str(num_particles)

    def calculateSizes(self):
        width = self.xResolution
        height = self.yResolution
        self.boxSize = int(min(width / len(self.map[0]), height / len(self.map)))
        self.DISPLAY = pygame.display.set_mode((self.xResolution, self.yResolution), 0, 32)
        self.DISPLAY.fill(self.BACKGROUND)

    def drawScale(self):
        array = np.arange(0.0, 1.0, 0.02)
        w, h = pygame.display.get_surface().get_size()
        x = (w - self.SCALE_DISPLAY_SIZE + 30) / self.boxSize
        y = ((h / self.boxSize) / 5 * 3) - len(array)

        boxScale = 2

        self.writeText(x + 3, y - 2, "Fast", 20, self.getColor(1.0))

        for scale in array:
            color = self.getColor(1 - scale)
            self.drawSquare(x, y, color, boxScale)
            y += boxScale

        self.writeText(x + 3, y - 5, "Slow", 20, self.getColor(0.0))

    def writeText(self, x, y, text, size, color):
        font = pygame.font.SysFont("Arial", size)
        label = font.render(text, 1, color)
        self.DISPLAY.blit(label, (x * self.boxSize, y * self.boxSize))

    def drawWalls(self):
        self.DISPLAY.fill(self.BACKGROUND)
        w, h = pygame.display.get_surface().get_size()
        self.writeText((w - self.SCALE_DISPLAY_SIZE + 30) / self.boxSize, 15, self.title, 25, self.BLACK)
        self.writeText((w - self.SCALE_DISPLAY_SIZE + 30) / self.boxSize, 30, self.num_particles, 25, self.BLACK)
        self.drawScale()
        for y in range(len(self.map)):
            for x in range(len(self.map[y])):
                if self.map[y][x] == 1:
                    self.drawSquare(x, y, self.BLACK)

    def drawSquare(self, x, y, color, boxAmount=1):
        pygame.draw.rect(self.DISPLAY, color,
                         (x * self.boxSize, y * self.boxSize, self.boxSize * boxAmount, self.boxSize * boxAmount))

    def clearChunk(self, x, y):
        pygame.draw.rect(self.DISPLAY, self.BACKGROUND, (
        x * self.chunkSize * self.boxSize, y * self.chunkSize * self.boxSize, self.chunkSize * self.boxSize,
        self.chunkSize * self.boxSize))

    def drawChunkArrows(self, velocities, maxVel):
        if (len(velocities) > 0):
            self.drawWalls()
            w, _ = pygame.display.get_surface().get_size()
            self.writeText((w - self.SCALE_DISPLAY_SIZE + 30) / self.boxSize, 45, "t=" + str(self.frameNr), 25, self.BLACK)
            for y in range(len(velocities)):
                for x in range(len(velocities[y])):
                    velArray = velocities[y][x]
                    Vx = velArray[0]
                    Vy = velArray[1]
                    if (Vx != 0 or Vy != 0):
                        self.drawArrow(x, y, math.atan2(Vy, Vx) * 180 / math.pi,
                                       (((Vx ** 2) + (Vy ** 2)) / maxVel))

    def cleanEmptySquares(self):
        for y in range(len(self.map)):
            for x in range(len(self.map[y])):
                if self.map[y][x] == -1:
                    self.drawSquare(x, y, self.BLUE)

    def getColor(self, scale):
        colorAux = plt.cm.YlOrRd(scale)
        return [colorAux[0] * 255, colorAux[1] * 255, colorAux[2] * 255]

    def drawArrow(self, x, y, rotation=0, scale=1):
        newBox = self.boxSize * self.chunkSize
        lineBold = 0.001 * newBox / 2
        lineLength = 0.8 * newBox / 2# * scale # removed *0.8 and added scale
        arrowSize = (0.15 * newBox / 2)
        xCord = x * newBox + (newBox / 2)
        yCord = y * newBox + (newBox / 2)
        rotationRadians = math.pi * rotation / 180
        sin = math.sin(rotationRadians)
        cos = math.cos(rotationRadians)
        color = self.getColor(scale)
        # color = [255, 255, 255]
        pygame.gfxdraw.aapolygon(self.DISPLAY, ((xCord - (lineBold * sin), yCord - (lineBold * cos)),
                                                (xCord + (lineBold * sin), yCord + (lineBold * cos)),
                                                (xCord + (lineLength * cos) + (lineBold * sin),
                                                 yCord - (lineLength * sin) + (lineBold * cos)),
                                                (xCord + (lineLength * cos) + (arrowSize * sin),
                                                 yCord - (lineLength * sin) + (arrowSize * cos)),
                                                (xCord + ((lineLength + arrowSize) * cos),
                                                 yCord - ((lineLength + arrowSize) * sin)),
                                                (xCord + (lineLength * cos) - (arrowSize * sin),
                                                 yCord - (lineLength * sin) - (arrowSize * cos)),
                                                (xCord + (lineLength * cos) - (lineBold * sin),
                                                 yCord - (lineLength * sin) - (lineBold * cos))), color)
        pygame.gfxdraw.filled_polygon(self.DISPLAY, ((xCord - (lineBold * sin), yCord - (lineBold * cos)),
                                                     (xCord + (lineBold * sin), yCord + (lineBold * cos)),
                                                     (xCord + (lineLength * cos) + (lineBold * sin),
                                                      yCord - (lineLength * sin) + (lineBold * cos)),
                                                     (xCord + (lineLength * cos) + (arrowSize * sin),
                                                      yCord - (lineLength * sin) + (arrowSize * cos)),
                                                     (xCord + ((lineLength + arrowSize) * cos),
                                                      yCord - ((lineLength + arrowSize) * sin)),
                                                     (xCord + (lineLength * cos) - (arrowSize * sin),
                                                      yCord - (lineLength * sin) - (arrowSize * cos)),
                                                     (xCord + (lineLength * cos) - (lineBold * sin),
                                                      yCord - (lineLength * sin) - (lineBold * cos))), color)

    def update(self, velocities, maxVel, frameNr):
        for event in pygame.event.get():
            if event.type == QUIT:
                pygame.quit()
                raise Exception("Window closed")
            elif event.type == KEYDOWN:
               if event.key == K_ESCAPE:
                   pygame.quit()
                   raise Exception("Window closed")

        self.frameNr = frameNr
        self.drawChunkArrows(velocities, maxVel)
        pygame.display.update()

    def firstUpdate(self):
        for event in pygame.event.get():
            if event.type == QUIT:
                raise Exception("Window closed")
            elif event.type == KEYDOWN:
               if event.key == K_ESCAPE:
                   pygame.quit()
                   raise Exception("Window closed")
        self.drawWalls()
        pygame.display.update()
