import matplotlib.pyplot as plt
import numpy as np
from datetime import datetime
from similaritymeasures import frechet_dist
import math
from scipy.signal import argrelextrema

NUMBER_OF_FILES = 1

def readFromFiles(folder, numParticles=10000):
    leftContainer = []
    rightContainer = []

    for i in range(NUMBER_OF_FILES):
        file = open(folder + "/distribution"+str(i+1), "r") # + "/particleDistributions(" + str(numParticles) + ")

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

def plotDifference(folder, numParticles=10000, model="FHP"):
    plt.figure()

    tmpLeftContainer, tmpRightContainer = readFromFiles(folder, numParticles)

    leftContainerValues = calculateMeanAndStd(changeArrayOfArraysOrder(tmpLeftContainer))

    rightContainerValues = calculateMeanAndStd(changeArrayOfArraysOrder(tmpRightContainer))

    num_particles_k = "(" + str(numParticles)[0:-3] + "k)"

    # now = datetime.utcnow().strftime("%m%d%Y %H%M%S")
    # path = "figures/" + now + "/"
    # isExist = os.path.exists(path)

    # if not isExist:
    #     os.makedirs(path)

    ERROR_BAR_STEP = 15
    v1 = [v for i, v in enumerate(leftContainerValues[0]) if i % ERROR_BAR_STEP == 0]
    v2 = [v for i, v in enumerate(rightContainerValues[0]) if i % ERROR_BAR_STEP == 0]

    vArr = [math.fabs(a_i - b_i) for a_i, b_i in zip(v1, v2)]

    plt.ylim(0, 5000)
    plt.xlim(2000, 10000)
    plt.plot(np.arange(0, len(leftContainerValues[0]), ERROR_BAR_STEP),
            vArr)

    plt.xlabel('Time (iterations)')
    plt.ylabel('Number of particles')
    plt.title(model + ': Particle Difference over Time ' + num_particles_k)
    plt.margins(x=0, y=0)
    plt.savefig(folder + "/difference", dpi=1000)
    plt.close()

def plotDataset(folder, numParticles=10000, model="FHP", return_plot=False):
    plt.figure()

    tmpLeftContainer, tmpRightContainer = readFromFiles(folder, numParticles)

    leftContainerValues = calculateMeanAndStd(changeArrayOfArraysOrder(tmpLeftContainer))

    rightContainerValues = calculateMeanAndStd(changeArrayOfArraysOrder(tmpRightContainer))

    num_particles_k = "(" + str(numParticles)[0:-3] + "k)"

    # now = datetime.utcnow().strftime("%m%d%Y %H%M%S")
    # path = "figures/" + now + "/"
    # isExist = os.path.exists(path)

    # if not isExist:
    #     os.makedirs(path)

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
    plt.title(model + ': Particle Distribution over Time ' + num_particles_k)
    plt.legend()

    if return_plot:
        return plt

    plt.savefig(folder + "/figure dist", dpi=1000)

    plt.figure()
    plt.plot(np.arange(0, len(leftContainerValues[0]), ERROR_BAR_STEP), 
             [v for i, v in enumerate(leftContainerValues[1]) if i % ERROR_BAR_STEP == 0])
    plt.xlabel('Time (iterations)')
    plt.ylabel('Std. between different iterations')
    plt.title(model + ': Std. Particle Distribution over Time ' + num_particles_k)
    plt.savefig(folder + "/figure std", dpi=1000)
    plt.close()

def readEquilibrium(folder):
    ratios = [];

    for i in range(NUMBER_OF_FILES):
        file = open(folder + "/equilibrium" + str(i+1), "r")

        lines = file.readlines()

        newRatios = [];
        for lineNumber in range(len(lines)):
            if(lines[lineNumber][0] != "#"):
                values = lines[lineNumber].split(" ")
                values[0] = values[0].split('\n')[0]
                newRatios.append(values[0]);

        ratios.append(newRatios)
    return ratios

