package simulations;

import java.awt.geom.Point2D;
import java.io.IOException;

import exceptions.NotEnoughSpaceException;

public class HallwaySimulation implements ISimulation {
    private int height, width;
    private Region leftContainer, rightContainer, wallRegion;
    private LatticeGasAutomata latticeGasAutomata;

    private int numberOfParticles;
    private int chunkSize;

    public boolean getUseHPP() {return false;}


    public HallwaySimulation(int height, int width, int numberOfParticles, int numberOfIterations,
                                   int chunkSize) throws IOException, NotEnoughSpaceException {

        this.width = width;
        this.height = height;
        this.numberOfParticles = numberOfParticles;
        this.chunkSize = chunkSize;

        generateSimulation(100);
    }

    public HallwaySimulation(int height, int width, int numberOfParticles) throws IOException, NotEnoughSpaceException {

        this.width = width;
        this.height = height;
        this.numberOfParticles = numberOfParticles;

        generateSimulation(100);
    }

    private void generateSimulation(int hallwayWidth) throws NotEnoughSpaceException{
        // Temporarily set hallwayWidth for testing purposes
        hallwayWidth = height / 8;

        leftContainer = new Region(width, height);
        leftContainer.addRectangle(0, 0, width/3-1, height-1, false);

        rightContainer = new Region(width, height);
        rightContainer.addRectangle(2*width/3+1, 0, width-1, height-1, false);

        wallRegion = new Region(width, height);
        //middle wall
        wallRegion.addRectangle(width/3, 0, 2*width/3, height-1, false);

        //hole in the wall
        wallRegion.addRectangle(width/3-1, height/2-hallwayWidth, 2*width/3 + 1, height/2+hallwayWidth, true);

        wallRegion.addRectangleBorders(0, 0, width-1, height-1, 2, false);

        latticeGasAutomata = new LatticeGasAutomata(width, height, leftContainer.getRegion(), wallRegion.getRegion(), numberOfParticles);
    }

    public int getLeftContainerParticles() {
        return latticeGasAutomata.countParticlesInsideRegion(leftContainer.getRegion());
    }

    public int getRightContainerParticles() {
        return latticeGasAutomata.countParticlesInsideRegion(rightContainer.getRegion());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Region getWallRegion() {
        return wallRegion;
    }

    public int getNumberOfParticles() {
        return numberOfParticles;
    }

    public void update() {
        this.latticeGasAutomata.update();
    }

    public Point2D.Float[][] getChunks(int chunkSize) {
        return latticeGasAutomata.getMeanVelocities(chunkSize);
    }
}
