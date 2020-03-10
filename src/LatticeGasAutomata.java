public class LatticeGasAutomata {

    /*
        Hex map is represented in the following way:

           1 2 3       1 2 3
            4 5 6  ->  4 5 6
           7 8 9       7 8 9

     */
    private char[][] lattice;

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
                +  invertVelocity((char)(value & D)) +  invertVelocity((char)(value & E)) +  invertVelocity((char)(value & F)));

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

    public LatticeGasAutomata(int width, int height){
        this.width = width;
        this.height = height;
        
        this.lattice = new char[height][width];
        
        fillWithParticles();
    }
    
    private void fillWithParticles(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                char value = j==0 || i==0 ? S : 0;
                
                value += (char)Math.round(Math.random())*R;
                
                if(Math.round(Math.random()) == 1){
                    value += (char)Math.pow(2, Math.round(Math.random()*6));
                }
                
                lattice[i][j] = value;
            }
        }
    }

    public void update(){
        hop();
        collide();
    }

    private void hop(){
        char[][] newLattice = new char[height][width];

        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {

                //A
                if(j+1<width){
                    newLattice[i][j+1] += lattice[i][j] & (A | R);
                }

                //B
                if(i-1>0){
                    newLattice[i-1][j] += lattice[i][j] & (B | R);
                }

                //C
                if(i-1>0 && j-1>0){
                    newLattice[i-1][j-1] += lattice[i][j] & (C | R);
                }

                //D
                if(j-1>0){
                    newLattice[i][j-1] += lattice[i][j] & (D | R);
                }

                //D
                if(i+1<height && j-1>0){
                    newLattice[i+1][j-1] += lattice[i][j] & (D | R);
                }

                //E
                if(i+1<height){
                    newLattice[i+1][j] += lattice[i][j] & (E | R);
                }

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
}
