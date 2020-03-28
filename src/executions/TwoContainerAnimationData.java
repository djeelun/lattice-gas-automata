package executions;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import simulations.TwoContainersSimulation;

public class TwoContainerAnimationData {
	public void TwoContainerAnimationData(String staticFile, String dynamicFile, TwoContainersSimulation simulation, 
			int numberOfIterations, int chunkSize) throws FileNotFoundException{
		int height = simulation.getHeight(), width = simulation.getWidth(), numberOfParticles = simulation.getNumberOfParticles();
		
		
		
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
                    staticFilePW.write((simulation.getWallRegion().getRegion()[i][j] ? 1 : 0) +" ");
                }
                staticFilePW.write('\n');
            }
        }


        try(PrintWriter dynamicFilePW = new PrintWriter(dynamicFile)){

            for(int t=1; t <= numberOfIterations; t++){
                dynamicFilePW.write("# T"+t+'\n');

                simulation.update();

                Point2D.Float[][] chunks = simulation.getChunks();
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
