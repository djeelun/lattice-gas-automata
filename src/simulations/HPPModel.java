package simulations;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

import exceptions.NotEnoughSpaceException;

public class HPPModel implements ILatticeGasAutomata {

    /*
        HPP map is represented in the following way

          1
        4 + 2
          3

          A
        D + B
          C
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
    // public static final char E = 0b00010000;
    // public static final char F = 0b00100000;
    public static final char S = 0b01000000;

    private char invertVelocity(char v){
        switch (v){
            case A:
                return C;
            case B:
                return D;
            case C:
                return A;
            case D:
                return B;
        }
        return 0;
    }

    private char solveCollision(char value){
        //checks if it is solid
        if((value & S) != 0){

            value = (char)(invertVelocity((char)(value & A)) + invertVelocity((char)(value & B)) + invertVelocity((char)(value & C))
                    +  invertVelocity((char)(value & D)) + S);

        }else{
            switch (value){
                //two particle collision
                case A+C:
                    value = B + D;
                    break;
                case B+D:
                    value = A + C;
                    break;
            }
        }


        return value;
    }


    public HPPModel(int width, int height, boolean[][] spawnRegion, boolean[][] wallRegion, int particlesAmount) throws NotEnoughSpaceException{
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
    }

    public HPPModel(int width, int height, boolean[][] wallRegion) throws NotEnoughSpaceException{
        this.width = width;
        this.height = height;
        this.wallRegion = wallRegion;

        this.lattice = new char[height][width];


        createWalls();
    }

    public void insertParticles(char[][] particles) {
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                lattice[i][j] |= particles[i][j];
            }
        }
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

    public void update(){
        hop();
        collide();
    }

    private void hop(){
        char[][] newLattice = new char[height][width];

        for(int i=0; i<height; i++) {
            for (int j = 0; j < width; j++) {

                //horizontal movement does not depend on parity of row
                //B
                if(j+1<width){
                    newLattice[i][j+1] += lattice[i][j] & B;
                }
                //D
                if(j-1>0){
                    newLattice[i][j-1] += lattice[i][j] & D;
                }

                //vertical movement does not depend on parity of row
                //A
                if(i-1>0){
                    newLattice[i-1][j] += lattice[i][j] & A;
                }
                //C
                if(i+1<height){
                    newLattice[i+1][j] += lattice[i][j] & C;
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

    public Point2D.Float[][] getMeanVelocities(int chunkSize){
        Point2D.Float[][] result = new Point2D.Float[height/chunkSize][width/chunkSize];
        for(int i=0; i<height/chunkSize; i++){
            for(int j=0; j<width/chunkSize; j++){
                result[i][j] = calculateChunkMeanVelocity(i, j, chunkSize);
            }
        }
        return result;
    }

    private Point2D.Float calculateChunkMeanVelocity(int coorI, int coorJ, int chunkSize){
        int offsetI = chunkSize*coorI, offsetJ = chunkSize*coorJ;

        int numberOfParticles = 0;
        float versorA = 0, versorB = 0;
        for(int i=offsetI; i<offsetI+chunkSize && i<height; i++){
            for(int j=offsetJ; j<offsetJ + chunkSize && j<width; j++){
                int val = lattice[i][j];
                numberOfParticles += countParticles(val);

                versorA += (val & A) > 0 ? 1 : 0;
                versorA -= (val & C) > 0 ? 1 : 0;

                versorB += (val & B) > 0 ? 1 : 0;
                versorB -= (val & D) > 0 ? 1 : 0;
            }
        }

        float x = versorB,
                y = versorA;

        if(numberOfParticles != 0) {
            x /= numberOfParticles;
            y /= numberOfParticles;
        }

        return new Point2D.Float(x, y);
    }

    public int countParticlesInsideRegion(boolean[][] region){
        int sum=0;
        for(int i=0 ; i<height; i++){
            for(int j=0 ; j<width; j++){
                if(region[i][j]){
                    sum+=countParticles(lattice[i][j]);
                }
            }
        }
        return sum;
    }

    private int countParticles(int cell){
        int value=0;
        value += (cell & A) != 0 ? 1 : 0;
        value += (cell & B) != 0 ? 1 : 0;
        value += (cell & C) != 0 ? 1 : 0;
        value += (cell & D) != 0 ? 1 : 0;
        return value;
    }
}
