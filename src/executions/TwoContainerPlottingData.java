package executions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.IntStream;

import exceptions.NotEnoughSpaceException;
import simulations.TwoContainersSimulation;

public class TwoContainerPlottingData {
	public TwoContainerPlottingData(String particleDistributionFile,
			int numberOfIterations, int numberOfExecutions, int particles, boolean useHPP) throws IOException, NotEnoughSpaceException {
//		for(int e =0; e<numberOfExecutions; e++) {
//			System.out.println((e+1)+"/"+numberOfExecutions);
//			TwoContainersSimulation simulation = new TwoContainersSimulation(200, 200, particles, useHPP);
//			try(PrintWriter particleDistributionFilePW = new PrintWriter(particleDistributionFile+(e+1))){
//
//	            for(int t=1; t <= numberOfIterations; t++){
//	                particleDistributionFilePW.write("# T"+t+'\n');
//
//	                simulation.update();
//
//	                particleDistributionFilePW.write(simulation.getLeftContainerParticles()
//	                        + " " + simulation.getRightContainerParticles() + '\n');
//
//	            }
//
//	        }
//		}

		IntStream intStream = IntStream.range(0, numberOfExecutions);
		intStream.parallel().forEach((e) -> {
			try {
				TwoContainersSimulation simulation = new TwoContainersSimulation(200, 200, particles, useHPP);
				try(PrintWriter particleDistributionFilePW = new PrintWriter(particleDistributionFile+(e+1))){

					for(int t=1; t <= numberOfIterations; t++){
						particleDistributionFilePW.write("# T"+t+'\n');

						simulation.update();

						particleDistributionFilePW.write(simulation.getLeftContainerParticles()
								+ " " + simulation.getRightContainerParticles() + '\n');

					}

				}
				System.out.println((e+1)+"/"+numberOfExecutions);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		 
	}
}
