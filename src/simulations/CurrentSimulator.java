package simulations;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
				if((i > 4 && i < (height / 2 - 4) && j >= 283 && j < width - 121) || ((i > height/2 - (height/4) + 3 && i < (height / 2 - 4) && j == 212))) {
					particles[i][j] = LatticeGasAutomata.A;
				}else if( (i > 4 && i < (height / 2 - 4) && j >= width - 121) ) {
					particles[i][j] = LatticeGasAutomata.F;
				}
				else {
					particles[i][j] = 0;
				}
			}
		}
		
		Region wallRegion = new Region(width, height);

		int halfHeight = height/2;
		wallRegion.addRectangle(0, 0, width-1, 2, false);
		wallRegion.addRectangle(70, halfHeight-3, width-121, halfHeight-1, false);

		//first
		wallRegion.addRectangle(70, halfHeight/2 + 1, 72, halfHeight - 1, false);

		//sec
		wallRegion.addRectangle(140, 0, 142, halfHeight/2+1, false);

		//third
		wallRegion.addRectangle(210, halfHeight/2 + 1, 212, halfHeight - 1, false);

		//fourth
		wallRegion.addRectangle(280, 0, 282, halfHeight/2 + 1, false);

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
