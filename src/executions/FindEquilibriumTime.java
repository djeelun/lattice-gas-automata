package executions;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import exceptions.NotEnoughSpaceException;
import org.jfree.chart.ChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import simulations.TwoContainersSimulation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;

public class FindEquilibriumTime {

    final static int MAX_ITERATIONS = 500000;

    public FindEquilibriumTime(int minN, int maxN, int step, int numberOfExecutions, int numberOfIterations, String file, float threshHoldRatio, int partN) throws IOException, NotEnoughSpaceException {

        for (int e = 0; e < 1; e++) {

            try (PrintWriter resultFile = new PrintWriter(file + (e + 1))) {
                for (int N = partN; N <= partN; N += step) {
                    System.out.println(N + "/" + maxN + " - Step " + step);

                    TwoContainersSimulation sim = new TwoContainersSimulation(200, 200, N, numberOfIterations);

                    resultFile.println("# N" + N);

                    int iterations = 0;
                    float ratioRight = 0, radioLeft = 1;
                    int side = 1, previousSide = 1;
                    boolean condition = true;
                    float maxDifference = -1;
                    int firstEquilibrumIteration = -1;
                    boolean previouslyChanged = false;
                    int sideChanges = 0;
                    float difference = -1;
//
//                    XYSeriesCollection dataset = new XYSeriesCollection( );
//                    XYSeries serieR = new XYSeries("Contenedor derecho");
//                    XYSeries serieL = new XYSeries("Contenedor izquierdo");

                    while((radioLeft > ratioRight)) {
                        ratioRight = sim.getRightContainerParticles() / (float) N;
						radioLeft = 1 - ratioRight;
                        iterations++;
                        sim.update();
                        System.out.println("Iteration: " + iterations);
                        System.out.println(radioLeft + " - " + ratioRight);
                    }

                    int maxDiffIteration = 0;

//                    while ( (maxDifference > threshHoldRatio || maxDifference == -1) && iterations < MAX_ITERATIONS) {
//
//						ratioRight = sim.getRightContainerParticles() / (float) N;
//						radioLeft = 1 - ratioRight;
//                        maxDifference = - 1;
//                        side = radioLeft > ratioRight ? 1 : -1;
//
//                        if(firstEquilibrumIteration == -1 && (1 - ratioRight <= threshHoldRatio)){
//                            firstEquilibrumIteration = iterations;
//                        }
//
//                        serieR.add(iterations, sim.getRightContainerParticles());
//                        serieL.add(iterations, sim.getLeftContainerParticles());
//                        if(side != previousSide)
//                            sideChanges++;
//                        while (side != previousSide && iterations < MAX_ITERATIONS) {
//                            previouslyChanged = true;
//                            float difference = Math.abs(ratioRight - radioLeft) / 2 + 0.5f;
//                            if ((difference > maxDifference || maxDifference == -1) && difference > 0.505f) {
//                                maxDifference = difference;
//                                if(maxDifference < threshHoldRatio)
//                                    maxDiffIteration = iterations;
//                            }
//
//                            iterations++;
//                            sim.update();
//							ratioRight = sim.getRightContainerParticles() / (float) N;
//							radioLeft = 1 - ratioRight;
//                            side = radioLeft > ratioRight ? 1 : -1;
//                            serieR.add(iterations, sim.getRightContainerParticles());
//                            serieL.add(iterations, sim.getLeftContainerParticles());
//                        }
//
//                        if(maxDifference > threshHoldRatio)
//                            maxDiffIteration = 0;
//
//                        if(previouslyChanged)
//                            previousSide = side * -1;
//
//                        iterations++;
//                        sim.update();
//                    }
//
//                    int itAux = iterations;
//                    while (itAux < MAX_ITERATIONS) {
//                        serieR.add(itAux, sim.getRightContainerParticles());
//                        serieL.add(itAux, sim.getLeftContainerParticles());
//                        itAux++;
//                        sim.update();
//                    }
//
//                    if(!previouslyChanged || (iterations == MAX_ITERATIONS && firstEquilibrumIteration != -1) || (sideChanges < 2)){
//                        iterations = firstEquilibrumIteration;
//                        maxDiffIteration = iterations;
//                    }
//                    dataset.addSeries(serieL);
//                    dataset.addSeries(serieR);
//
//                    XYSeries serie = new XYSeries("Equilibrio");
//                    for(int i = 0; i < partN; i++) {
//                        serie.add(maxDiffIteration, i);
//                    }
//                    dataset.addSeries(serie);
//
//                    JPanel chartPanel = createChartPanel(dataset, partN);
//                    add(chartPanel, BorderLayout.CENTER);
//
//                    setSize(800,600);
//
//
//                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    setLocationRelativeTo(null);




                }

            }
        }
    }

}
