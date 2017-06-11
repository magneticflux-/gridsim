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
public abstract class PerTileSubsystem implements Subsystem {

    @Override
    public Collection<TileDelta> compute(World world, ForkJoinPool forkJoinPool) {
        List<TileLocation> tileLocations = new ArrayList<>(world.getRows() * world.getCols());

        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {
                tileLocations.add(new TileLocation(row, col));
            }
        }

        return getSilently(forkJoinPool.submit(new ComputeTileDeltaRecursiveTask(tileLocations, world)));
    }

    /**
     * @param row   the row of the tile to compute a delta for
     * @param col   the column of the tile to compute a delta for
     * @param world the current world
     * @return a {@link TileDelta} expressing the tile's change
     * @implNote This method is called from multiple threads simultaneously, and therefore must be thread-safe.
     */
    protected abstract TileDelta getTileDeltaForTile(int row, int col, World world);

    private class ComputeTileDeltaRecursiveTask extends RecursiveTask<List<TileDelta>> {
        private final List<TileLocation> tiles;
        private final World world;

        public ComputeTileDeltaRecursiveTask(List<TileLocation> tiles, World world) {
            this.tiles = tiles;
            this.world = world;
        }

        @Override
        protected List<TileDelta> compute() {
            if (tiles.size() < 2000) {
                List<TileDelta> deltas = new ArrayList<>(tiles.size());
                for (TileLocation tileLocation : tiles)
                    deltas.add(getTileDeltaForTile(tileLocation.getRow(), tileLocation.getCol(), world));
                return deltas;
            } else {
                List<TileDelta> results = new ArrayList<>(tiles.size());

                ForkJoinTask<List<TileDelta>> task1 = new ComputeTileDeltaRecursiveTask(tiles.subList(0, tiles.size() / 2), world).fork();
                ComputeTileDeltaRecursiveTask task2 = new ComputeTileDeltaRecursiveTask(tiles.subList(tiles.size() / 2, tiles.size()), world);

                results.addAll(task2.compute());
                results.addAll(task1.join());

                return results;
            }
        }
    }

}
