package simulations;

import java.awt.geom.Point2D;

public interface ILatticeGasAutomata {
    public int countParticlesInsideRegion(boolean[][] region);
    public Point2D.Float[][] getMeanVelocities(int chunkSize);
    public void update();
    public char[][] getLattice();
}
