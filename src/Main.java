public class Main {
    public static void main(String[] args){
        LatticeGasAutomata latticeGasAutomata = new LatticeGasAutomata(10, 10);

        printLattice(latticeGasAutomata.getLattice(),10,10);


        for(int i=1; i <= 10; i++){
            latticeGasAutomata.update();

            System.out.println("ITERATION (" + i + ")-----------------------");

            printLattice(latticeGasAutomata.getLattice(),10,10);
        }



    }

    private static void printLattice(char[][] lattice, int width, int height){
        final String OFFSET = " ";
        for(int i=0; i<height; i++){
            StringBuilder sb = new StringBuilder(i%2 == 0 ? OFFSET : "");
            for(int j=0; j<width; j++){
                sb.append(OFFSET).append(countParticles(lattice[i][j]));
            }
            System.out.println(sb);
        }
    }

    private static int countParticles(char particles){
        int value = 0;

        value += (particles & LatticeGasAutomata.A) != 0 ? 1 : 0;
        value += (particles & LatticeGasAutomata.B) != 0 ? 1 : 0;
        value += (particles & LatticeGasAutomata.C) != 0 ? 1 : 0;
        value += (particles & LatticeGasAutomata.D) != 0 ? 1 : 0;
        value += (particles & LatticeGasAutomata.E) != 0 ? 1 : 0;
        value += (particles & LatticeGasAutomata.F) != 0 ? 1 : 0;


        return value;
    }
}
