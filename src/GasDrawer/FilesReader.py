
class FilesReader:

    dynamicFile = None
    staticFile = None

    height = 0
    width = 0
    particles = 0
    iterations = 0
    chunkSize = 0
    map = None

    dynamicLineNumber = 0
    dynamicLines = None

    maxVel = 0

    def __init__(self, path):
        self.dynamicFile = open(path + "/dynamicFile", "r")
        self.staticFile = open(path + "/staticFile", "r")
        self.dynamicLines = self.dynamicFile.readlines()

    def readMapSize(self, line):
        array = line.split()
        self.height = array[0]
        self.width = array[1]


    def readStatic(self):
        lines = self.staticFile.readlines()
        lineNumber = 0
        parameterNumber = 0

        while(len(lines) > lineNumber and parameterNumber < 4):
            line = lines[lineNumber]
            if(len(line) > 0 and line[0] != '#'):

                if parameterNumber == 0:
                    self.readMapSize(line)

                elif parameterNumber == 1:
                    self.particles = int(line)

                elif parameterNumber == 2:
                    self.iterations = int(line)

                elif parameterNumber == 3:
                    self.chunkSize = int(line)

                parameterNumber += 1
            lineNumber += 1

        line = lines[lineNumber]
        while(line[0] == '#'):
            lineNumber += 1
            line = lines[lineNumber]

        self.map = [[int(num) for num in line.split()] for line in lines[lineNumber::]]

        return self.chunkSize, self.map

    def readVelocity(self, line):
        velocity = [0, 0]
        lineNumber = 1
        while(line[lineNumber] != ','):
            lineNumber += 1
        velocity[0] = float(line[1:lineNumber])
        velocity[1] = float(line[lineNumber+1:len(line)-1])
        velNorm = (velocity[0] ** 2) + (velocity[1] ** 2)
        if self.maxVel < velNorm:
            self.maxVel = velNorm
        return velocity


    def readNextTime(self):
        self.maxVel = 0
        lines = self.dynamicLines

        if len(lines) > self.dynamicLineNumber:
            line = lines[self.dynamicLineNumber]

            while line[0] == '#':
                self.dynamicLineNumber += 1
                line = lines[self.dynamicLineNumber]

            endNumber = self.dynamicLineNumber
            while line[0] != '#' and endNumber < len(lines) - 1:
                endNumber += 1
                line = lines[endNumber]
            chunk = [[self.readVelocity(velocity) for velocity in line.split()] for num in line.split() for line in lines[self.dynamicLineNumber:endNumber]]
            self.dynamicLineNumber = endNumber
            return chunk, self.maxVel