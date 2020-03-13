import Exceptions.NotEnoughSpaceException;

import java.awt.*;
import java.util.*;
import java.util.List;

public class LatticeGasAutomata {

    /*
        Hex map is represented in the following way:

           1 2 3       1 2 3
            4 5 6  ->  4 5 6
           7 8 9       7 8 9

     */
    private char[][] lattice;
    private boolean[][] spawnRegion, wallRegion;

    private List<Point> spawnPoints = new ArrayList<>();

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
                +  invertVelocity((char)(value & D)) +  invertVelocity((char)(value & E)) +  invertVelocity((char)(value & F)) + S + (value & R));

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

                //4 particle collision
                case C + B + E + F:
                    value = C + D + A + F;
                    break;
                case C + B + E + F + R:
                    value = B + A + D + E;
                    break;
                case C + D + A + F:
                    value = E + B + D + A;
                    break;
                case C + D + A + F + R:
                    value = E + B + C + F;
                    break;
                case D + E + B + A:
                    value = C + F + D + A;
                    break;
                case D + E + B + A + R:
                    value = C + F + B + E;
                    break;
            }
        }


        return value;
    }


    public LatticeGasAutomata(int width, int height, boolean[][] spawnRegion, boolean[][] wallRegion, int particlesAmount) throws NotEnoughSpaceException{
        this.width = width;
        this.height = height;
        this.spawnRegion = spawnRegion;
        this.wallRegion = wallRegion;
        
        this.lattice = new char[height][width];

        calculateSpawnPoints();
        if(spawnPoints.size() * 6 < particlesAmount)
            throw new NotEnoughSpaceException("There is no space to generate the particles");
        fillWithParticles(particlesAmount);
        createWalls();
        createRandomBit();
    }

    private void createWalls(){
        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {
                this.lattice[i][j] += this.wallRegion[i][j] ? S : 0;
            }
        }
    }

    private void calculateSpawnPoints() {
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(this.spawnRegion[i][j] && !this.wallRegion[i][j]) {
                    spawnPoints.add(new Point(i,j));
                }
            }
        }
    }

    private List<Character> getAvailableSpaces(Point p) {
        List<Character> availableSpaces = new ArrayList<>();
        availableSpaces.add(A);
        availableSpaces.add(B);
        availableSpaces.add(C);
        availableSpaces.add(D);
        availableSpaces.add(E);
        availableSpaces.add(F);

        Iterator<Character> iterator = availableSpaces.iterator();
        while(iterator.hasNext()){
            char value = lattice[p.x][p.y];
            if((value & iterator.next()) != 0){
                iterator.remove();
            }
        }

        return availableSpaces;
    }

    private void addParticle(Point p) {
        List<Character> availableSpaces = getAvailableSpaces(p);

        int index = (int) Math.round(Math.random() * (availableSpaces.size() - 1));
        lattice[p.x][p.y] += availableSpaces.get(index);

        if(availableSpaces.size() <= 1)
            spawnPoints.remove(p);
    }

    private void fillWithParticles(int particlesAmount) {
        while(particlesAmount > 0) {
            int index = (int) Math.round(Math.random() * (spawnPoints.size() - 1));
            addParticle(spawnPoints.get(index));
            particlesAmount--;
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

    private void hop(){
        char[][] newLattice = new char[height][width];

        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {

                //horizontal movement does not depend on parity of row
                //A
                if(j+1<width){
                    newLattice[i][j+1] += lattice[i][j] & A;
                }
                //D
                if(j-1>0){
                    newLattice[i][j-1] += lattice[i][j] & D;
                }

                //vertical movement does depend on parity of row
                if(i % 2 == 0){
                    //C
                    if(i-1>0){
                        newLattice[i-1][j] += lattice[i][j] & C;
                    }

                    //B
                    if(i-1>0 && j+1<width){
                        newLattice[i-1][j+1] += lattice[i][j] & B;
                    }


                    //F
                    if(i+1<height && j+1<width){
                        newLattice[i+1][j+1] += lattice[i][j] & F;
                    }

                    //E
                    if(i+1<height){
                        newLattice[i+1][j] += lattice[i][j] & E;
                    }
                }else{
                    //B
                    if(i-1>0){
                        newLattice[i-1][j] += lattice[i][j] & B;
                    }

                    //C
                    if(i-1>0 && j-1>0){
                        newLattice[i-1][j-1] += lattice[i][j] & C;
                    }


                    //E
                    if(i+1<height && j-1>0){
                        newLattice[i+1][j-1] += lattice[i][j] & E;
                    }

                    //F
                    if(i+1<height){
                        newLattice[i+1][j] += lattice[i][j] & F;
                    }
                }


                //S
                newLattice[i][j] += lattice[i][j] & (S | R);

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
