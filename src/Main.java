import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import exceptions.NotEnoughSpaceException;
import executions.FindEquilibriumTime;
import executions.TwoContainerPlottingData;
import simulations.TwoContainersSimulation;

public class Main {
	final static int NUMBER_OF_ITERATIONS = 3000;
    final static int NUMBER_OF_EXECUTIONS = 20;
    
    final static String folder = "results/";
	
    public static void main(String[] args) throws IOException, NotEnoughSpaceException{
        
    	/*plotData(2000);
    	plotData(3000);
    	plotData(5000);
    	plotData(20000);
    	plotData(50000);
    	plotData(100000);*/
    	
    	FindEquilibriumTime eq = new FindEquilibriumTime(2000, 100000, 5000, NUMBER_OF_EXECUTIONS, NUMBER_OF_ITERATIONS, folder + "equilibrium", 0.55f);
    	

    }


    private static void plotData(int particles) throws IOException, NotEnoughSpaceException{
    	TwoContainersSimulation sim = new TwoContainersSimulation(200, 200, particles, NUMBER_OF_ITERATIONS);
        new TwoContainerPlottingData(folder + "particleDistributions("+particles+")/distribution",
        		sim, NUMBER_OF_ITERATIONS, NUMBER_OF_EXECUTIONS);
    }
}