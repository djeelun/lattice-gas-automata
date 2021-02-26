import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import exceptions.NotEnoughSpaceException;
import executions.TwoContainerAnimationData;
import executions.TwoContainerPlottingData;
import simulations.TwoContainersSimulation;

public class Main {
    final static int NUMBER_OF_ITERATIONS = 3000;
    final static int NUMBER_OF_EXECUTIONS = 20;

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
                    numberOfParticles = Optional.ofNullable(arguments.get("numberOfParticles")).orElse(5000),
                    numberOfIterations = Optional.ofNullable(arguments.get("numberOfIterations")).orElse(3000),
                    chunkSize = Optional.ofNullable(arguments.get("chunkSize")).orElse(5);

            TwoContainerAnimationData tca = new TwoContainerAnimationData(
                    folder + "staticFile",
                    folder + "dynamicFile",
                    new TwoContainersSimulation(height, width, numberOfParticles),
                    numberOfIterations,
                    chunkSize);

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