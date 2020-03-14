import Exceptions.NotEnoughSpaceException;

import java.io.IOException;
import java.io.PrintWriter;

public class TwoContainersSimulation {
    public TwoContainersSimulation(int height, int width, int numberOfParticles, int numberOfIterations,
                                   String staticFile, String dynamicFile, String particleDistributionFile) throws IOException, NotEnoughSpaceException {


        Region spawnRegion = new Region(width, height);
        spawnRegion.addRectangle(3, 3, width/2-4, height-4, false);

        Region wallRegion = new Region(width, height);
        //middle wall
        wallRegion.addRectangle(width/2, 0, width/2+1, height-1, false);

        //hole in the wall
        wallRegion.addRectangle(width/2, height/2-height/8, width/2 + 1, height/2+height/8, true);

        wallRegion.addRectangleBorders(0, 0, width-1, height-1, 2, false);

        LatticeGasAutomata latticeGasAutomata;

        latticeGasAutomata = new LatticeGasAutomata(width, height, spawnRegion.getRegion(), wallRegion.getRegion(), numberOfParticles);



        try(PrintWriter dynamicFilePW = new PrintWriter(dynamicFile);
            PrintWriter particleDistributionFilePW = new PrintWriter(particleDistributionFile)){

            for(int i=1; i <= numberOfIterations; i++){
                latticeGasAutomata.update();
            }

        }

    }
}
