package simulations;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;

import exceptions.NotEnoughSpaceException;

public class CurrentSimulator {
	private int height, width;
	
	public CurrentSimulator(int height, int width, int numberOfIterations,
            String staticFile, String dynamicFile, int chunkSize) throws IOException, NotEnoughSpaceException {

		this.height = height;
		this.width = width;

		char[][] particles = new char[height][width];
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				if(i > 4 && i < (height / 2 - 4) && j == 205) {
					particles[i][j] = LatticeGasAutomata.A;
				}else {
					particles[i][j] = 0;
				}
			}
		}
		
		Region wallRegion = new Region(width, height);

		int halfHeight = height/2;
		wallRegion.addRectangle(0, 0, width-1, 2, false);
		wallRegion.addRectangle(30, halfHeight-3, width-121, halfHeight-1, false);
		
		wallRegion.addRectangle(20, 0, 22, halfHeight/2+1, false);
		
		wallRegion.addRectangle(40, halfHeight/2+1, 42, halfHeight-1, false);

		
		wallRegion.addRectangle(70, 0, 72, halfHeight/2+1, false);
		
		wallRegion.addRectangle(100, halfHeight/2+1, 102, halfHeight-1, false);


		wallRegion.addRectangle(150, 0, 152, halfHeight/2+1, false);
		
		wallRegion.addRectangle(200, halfHeight/2+1, 202, halfHeight-1, false);

		wallRegion.addRectangle(width-3, 0, width - 1, height - 1, false);

		wallRegion.addRectangle(0, height - 3, width - 1, height - 1, false);

		wallRegion.addRectangle(0, 0, 2, height - 1, false);

		
		Region currentRegion = new Region(width, height);
		
		LatticeGasAutomata latticeGasAutomata = new LatticeGasAutomata(width, height, wallRegion.getRegion());
		
		try(PrintWriter staticFilePW = new PrintWriter(staticFile)){
            staticFilePW.println("# HEIGHT WIDTH");
            staticFilePW.println(height + " " + width);
            staticFilePW.println("# NUMBER OF PARTICLES");
            staticFilePW.println(-1);
            staticFilePW.println("# NUMBER OF ITERATIONS");
            staticFilePW.println(numberOfIterations);
            staticFilePW.println("# CHUNK SIZE");
            staticFilePW.println(chunkSize);
            staticFilePW.println("# WALL MATRIX (1 is wall and 0 is empty)");
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    staticFilePW.write((wallRegion.getRegion()[i][j] ? 1 : 0) +" ");
                }
                staticFilePW.write('\n');
            }
        }
		
		
		try(PrintWriter dynamicFilePW = new PrintWriter(dynamicFile)){

	            for(int t=1; t <= numberOfIterations; t++){
	                dynamicFilePW.write("# T"+t+'\n');

	                latticeGasAutomata.insertParticles(particles);
	                latticeGasAutomata.update();

	                Point2D.Float[][] chunks = latticeGasAutomata.getMeanVelocities(chunkSize);
	                for(int i=0; i<height/chunkSize; i++){
	                    for(int j=0; j<width/chunkSize; j++){
	                        float x = (float)chunks[i][j].getX(), y = (float)chunks[i][j].getY();
	                        dynamicFilePW.write("("+x+","+y+") ");
	                    }
	                    dynamicFilePW.write('\n');
	                }

	            }

	        }
		
	}

	
}