def plotFrechetDist(fhp_path_list, hpp_path_list, num_particles_list, output_path):
    plt.figure()

    frdists = []
    # For every corresponding particle number model thing, calculate the frechet distance
    for i in range(0, len(num_particles_list)):
        num_particles = num_particles_list[i]
        fhp_path = fhp_path_list + "(" + str(num_particles) + ")"
        hpp_path = hpp_path_list + "(" + str(num_particles) + ")"

        # We only need either the left or the right container
        fhpLeftContainer, _ = readFromFiles(fhp_path, num_particles)
        fhpContainerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(fhpLeftContainer))

        hppLeftContainer, _ = readFromFiles(hpp_path, num_particles)
        hppContainerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(hppLeftContainer))

        ERROR_BAR_STEP = 15
        input_p = [list(a) for a in zip( np.arange(0, len(fhpContainerValues), ERROR_BAR_STEP) , 
                                        [v for i, v in enumerate(fhpContainerValues) if i % ERROR_BAR_STEP == 0])]
        input_q = [list(a) for a in zip( np.arange(0, len(hppContainerValues), ERROR_BAR_STEP) , 
                                        [v for i, v in enumerate(hppContainerValues) if i % ERROR_BAR_STEP == 0])]

        dissimilarity = frechet_dist(input_p, input_q)
        print(dissimilarity)
        frdists.append(dissimilarity)

    meanFD = np.mean(frdists)
    stdFD = np.std(frdists)

    # plt.bar(num_particles_list, frdists, width=4000)
    plt.plot(num_particles_list, frdists, marker='o')
    plt.xlabel("Number of particles")
    plt.ylabel("Fréchet distance")
    plt.title("Fréchet distance for different particle densities (m=" + "{:.1f}".format(meanFD) + ", s=" + "{:.1f}".format(stdFD) + ")")
    plt.savefig(output_path + "/frechetPlotPlot")
    plt.close()

def plotEquilibrium(folder, num_particles, model):
    plt.figure()

    # We only need either the left or the right container
    _, rightContainer = readFromFiles(folder, num_particles)
    containerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(rightContainer))
    smCVs = [v for i, v in enumerate(containerValues) if i % 15 == 0]

    # (equilibria,) = argrelextrema(np.convolve(smCVs, 200, 'flat'), np.greater)
    # print(equilibria)

    equilibrium = findEquilibrium(containerValues, num_particles)
    
    myPlot = plotDataset(folder, num_particles, model, True)
    myPlot.axvline(x=equilibrium, color='g')
    myPlot.annotate(str(equilibrium), xy=(equilibrium, 0), xytext=(equilibrium - 30, -(num_particles / 10)), arrowprops=dict(facecolor='black', arrowstyle='-'))
    myPlot.margins(x=0, y=0)
    myPlot.savefig(folder + "/equilibrium")
    myPlot.close()

    # ERROR_BAR_STEP = 15
    # ar = np.arange(0, len(containerValues), ERROR_BAR_STEP)
    # reducedCV = [v for i, v in enumerate(containerValues) if i % ERROR_BAR_STEP == 0]

    
    # print(np.arange(2000, len(ratios[0]) * ERROR_BAR_STEP, ERROR_BAR_STEP))

    # plt.errorbar(np.arange(2000, len(ratios[0]) * ERROR_BAR_STEP, ERROR_BAR_STEP),
    #              ratios[0],
    #              yerr=ratios[1],
    #              elinewidth=0.8)


    # plt.xlabel('Numero de particulas')
    # plt.ylabel('Numero de iteraciones')
    # plt.savefig(folder + "/equilibrium")

def plotInterval(folder, num_particles, model):
    plt.figure()

    # We only need either the left or the right container
    _, rightContainer = readFromFiles(folder, num_particles)
    containerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(rightContainer))

    intervals = findIntervals(containerValues)
    
    myPlot = plotDataset(folder, num_particles, model, True)
    for inter in intervals:
        myPlot.axvline(x=inter, color='g')
    myPlot.savefig(folder + "/interval")
    myPlot.close()


def plotAllEquilibria(fhp_path_list, hpp_path_list, num_particles_list, output_path):
    fhpEquilibria = []
    # hppEquilibria = []
    for i in range(0, len(num_particles_list)):
        num_particles = num_particles_list[i]
        fhp_path = fhp_path_list + "(" + str(num_particles) + ")"
        # hpp_path = hpp_path_list + "(" + str(num_particles) + ")"

        # We only need either the left or the right container
        fhpLeftContainer, fhpRightContainer = readFromFiles(fhp_path, num_particles)
        fhpContainerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(fhpRightContainer))
        fhpEquilibria.append(findEquilibrium(fhpContainerValues, num_particles))

        # hppLeftContainer, _ = readFromFiles(hpp_path, num_particles)
        # hppContainerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(hppLeftContainer))
        # hppEquilibria.append(findEquilibrium(hppContainerValues, num_particles))

    plt.figure()

    plt.plot(num_particles_list, fhpEquilibria, label="FHP")
    # plt.plot(num_particles_list, hppEquilibria, label="HPP")
    plt.xlabel("Number of particles")
    plt.ylabel("T_0")
    plt.title("T_0 for different particle densities")
    # plt.legend()
    plt.savefig(output_path + "/allEquilibria")
    plt.close() # beware

    # plt.figure()
    # vArr = [math.fabs(a_i - b_i) for a_i, b_i in zip(fhpEquilibria, hppEquilibria)]

    # plt.plot(num_particles_list, vArr)
    # plt.xlabel("Number of particles")
    # plt.ylabel("Difference in time of equilibrium")
    # plt.title("Difference in time of between HPP and FHP")
    # plt.savefig(output_path + "/allEquilibriaDiff")
    # plt.close()
        

