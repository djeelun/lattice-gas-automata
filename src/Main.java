import Exceptions.NotEnoughSpaceException;

public class Main {
    public static void main(String[] args){

        try{
            TwoContainersSimulation tcs = new TwoContainersSimulation(200, 200, 2000,
                    2000, "staticFile", "dynamicFile", "particleDistributionFile");
        }catch (Exception e){
            System.err.println(e);
            return;
        }

    }
}
