package executions;
import java.io.IOException;
import java.io.PrintWriter;

import exceptions.NotEnoughSpaceException;
import simulations.TwoContainersSimulation;

public class FindEquilibriumTime {

	final static int MAX_ITERATIONS = 5000;
	
	public FindEquilibriumTime(int minN, int maxN, int step, int numberOfExecutions, int numberOfIterations, String file, float threshHoldRatio) throws IOException, NotEnoughSpaceException{
		for(int e=0; e<numberOfExecutions; e++) {

			try(PrintWriter resultFile = new PrintWriter(file+(e+1))){
					for(int N = minN; N<maxN; N+=step) {
						System.out.println(N+"/"+maxN+" - Step "+step);
						
						TwoContainersSimulation sim = new TwoContainersSimulation(200, 200, N, numberOfIterations);
						
						resultFile.println("# N" + N);
						
						int iterations = 0;
						float ratio = 0;
						while(ratio < threshHoldRatio && iterations < MAX_ITERATIONS) {
							ratio = sim.getRightContainerParticles() / (float)N;
							iterations++;
							sim.update();
						}
						
						resultFile.println(iterations == MAX_ITERATIONS ? "-1" : iterations);
							
					}
				
			}
		}
	}
}