def plotAllIntervals(fhp_path_list, hpp_path_list, num_particles_list, output_path):
    fhpEquilibria = []
    hppEquilibria = []
    for i in range(0, len(num_particles_list)):
        num_particles = num_particles_list[i]
        fhp_path = fhp_path_list + "(" + str(num_particles) + ")"
        hpp_path = hpp_path_list + "(" + str(num_particles) + ")"

        # We only need either the left or the right container
        fhpLeftContainer, _ = readFromFiles(fhp_path, num_particles)
        fhpContainerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(fhpLeftContainer))
        fhpEquilibria.append(findInterval(fhpContainerValues))

        hppLeftContainer, _ = readFromFiles(hpp_path, num_particles)
        hppContainerValues, _ = calculateMeanAndStd(changeArrayOfArraysOrder(hppLeftContainer))
        hppEquilibria.append(findInterval(hppContainerValues))

    plt.figure()

    plt.plot(num_particles_list, fhpEquilibria, label="FHP")
    plt.plot(num_particles_list, hppEquilibria, label="HPP")
    plt.xlabel("Number of particles")
    plt.ylabel("Mean wavelength (equilibrium)")
    plt.title("Mean wavelength of oscillating particle distribution")
    plt.legend()
    plt.savefig(output_path + "/allIntervals")
    plt.close()

def findEquilibrium(containerValues, num_particles):
    up = 1
    maxima = []
    smoothing = 20
    prevVal = np.mean(containerValues[0:smoothing*2])
    f_value = 10000
    equilibrium = -1
    minOff = 0
    for i in range(smoothing, len(containerValues) - smoothing):
        if i < minOff:
            continue
        currVal = np.mean(containerValues[(i-smoothing):(i+smoothing)])
        if up * (currVal - prevVal) < 0:
            maxima.append(i)
            if math.fabs(0.5 - (containerValues[i] / num_particles)) < f_value:
                print(containerValues[i], i, (containerValues[i] / num_particles))
                equilibrium = i
                break
            up = -1 * up
            minOff = i + 100
        prevVal = currVal
    

    if equilibrium < 0 and len(maxima) > 0:
        equilibrium = maxima[0]

    return equilibrium

def findInterval(containerValues):
    up = 1
    maxima = []
    smoothing = 300
    minOff = 0
    prevVal = np.mean(containerValues[0:smoothing*2])
    for i in range(smoothing, len(containerValues) - smoothing):
        if i < minOff:
            continue
        currVal = np.mean(containerValues[(i-smoothing):(i+smoothing)])
        if up * (currVal - prevVal) < 0:
            maxima.append(i)
            up = -1 * up
            minOff = i + 100
        prevVal = currVal
    

    interval = (maxima[-1] - maxima[0]) / len(maxima)
    return interval

def findIntervals(containerValues):
    up = 1
    maxima = []
    smoothing = 300
    minOff = 0
    prevVal = np.mean(containerValues[0:smoothing*2])
    for i in range(smoothing, len(containerValues) - smoothing):
        if i < minOff:
            continue
        currVal = np.mean(containerValues[(i-smoothing):(i+smoothing)])
        if up * (currVal - prevVal) < 0:
            maxima.append(i)
            up = -1 * up
            minOff = i + 100
        prevVal = currVal
    
    print(len(maxima))
    return maxima

def findTops(containerValues):
    up = 1
    maxima = []
    smoothing = 300
    minOff = 0
    prevVal = np.mean(containerValues[0:smoothing*2])
    for i in range(smoothing, len(containerValues) - smoothing):
        if i < minOff:
            continue
        currVal = np.mean(containerValues[(i-smoothing):(i+smoothing)])
        if up * (currVal - prevVal) < 0:
            maxima.append(containerValues[i])
            up = -1 * up
            minOff = i + 100
        prevVal = currVal
    
    print(len(maxima))
    return maxima

def runPlotter():
    plotEquilibrium('results')
    plt.show()