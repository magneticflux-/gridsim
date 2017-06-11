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

package org.skaggsm.gridsim.subsystem;

import kotlin.Pair;
import org.skaggsm.gridsim.World;
import org.skaggsm.gridsim.tile.delta.TileDelta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static org.skaggsm.gridsim.FutureExtensionsKt.getSilently;

/**
 * @author Mitchell Skaggs
 */
public abstract class PerAdjacencySubsystem implements Subsystem {

    @Override
    public Collection<TileDelta> compute(World world, ForkJoinPool forkJoinPool) {
        List<TilePair> tilePairs = new ArrayList<>(numPairs(world.getRows(), world.getCols()));
        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {
                if (row + 1 < world.getRows())
                    tilePairs.add(new TilePair(row, col, row + 1, col));
                if (col + 1 < world.getCols())
                    tilePairs.add(new TilePair(row, col, row, col + 1));
            }
        }
        return getSilently(forkJoinPool.submit(new TileDeltasRecursiveTask(tilePairs, world)));
    }

    protected static int numPairs(int rows, int cols) {
        // r*(c-1) + c*(r-1)
        return rows * (cols - 1) + cols * (rows - 1); // Formula for number of adjacent pairs in a grid
    }

    protected abstract Pair<TileDelta, TileDelta> getTileDeltasForAdjacency(int row1, int col1, int row2, int col2, World world);

    private class TileDeltasRecursiveTask extends RecursiveTask<List<TileDelta>> {
        private final List<TilePair> tilePairs;
        private final World world;

        public TileDeltasRecursiveTask(List<TilePair> tilePairs, World world) {
            this.tilePairs = tilePairs;
            this.world = world;
        }

        @Override
        protected List<TileDelta> compute() {
            if (tilePairs.size() < 2500) {
                List<TileDelta> tileDeltas = new ArrayList<>(tilePairs.size() * 2);
                for (TilePair tilePair : tilePairs) {
                    Pair<TileDelta, TileDelta> pair = getTileDeltasForAdjacency(
                            tilePair.getRow1(),
                            tilePair.getCol1(),
                            tilePair.getRow2(),
                            tilePair.getCol2(),
                            world
                    );
                    tileDeltas.add(pair.getFirst());
                    tileDeltas.add(pair.getSecond());
                }
                return tileDeltas;
            } else {
                List<TileDelta> results = new ArrayList<>(tilePairs.size() * 2);

                ForkJoinTask<List<TileDelta>> task1 = new TileDeltasRecursiveTask(tilePairs.subList(0, tilePairs.size() / 2), world).fork();
                TileDeltasRecursiveTask task2 = new TileDeltasRecursiveTask(tilePairs.subList(tilePairs.size() / 2, tilePairs.size()), world);

                results.addAll(task2.compute());
                results.addAll(task1.join());

                return results;
            }
        }
    }
}
