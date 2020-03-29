import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import exceptions.NotEnoughSpaceException;
import executions.FindEquilibriumTime;
import executions.TwoContainerAnimationData;
import executions.TwoContainerPlottingData;
import simulations.CurrentSimulator;
import simulations.LatticeGasAutomata;
import simulations.TwoContainersSimulation;

public class Main {
    final static int NUMBER_OF_ITERATIONS = 3000;
    final static int NUMBER_OF_EXECUTIONS = 20;

    final static String folder = "results/";

    public static void main(String[] args) throws IOException, NotEnoughSpaceException {

//		try {
//			String folder = "particles2000/";
//			int width = 200, height = 200;
//			char[][] particles = new char[height][width];
//            TwoContainerAnimationData tca = new TwoContainerAnimationData(folder + "staticFile", folder + "dynamicFile", new TwoContainersSimulation(200, 200, 5000,1), 3000, 5);
//		}catch (IOException | NotEnoughSpaceException e) {
//			System.err.println(e);
//			return;
//		}

//    	plotData(2000);
//    	plotData(3000);
//    	plotData(5000);
//    	plotData(20000);
//    	plotData(50000);
//    	plotData(100000);


        new FindEquilibriumTime(7000, 100000, 5000, NUMBER_OF_EXECUTIONS, NUMBER_OF_ITERATIONS, folder + "equilibrium", 0.025f + 0.5f, 1);


    }


    private static void plotData(int particles) throws IOException, NotEnoughSpaceException {
        (new File(folder)).mkdir();
        String particleDistributionName = folder + "particleDistributions(" + particles + ")";
        (new File(particleDistributionName)).mkdir();
        (new File(particleDistributionName + "/distribution")).mkdir();
        String folderName = folder + "particleDistributions(" + particles + ")/distribution";
        File folder = new File(folderName);
        folder.mkdir();
        new TwoContainerPlottingData(folderName, NUMBER_OF_ITERATIONS, NUMBER_OF_EXECUTIONS, particles);
    }
}