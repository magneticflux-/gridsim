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

import org.skaggsm.gridsim.subsystem.Subsystem;
import org.skaggsm.gridsim.tile.Tile;
import org.skaggsm.gridsim.tile.delta.TileDelta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Mitchell Skaggs
 */
public class Engine {
    private final World world;
    private final List<Subsystem> subsystems;
    private final ExecutorService executorService;

    public Engine(World world, Subsystem... subsystems) {
        this(world, Arrays.asList(subsystems));
    }

    public Engine(World world, List<Subsystem> subsystems) {
        this.world = world;
        this.subsystems = new ArrayList<>(subsystems);
        this.executorService = Executors.newWorkStealingPool();
    }

    public void tick() {
        Collection<TileDelta> tileDeltas = subsystems.stream().flatMap(subsystem -> subsystem.compute(world, executorService).stream()).collect(Collectors.toList());

        Collection<Future<?>> pendingDeltas = tileDeltas.stream()
                .map(tileDelta -> executorService.submit(new ApplyTileDeltaRunnable(tileDelta, tileDelta.getCorrespondingTile(world))))
                .collect(Collectors.toList());

        pendingDeltas.forEach(FutureExtensionsKt::getSilently);
    }

    private class ApplyTileDeltaRunnable implements Runnable {

        private final TileDelta tileDelta;
        private final Tile correspondingTile;

        public ApplyTileDeltaRunnable(TileDelta tileDelta, Tile correspondingTile) {
            this.tileDelta = tileDelta;
            this.correspondingTile = correspondingTile;
        }

        @Override
        public void run() {
            synchronized (correspondingTile) {
                tileDelta.apply(correspondingTile);
            }
        }
    }
}
