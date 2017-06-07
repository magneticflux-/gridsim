/*
 * Copyright (C) 2017 Mitchell Skaggs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.skaggsm.gridsim;

import org.skaggsm.gridsim.subsystem.TemperatureSubsystem;
import org.skaggsm.gridsim.tile.DefaultMaterial;
import org.skaggsm.gridsim.tile.Tile;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.util.DoubleSummaryStatistics;
import java.util.stream.IntStream;

/**
 * @author Mitchell Skaggs
 */
public class Run {

    public static void main(String[] args) {
        World world = new World(50, 50);
        Engine engine = new Engine(world, new TemperatureSubsystem());

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                world.getTile(row, col).setTemperature(1000);
                world.getTile(row, col).setMaterial(DefaultMaterial.COPPER);
            }
        }

        for (int col = 0; col < world.getCols(); col++) {
            world.getTile(10, col).setMaterial(DefaultMaterial.COPPER);
        }
        for (int row = 0; row < world.getRows(); row++) {
            world.getTile(row, 10).setMaterial(DefaultMaterial.COPPER);
        }

        JFrame jFrame = new JFrame();
        JPanel jPanel = new WorldJPanel(world);
        jFrame.add(jPanel);
        jFrame.setSize(900, 900);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        while (true) {
            //long startTime = System.nanoTime();
            jPanel.paintImmediately(jPanel.getVisibleRect());
            //System.out.println("Paint: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
            //startTime = System.nanoTime();
            for (int i = 0; i < 1; i++)
                engine.tick();
            //System.out.println("Tick: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));

            double tempSum = 0;
            for (int row = 0; row < world.getRows(); row++) {
                for (int col = 0; col < world.getCols(); col++) {
                    tempSum += world.getTile(row, col).getTemperature() * world.getTile(row, col).getHeatCapacity();
                }
            }
            System.out.printf("Total energy: %.4fJ\n", tempSum);
        }
    }

    private static class WorldJPanel extends JPanel {
        private static final int TILE_SIZE = 16;
        private final World world;

        public WorldJPanel(World world) {
            this.world = world;
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            DoubleSummaryStatistics statistics = IntStream.range(0, world.getRows())
                    .boxed()
                    .flatMap(row -> IntStream.range(0, world.getCols()).mapToObj(col -> world.getTile(row, col)))
                    .mapToDouble(Tile::getTemperature)
                    .summaryStatistics();
            double maxTemp = statistics.getMax();
            double minTemp = statistics.getMin();
            if (maxTemp - minTemp < 10) {
                double middle = (maxTemp + minTemp) / 2;
                maxTemp = middle + 5;
                minTemp = middle - 5;
            }
            double tempRange = maxTemp - minTemp;

            for (int row = 0; row < world.getRows(); row++) {
                for (int col = 0; col < world.getCols(); col++) {
                    int x = TILE_SIZE * col;
                    int y = TILE_SIZE * row;

                    Tile tile = world.getTile(row, col);

                    float scaledTemp = (float) ((tile.getTemperature() - minTemp) / tempRange);

                    g.setColor(new Color(scaledTemp, scaledTemp, scaledTemp));
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                    if (tile.getMaterial() == DefaultMaterial.COPPER)
                        g.setColor(Color.ORANGE);
                    else if (tile.getMaterial() == DefaultMaterial.GRANITE)
                        g.setColor(Color.PINK);
                    else if (tile.getMaterial() == DefaultMaterial.BERYLLIUM)
                        g.setColor(Color.GREEN);
                    else if (tile.getMaterial() == DefaultMaterial.STEEL)
                        g.setColor(Color.GRAY);

                    g.drawRect(x, y, TILE_SIZE - 1, TILE_SIZE - 1);
                    /*
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                    g.drawString(String.format("%.3fK", world.getTile(row, col).getTemperature()), x + 20, y + 20);
                    */
                }
            }
        }
    }
}
