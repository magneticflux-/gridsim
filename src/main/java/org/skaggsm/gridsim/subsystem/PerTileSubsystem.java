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

import org.skaggsm.gridsim.FutureExtensionsKt;
import org.skaggsm.gridsim.World;
import org.skaggsm.gridsim.tile.delta.TileDelta;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Mitchell Skaggs
 */
public abstract class PerTileSubsystem implements Subsystem {

    @Override
    public Collection<TileDelta> compute(World world, ForkJoinPool forkJoinPool) {
        List<Future<TileDelta>> futures = new LinkedList<>();

        for (int row = 0; row < world.getRows(); row++) {
            for (int col = 0; col < world.getCols(); col++) {
                futures.add(forkJoinPool.submit(new ComputeTileDeltaCallable(row, col, world)));
            }
        }

        return futures.stream().map(FutureExtensionsKt::getSilently).collect(Collectors.toList());
    }

    /**
     * @param row   the row of the tile to compute a delta for
     * @param col   the column of the tile to compute a delta for
     * @param world the current world
     * @return a {@link TileDelta} expressing the tile's change
     * @implNote This method is called from multiple threads simultaneously, and therefore must be thread-safe.
     */
    protected abstract TileDelta getTileDeltaForTile(int row, int col, World world);

    private class ComputeTileDeltaCallable implements Callable<TileDelta> {
        private final int row, col;
        private final World world;

        public ComputeTileDeltaCallable(int row, int col, World world) {
            this.row = row;
            this.col = col;
            this.world = world;
        }

        @Override
        public TileDelta call() throws Exception {
            return getTileDeltaForTile(row, col, world);
        }
    }
}
