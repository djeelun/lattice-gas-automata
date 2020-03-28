package executions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import exceptions.NotEnoughSpaceException;
import simulations.TwoContainersSimulation;

public class TwoContainerPlottingData {
	public TwoContainerPlottingData(String particleDistributionFile,
			int numberOfIterations, int numberOfExecutions, int particles) throws IOException, NotEnoughSpaceException {
		
		for(int e =0; e<numberOfExecutions; e++) {
			System.out.println((e+1)+"/"+numberOfExecutions);
			TwoContainersSimulation simulation = new TwoContainersSimulation(200, 200, particles, numberOfIterations);
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
