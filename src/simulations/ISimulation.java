package simulations;

import java.awt.geom.Point2D;

public interface ISimulation {
    public boolean getUseHPP();
    public int getWidth();
    public int getHeight();
    public Region getWallRegion();
    public int getNumberOfParticles();
    public void update();
    public Point2D.Float[][] getChunks(int chunkSize);
}
