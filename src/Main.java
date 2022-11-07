import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import exceptions.NotEnoughSpaceException;
import executions.TwoContainerAnimationData;
import executions.TwoContainerPlottingData;
import simulations.HallwaySimulation;
import simulations.TwoContainersSimulation;

public class Main {
    final static int NUMBER_OF_ITERATIONS = 3000;
    final static int NUMBER_OF_EXECUTIONS = 30;

    final static String folder = "results/";

    public static void main(String[] args) throws IOException, NotEnoughSpaceException {
        Map<String, Integer> arguments = readArgs(args);

		try {
            File directory = new File(folder);
            if (! directory.exists()) {
                directory.mkdir();
            }

			int     width = Optional.ofNullable(arguments.get("width")).orElse(200),
                    height = Optional.ofNullable(arguments.get("height")).orElse(200),
                    numberOfParticles = Optional.ofNullable(arguments.get("numberOfParticles")).orElse(75000),
                    numberOfIterations = Optional.ofNullable(arguments.get("numberOfIterations")).orElse(3000),
                    chunkSize = Optional.ofNullable(arguments.get("chunkSize")).orElse(5);
            boolean useHPP = false;
            long startTime = System.currentTimeMillis();

//            TwoContainerAnimationData tca = new TwoContainerAnimationData(
//                    folder + "staticFile",
//                    folder + "dynamicFile",
//                    new TwoContainersSimulation(height, width, numberOfParticles, useHPP),
//                    numberOfIterations,
//                    chunkSize);

//            int tenHPP = 19208 * 4 / 10;
            int tenFHP = 19208 * 6 / 10;
            int stepSize = 5000;
//            for (int i = stepSize; i <= 75000; i+=stepSize) {
//                System.out.println("Starting iteration HPP: " + i);
//                plotData(i, useHPP);
//            }
            useHPP = false;
            for (int i = 80000; i <= 115000; i+=stepSize) {
                System.out.println("Starting iteration FHP: " + i);
                plotData(i, useHPP);
            }
            //plotData(numberOfParticles, useHPP);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("Elapsed ms: " + elapsedTime);
		}catch (IOException | NotEnoughSpaceException e) {
			System.err.println(e);
			return;
		}
    }

    private static Map<String, Integer> readArgs(String[] args){
        Map<String, Integer> result = new HashMap<>();

        for (String arg: args){
            String[] splitValues = arg.split("=");

            if(splitValues.length != 2) continue;

            result.put(splitValues[0], Integer.valueOf(splitValues[1]));
        }

        return result;
    }

    private static void plotData(int particles, boolean useHPP) throws IOException, NotEnoughSpaceException {
        (new File(folder)).mkdir();
        String model = useHPP ? "HPP" : "FHP";

        String particleDistributionName = folder + model + "particleDistributions(" + particles + ")";
        (new File(particleDistributionName)).mkdir();
        (new File(particleDistributionName + "/distribution")).mkdir();
        String folderName = folder + model + "particleDistributions(" + particles + ")/distribution";
        File folder = new File(folderName);
        folder.mkdir();
        new TwoContainerPlottingData(folderName, NUMBER_OF_ITERATIONS, NUMBER_OF_EXECUTIONS, particles, useHPP);
    }
}