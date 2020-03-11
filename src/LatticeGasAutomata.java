public class LatticeGasAutomata {

    /*
        Hex map is represented in the following way:

           1 2 3       1 2 3
            4 5 6  ->  4 5 6
           7 8 9       7 8 9

     */
    private char[][] lattice;
    private boolean[][] spawnRegion, wallRegion;

    private int height;
    private int width;

    public static final char A = 0b00000001;
    public static final char B = 0b00000010;
    public static final char C = 0b00000100;
    public static final char D = 0b00001000;
    public static final char E = 0b00010000;
    public static final char F = 0b00100000;
    public static final char S = 0b01000000;
    public static final char R = 0b10000000;

    private char invertVelocity(char v){
        switch (v){
            case A:
                return D;
            case B:
                return E;
            case C:
                return F;
            case D:
                return A;
            case E:
                return B;
            case F:
                return C;
        }
        return 0;
    }

    private char solveCollision(char value){
        //checks if it is solid
        if((value & S) != 0){

            value = (char)(invertVelocity((char)(value & A)) + invertVelocity((char)(value & B)) + invertVelocity((char)(value & C))
                +  invertVelocity((char)(value & D)) +  invertVelocity((char)(value & E)) +  invertVelocity((char)(value & F)) + S);

        }else{
            switch (value){
                //two particle collision
                case A+D:
                    value = B + E;
                    break;
                case B+E:
                    value = C + F;
                    break;
                case C+F:
                    value = A + D;
                    break;
                case A+D+R:
                    value = C + F;
                    break;
                case B+E+R:
                    value = A + D;
                    break;
                case C+F+R:
                    value = B + E;
                    break;

                //3 particle collision
                case A+C+E:
                    value = B + D + F;
                    break;
                case B + D + F:
                    value = A + C + E;
                    break;
                case A + C + E + R:
                    value = B + D + F + R;
                    break;
                case B + D + F + R:
                    value = A + C + E + R;
                    break;
            }
        }


        return value;
    }


    public LatticeGasAutomata(int width, int height, boolean[][] spawnRegion, boolean[][] wallRegion){
        this.width = width;
        this.height = height;
        this.spawnRegion = spawnRegion;
        this.wallRegion = wallRegion;
        
        this.lattice = new char[height][width];

        createWalls();
        fillWithParticles();
        createRandomBit();
    }

    private void createWalls(){
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                this.lattice[i][j] = this.wallRegion[i][j] ? S : 0;
            }
        }
    }

    private void fillWithParticles(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){

                char value = 0;

                if(this.spawnRegion[i][j] && !this.wallRegion[i][j]) {

                    if (Math.round(Math.random()) == 1) {
                        value += (char) Math.pow(2, Math.round(Math.random() * 6));
                    }
                }


                lattice[i][j] = value;
            }
        }
    }

    private void createRandomBit(){
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                this.lattice[i][j] += (char) Math.round(Math.random()) * R;
            }
        }
    }

    public void update(){
        hop();
        collide();
    }

    //TODO: hay un bug aca. Sera que estoy manejando mal el mapeo HEX -> MATRIZ ?
    private void hop(){
        char[][] newLattice = new char[height][width];

        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {

                //horizontal movement does not depend on parity of row
                //A
                if(j+1<width){
                    newLattice[i][j+1] += lattice[i][j] & (A | R);
                }
                //D
                if(j-1>0){
                    newLattice[i][j-1] += lattice[i][j] & (D | R);
                }

                //vertical movement does depend on parity of row
                if(i % 2 == 0){
                    //C
                    if(i-1>0){
                        newLattice[i-1][j] += lattice[i][j] & (C | R);
                    }

                    //B
                    if(i-1>0 && j+1<width){
                        newLattice[i-1][j+1] += lattice[i][j] & (B | R);
                    }


                    //F
                    if(i+1<height && j+1<width){
                        newLattice[i+1][j+1] += lattice[i][j] & (F | R);
                    }

                    //E
                    if(i+1<height){
                        newLattice[i+1][j] += lattice[i][j] & (E | R);
                    }
                }else{
                    //B
                    if(i-1>0){
                        newLattice[i-1][j] += lattice[i][j] & (B | R);
                    }

                    //C
                    if(i-1>0 && j-1>0){
                        newLattice[i-1][j-1] += lattice[i][j] & (C | R);
                    }


                    //E
                    if(i+1<height && j-1>0){
                        newLattice[i+1][j-1] += lattice[i][j] & (E | R);
                    }

                    //F
                    if(i+1<height){
                        newLattice[i+1][j] += lattice[i][j] & (F | R);
                    }
                }


                //S
                newLattice[i][j] += lattice[i][j] & S;

            }
        }

        this.lattice = newLattice;
    }

    private void collide(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                lattice[i][j] = solveCollision(lattice[i][j]);
            }
        }
    }

    public char[][] getLattice() {
        return lattice;
    }

    public int countParticles(){
        int count = 0;
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                count += countCellParticles(lattice[i][j]);
            }
        }
        return count;
    }

    private int countCellParticles(char cell){
        int value = 0;

        value += (cell & A) != 0 ? 1 : 0;
        value += (cell & B) != 0 ? 1 : 0;
        value += (cell & C) != 0 ? 1 : 0;
        value += (cell & D) != 0 ? 1 : 0;
        value += (cell & E) != 0 ? 1 : 0;
        value += (cell & F) != 0 ? 1 : 0;


        return value;
    }
}
