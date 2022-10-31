import matplotlib.pyplot as plt
import numpy as np
from datetime import datetime

NUMBER_OF_FILES = 20

def readFromFiles(folder):
    leftContainer = []
    rightContainer = []

    for i in range(NUMBER_OF_FILES):
        file = open(folder + "/particleDistributions(10000)/distribution"+str(i+1), "r")

        lines = file.readlines()

        newLeftContainer = []
        newRightContainer = []
        for lineNumber in range(len(lines)):
            if lines[lineNumber][0] != "#":
                values = lines[lineNumber].split(" ")
                values[1] = values[1].split('\n')[0]
                newLeftContainer.append(int(values[0]))
                newRightContainer.append(int(values[1]))


        leftContainer.append(newLeftContainer)
        rightContainer.append(newRightContainer)

    return leftContainer, rightContainer



def changeArrayOfArraysOrder(array):
    arrays = []

    for j in range(len(array[0])):
        newArray = []
        for i in range(NUMBER_OF_FILES):
            newArray.append(int(array[i][j]))
        arrays.append(newArray)

    return arrays


def calculateMeanAndStd(values):
    means = []
    stds = []
    for i in range(len(values)):
        means.append(np.mean(values[i]))
        stds.append(np.std(values[i]))

    return means, stds



def plotDataset(folder):
    plt.figure()

    tmpLeftContainer, tmpRightContainer = readFromFiles(folder)

    leftContainerValues = calculateMeanAndStd(changeArrayOfArraysOrder(tmpLeftContainer))

    rightContainerValues = calculateMeanAndStd(changeArrayOfArraysOrder(tmpRightContainer))


    ERROR_BAR_STEP = 15
    plt.errorbar(np.arange(0, len(leftContainerValues[0]), ERROR_BAR_STEP),
                 [v for i, v in enumerate(leftContainerValues[0]) if i % ERROR_BAR_STEP == 0],
                 yerr=[v for i, v in enumerate(leftContainerValues[1]) if i % ERROR_BAR_STEP == 0],
                 elinewidth=0.5, label="Left container")
    plt.errorbar(np.arange(0, len(rightContainerValues[0]), ERROR_BAR_STEP),
                 [v for i, v in enumerate(rightContainerValues[0]) if i % ERROR_BAR_STEP == 0],
                 yerr=[v for i, v in enumerate(rightContainerValues[1]) if i % ERROR_BAR_STEP == 0],
                 elinewidth=0.5, label="Right container")

    plt.xlabel('Time (iterations)')
    plt.ylabel('Number of particles')
    plt.title('HPP: Particle Distribution over Time')
    plt.legend()
    plt.savefig("figures/figure" + datetime.utcnow().strftime("%m%d%Y %H%M%S"))

def readEquilibrium(folder):
    ratios = [];

    for i in range(NUMBER_OF_FILES):
        file = open("../../" + folder + "/equilibrium" + str(i+1), "r")

        lines = file.readlines()

        newRatios = [];
        for lineNumber in range(len(lines)):
            if(lines[lineNumber][0] != "#"):
                values = lines[lineNumber].split(" ")
                values[0] = values[0].split('\n')[0]
                newRatios.append(values[0]);

        ratios.append(newRatios)
    return ratios


def plotEquilibrium(folder):
    plt.figure()

    ratios = calculateMeanAndStd(changeArrayOfArraysOrder(readEquilibrium(folder)))
    ERROR_BAR_STEP = 5000
    print(np.arange(2000, len(ratios[0]) * ERROR_BAR_STEP, ERROR_BAR_STEP))

    plt.errorbar(np.arange(2000, len(ratios[0]) * ERROR_BAR_STEP, ERROR_BAR_STEP),
                 ratios[0],
                 yerr=ratios[1],
                 elinewidth=0.8)


    plt.xlabel('Numero de particulas')
    plt.ylabel('Numero de iteraciones')

def runPlotter():
    plotEquilibrium('results')
    plt.show()