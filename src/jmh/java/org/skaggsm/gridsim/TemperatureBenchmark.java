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

import org.openjdk.jmh.annotations.*;
import org.skaggsm.gridsim.subsystem.TemperatureSubsystem;
import org.skaggsm.gridsim.tile.DefaultMaterial;

/**
 * @author Mitchell Skaggs
 */
@State(Scope.Thread)
public class TemperatureBenchmark {
    private Engine engine;

    @Setup
    public void setupWorld() {
        System.out.println("Setup running!");
        World world = new World(1000, 1000);
        engine = new Engine(world, new TemperatureSubsystem());

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                world.getTile(row, col).setTemperature(1000);
                world.getTile(row, col).setMaterial(DefaultMaterial.COPPER);
            }
        }

        for (int col = 0; col < world.getCols(); col++) {
            world.getTile(10, col).setMaterial(DefaultMaterial.COPPER);
            world.getTile(world.getRows() - 1, col).setMaterial(DefaultMaterial.COPPER);
        }
        for (int row = 0; row < world.getRows(); row++) {
            world.getTile(row, 10).setMaterial(DefaultMaterial.COPPER);
            world.getTile(row, world.getCols() - 1).setMaterial(DefaultMaterial.COPPER);
        }
    }

    @Benchmark
    @Threads(1)
    @Fork(1)
    @Warmup(iterations = 5, batchSize = 1, time = 5)
    @Measurement(iterations = 5, batchSize = 1, time = 5)
    public void measureTick() {
        engine.tick();
    }
}
