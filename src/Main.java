import Exceptions.NotEnoughSpaceException;

import java.io.IOException;

public class Main {
    public static void main(String[] args){

        try{
            TwoContainersSimulation tcs = new TwoContainersSimulation(200, 200, 2000,
                    1000, "staticFile", "dynamicFile", "particleDistributionFile", 10);
        }catch (IOException | NotEnoughSpaceException e){
            System.err.println(e);
            return;
        }

    }
}