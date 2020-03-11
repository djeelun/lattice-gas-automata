public class Main {
    public static void main(String[] args){


        int width = 100, height=100;

        Region spawnRegion = new Region(width, height);
        spawnRegion.addRectangle(3, 3, width/2-4, height-4, false);

        Region wallRegion = new Region(width, height);
        //middle wall
        wallRegion.addRectangle(width/2, 0, width/2+1, height-1, false);
        wallRegion.addRectangleBorders(0, 0, width-1, height-1, 2, false);

        LatticeGasAutomata latticeGasAutomata = new LatticeGasAutomata(width, height, spawnRegion.getRegion(), wallRegion.getRegion());

        System.out.println("Spawn region: -------------------");
        spawnRegion.printRegion();

        System.out.println("Wall region: -------------------");
        wallRegion.printRegion();


        System.out.println("Estado inicial: -------------------");
        printLattice(latticeGasAutomata.getLattice(),width,height);

        int lastParticleCount=latticeGasAutomata.countParticles();
        for(int i=1; i <= 100000; i++){
            latticeGasAutomata.update();

            int newParticleCount = latticeGasAutomata.countParticles();

            //System.out.println(newParticleCount);
        }

        System.out.println("Estado final: -------------------");
        printLattice(latticeGasAutomata.getLattice(),width,height);



    }

    private static void printLattice(char[][] lattice, int width, int height){
        int particleCount = 0;
        final String OFFSET = " ";
        for(int i=0; i<height; i++){
            StringBuilder sb = new StringBuilder(i%2 == 0 ? OFFSET : "");
            for(int j=0; j<width; j++){
                int particles = countParticles(lattice[i][j]);
                sb.append(OFFSET).append(particles);
                particleCount += particles;
            }
            System.out.println(sb);
        }
        System.out.println("Number of particles: "+particleCount);
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
