import Exceptions.NotEnoughSpaceException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        final int NUMBER_OF_ITERATIONS = 3000;
        /*final int NUMBER_OF_EXECUTIONS = 20;

        for(int i =0; i<NUMBER_OF_EXECUTIONS; i++) {
            System.out.println("Running ("+(i+1)+"/"+NUMBER_OF_EXECUTIONS+")");
            try {
                String folder = "particles2000/";
                (new File(folder)).mkdir();
                TwoContainersSimulation tcs = new TwoContainersSimulation(200, 200, 2000,
                        NUMBER_OF_ITERATIONS, folder + "staticFile", folder + "dynamicFile", folder + "particleDistributionFile" + i, 10);
            } catch (IOException | NotEnoughSpaceException e) {
                System.err.println(e);
                return;
            }

            try {
                String folder = "particles3000/";
                (new File(folder)).mkdir();
                TwoContainersSimulation tcs = new TwoContainersSimulation(200, 200, 3000,
                        NUMBER_OF_ITERATIONS, folder + "staticFile", folder + "dynamicFile", folder + "particleDistributionFile" + i, 10);
            } catch (IOException | NotEnoughSpaceException e) {
                System.err.println(e);
                return;
            }

            try {
                String folder = "particles5000/";
                (new File(folder)).mkdir();
                TwoContainersSimulation tcs = new TwoContainersSimulation(200, 200, 5000,
                        NUMBER_OF_ITERATIONS, folder + "staticFile", folder + "dynamicFile", folder + "particleDistributionFile" + i, 10);
            } catch (IOException | NotEnoughSpaceException e) {
                System.err.println(e);
                return;
            }
        }*/
    	
    	try {
    		String folder = "current/";
    		int width = 500, height=100;
    		char[][] particles = new char[height][width];
    		for(int i=0; i<height; i++) {
    			for(int j=0; j<width; j++) {
    				if(i > height*1/4 && i < height*3/4 && j < 8 && j > 5) {
						particles[i][j] = LatticeGasAutomata.A + LatticeGasAutomata.B + LatticeGasAutomata.F;
    				}else {
    					particles[i][j] = 0;
    				}
    			}
    		}
             CurrentSimulator tcs = new CurrentSimulator(height, width, particles,
                     NUMBER_OF_ITERATIONS, folder + "staticFile", folder + "dynamicFile", 10);
    	}catch (IOException | NotEnoughSpaceException e) {
            System.err.println(e);
            return;
        }

    }
}