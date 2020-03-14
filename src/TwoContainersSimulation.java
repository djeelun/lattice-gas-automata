import Exceptions.NotEnoughSpaceException;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;

public class TwoContainersSimulation {
    public TwoContainersSimulation(int height, int width, int numberOfParticles, int numberOfIterations,
                                   String staticFile, String dynamicFile, String particleDistributionFile, int chunkSize) throws IOException, NotEnoughSpaceException {

        Region leftContainer = new Region(width, height);
        leftContainer.addRectangle(0, 0, width/2-1, height-1, false);

        Region rightContainer = new Region(width, height);
        rightContainer.addRectangle(width/2, 0, width-1, height-1, false);

        Region wallRegion = new Region(width, height);
        //middle wall
        wallRegion.addRectangle(width/2, 0, width/2+1, height-1, false);

        //hole in the wall
        wallRegion.addRectangle(width/2, height/2-height/8, width/2 + 1, height/2+height/8, true);

        wallRegion.addRectangleBorders(0, 0, width-1, height-1, 2, false);

        LatticeGasAutomata latticeGasAutomata;

        latticeGasAutomata = new LatticeGasAutomata(width, height, leftContainer.getRegion(), wallRegion.getRegion(), numberOfParticles);

        try(PrintWriter staticFilePW = new PrintWriter(staticFile)){
            staticFilePW.println("# HEIGHT WIDTH");
            staticFilePW.println(height + " " + width);
            staticFilePW.println("# NUMBER OF PARTICLES");
            staticFilePW.println(numberOfParticles);
            staticFilePW.println("# NUMBER OF ITERATIONS");
            staticFilePW.println(numberOfIterations);
            staticFilePW.println("# CHUNK SIZE");
            staticFilePW.println(chunkSize);
            staticFilePW.println("# WALL MATRIX (1 is wall and 0 is empty)");
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    staticFilePW.write((wallRegion.getRegion()[i][j] ? 1 : 0) +" ");
                }
                staticFilePW.write('\n');
            }
        }


        try(PrintWriter dynamicFilePW = new PrintWriter(dynamicFile);
            PrintWriter particleDistributionFilePW = new PrintWriter(particleDistributionFile)){

            for(int t=1; t <= numberOfIterations; t++){
                dynamicFilePW.write("# T"+t+'\n');
                particleDistributionFilePW.write("# T"+t+'\n');

                latticeGasAutomata.update();

                particleDistributionFilePW.write(latticeGasAutomata.countParticlesInsideRegion(leftContainer.getRegion())
                        + " " + latticeGasAutomata.countParticlesInsideRegion(rightContainer.getRegion()) + '\n');

                Point2D.Float[][] chunks = latticeGasAutomata.getMeanVelocities(chunkSize);
                for(int i=0; i<height/chunkSize; i++){
                    for(int j=0; j<width/chunkSize; j++){
                        float x = (float)chunks[i][j].getX(), y = (float)chunks[i][j].getY();
                        dynamicFilePW.write("("+x+","+y+") ");
                    }
                    dynamicFilePW.write('\n');
                }

            }

        }

    }
}
