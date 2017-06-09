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
import org.skaggsm.gridsim.tile.delta.TileDelta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

import static org.skaggsm.gridsim.FutureExtensionsKt.getSilently;

/**
 * @author Mitchell Skaggs
 */
public class Engine {
    private final World world;
    private final List<Subsystem> subsystems;
    private final ForkJoinPool forkJoinPool;

    public Engine(World world, Subsystem... subsystems) {
        this(world, Arrays.asList(subsystems));
    }

    public Engine(World world, List<Subsystem> subsystems) {
        this.world = world;
        this.subsystems = new ArrayList<>(subsystems);
        this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    }

    public void tick() {
        //Stopwatch stopwatch = Stopwatch.createStarted();
        List<TileDelta> tileDeltas = subsystems.stream().flatMap(subsystem -> subsystem.compute(world, forkJoinPool).stream()).collect(Collectors.toList());
        //System.out.printf("Compute time:\t%8d\n", stopwatch.elapsed(TimeUnit.NANOSECONDS));
        //stopwatch.reset().start();

        getSilently(forkJoinPool.submit(new ApplyTileDeltaRecursiveAction(tileDeltas, world)));
        //System.out.printf("Apply time:  \t%8d\n", stopwatch.elapsed(TimeUnit.NANOSECONDS));
    }

    private static class ApplyTileDeltaRecursiveAction extends RecursiveAction {
        private final List<TileDelta> tileDeltas;
        private final World world;

        public ApplyTileDeltaRecursiveAction(List<TileDelta> tileDeltas, World world) {
            this.tileDeltas = tileDeltas;
            this.world = world;
        }

        @Override
        protected void compute() {
            if (tileDeltas.size() < 16000) {
                for (TileDelta tileDelta : tileDeltas) {
                    tileDelta.apply(world);
                }
            } else {
                ForkJoinTask<Void> action1 = new ApplyTileDeltaRecursiveAction(tileDeltas.subList(0, tileDeltas.size() / 2), world).fork();
                ApplyTileDeltaRecursiveAction action2 = new ApplyTileDeltaRecursiveAction(tileDeltas.subList(tileDeltas.size() / 2, tileDeltas.size()), world);

                action2.compute();
                action1.join();
            }
        }
    }

}
