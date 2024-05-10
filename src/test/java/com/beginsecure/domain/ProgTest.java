package com.beginsecure.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.File;

import javax.swing.*;
import java.awt.*;

class ProgTest {

    @Test
    public void test_min() {
        int[] numbers = {5, 2, 8, 1, 9};
        assertEquals(1, Prog._min(numbers));
    }

    @Test
    public void test_max() {
        int[] numbers = {5, 2, 8, 1, 9};
        assertEquals(9, Prog._max(numbers));
    }

    @Test
    public void test_sum() {
        int[] numbers = {5, 2, 8, 1, 9};
        assertEquals(25, Prog._sum(numbers));
    }

    @Test
    public void test_mult() {
        int[] numbers = {5, 2, 8, 1, 9};
        assertEquals(720, Prog._mult(numbers));
    }

    @Test
    void testPerformance() throws IOException {
        Random random = new Random();

        int limit = 13;
        int[] sizes = new int[limit];
        for (int i = 0; i < limit; ++i) {
            sizes[i] = (int) Math.pow(2, i);
        }
        long[] times = new long[limit];

        for (int i = 0; i < limit; i++) {
            int[] numbers = new int[sizes[i]];
            for (int j = 0; j < sizes[i]; j++) {
                numbers[j] = random.nextInt(1000);
            }

            long startTime = System.nanoTime();
            Prog._min(numbers);
            long endTime = System.nanoTime();
            times[i] = endTime - startTime;

            assertTrue(times[i] <= 20000000, "Performance test failed" + " " + times[i]);
        }

        long[] data1 = new long[limit];
        for (long i = 0; i < limit; i++) {
            data1[(int) i] = i;
        }
        XYSeries series = new XYSeries("Data");
        for (int i = 1; i < data1.length; i++) {
            series.add(data1[i], times[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Dependency Graph",
                "X Axis",
                "Y Axis",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

        try {
            ChartUtils.saveChartAsPNG(new File("dependency_graph.png"), chart, 800, 600);
            System.out.println("Изображение сохранено как dependency_graph.png");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
        }
    }

    @Test
    void testMemory() throws IOException {
        Random random = new Random();

        int limit = 30;
        int[] sizes = new int[limit];
        for (int i = 0; i < limit; ++i) {
            sizes[i] = (int) Math.pow(10, 5);
        }

        for (int i = 0; i < limit; i++) {
            int[] numbers = new int[sizes[i]];
            for (int j = 0; j < sizes[i]; j++) {
                numbers[j] = random.nextInt(1000);
            }

            Prog._min(numbers);
            long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            assertTrue(usedBytes < 50000000, "Memory test failed" + " " + usedBytes);
        }
    }
}
