package executions;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import simulations.TwoContainersSimulation;

public class TwoContainerPlottingData {
	public TwoContainerPlottingData(String particleDistributionFile, TwoContainersSimulation simulation, 
			int numberOfIterations, int numberOfExecutions) throws FileNotFoundException{
		
		for(int e =0; e<numberOfExecutions; e++) {
			System.out.println((e+1)+"/"+numberOfExecutions);
			try(PrintWriter particleDistributionFilePW = new PrintWriter(particleDistributionFile+(e+1))){

	            for(int t=1; t <= numberOfIterations; t++){
	                particleDistributionFilePW.write("# T"+t+'\n');

	                simulation.update();

	                particleDistributionFilePW.write(simulation.getLeftContainerParticles()
	                        + " " + simulation.getRightContainerParticles() + '\n');

	            }

	        }
		}
		
		 
	}
}
