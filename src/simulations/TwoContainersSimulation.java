package simulations;

import java.awt.geom.Point2D;
import java.io.IOException;

import exceptions.NotEnoughSpaceException;

public class TwoContainersSimulation implements ISimulation {
	private int height, width;
	private Region leftContainer, rightContainer, wallRegion;
	private ILatticeGasAutomata latticeGasAutomata;
	
	private int numberOfParticles;
	private int chunkSize;
	private boolean useHPP = false;

    public TwoContainersSimulation(int height, int width, int numberOfParticles, int numberOfIterations,
                                    int chunkSize) throws IOException, NotEnoughSpaceException {

        this.width = width;
        this.height = height;
        this.numberOfParticles = numberOfParticles;
        this.chunkSize = chunkSize;
        
        generateSimulation();
    }
    
    public TwoContainersSimulation(int height, int width, int numberOfParticles) throws IOException, NotEnoughSpaceException {

		this.width = width;
		this.height = height;
		this.numberOfParticles = numberOfParticles;
	
		generateSimulation();
	}

    public TwoContainersSimulation(int height, int width, int numberOfParticles, boolean useHPP) throws IOException, NotEnoughSpaceException {

        this.width = width;
        this.height = height;
        this.numberOfParticles = numberOfParticles;
        this.useHPP = useHPP;

        generateSimulation();
    }
    
    private void generateSimulation() throws NotEnoughSpaceException{
        int wallPart = 2;

    	leftContainer = new Region(width, height);
        leftContainer.addRectangle(0, 0, width/wallPart-1, height-1, false);

        rightContainer = new Region(width, height);
        rightContainer.addRectangle(width/wallPart+1, 0, width-1, height-1, false);

        wallRegion = new Region(width, height);
        //middle wall
        wallRegion.addRectangle(width/wallPart, 0, width/wallPart, height-1, false);

        //hole in the wall
        wallRegion.addRectangle(width/wallPart-1, height/2-height/8, width/wallPart + 1, height/2+height/8, true);

        wallRegion.addRectangleBorders(0, 0, width-1, height-1, 2, false);

        if (useHPP) {
            latticeGasAutomata = new HPPModel(width, height, leftContainer.getRegion(), wallRegion.getRegion(), numberOfParticles);
        } else {
            latticeGasAutomata = new LatticeGasAutomata(width, height, leftContainer.getRegion(), wallRegion.getRegion(), numberOfParticles);
        }
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

    public boolean getUseHPP() {
        return useHPP;
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
