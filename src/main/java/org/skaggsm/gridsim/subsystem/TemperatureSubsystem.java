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
import org.skaggsm.gridsim.tile.Tile;
import org.skaggsm.gridsim.tile.delta.ChangeTemperatureTileDelta;
import org.skaggsm.gridsim.tile.delta.TileDelta;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mitchell Skaggs
 */
public class TemperatureSubsystem extends LocalSubsystem {
    private static final double SIDE_LENGTH = 1; // m
    private static final double CONTACT_AREA = SIDE_LENGTH * SIDE_LENGTH; // m^2
    private static final double DELTA_T = 1000;

    private final ConcurrentHashMap<TilePair, Double> computations = new ConcurrentHashMap<>();

    @Override
    protected void preCompute() {
        computations.clear();
    }

    @Override
    protected TileDelta getTileDeltaForTile(int row, int col, World world) {
        Tile tile = world.getTile(row, col);

        double tileTemperatureDelta = 0;

        if (row - 1 >= 0)
            tileTemperatureDelta += getTemperatureDeltaNew(tile, row, col, world.getTile(row - 1, col), row - 1, col);
        if (row + 1 < world.getRows())
            tileTemperatureDelta += getTemperatureDeltaNew(tile, row, col, world.getTile(row + 1, col), row + 1, col);
        if (col - 1 >= 0)
            tileTemperatureDelta += getTemperatureDeltaNew(tile, row, col, world.getTile(row, col - 1), row, col - 1);
        if (col + 1 < world.getCols())
            tileTemperatureDelta += getTemperatureDeltaNew(tile, row, col, world.getTile(row, col + 1), row, col + 1);

        return new ChangeTemperatureTileDelta(row, col, tileTemperatureDelta);
    }

    private double getTemperatureDeltaNew(Tile tile1, int row1, int col1, Tile tile2, int row2, int col2) {
        boolean outOfOrder = TilePair.isOutOfOrder(row1, col1, row2, col2);
        double result = computations.computeIfAbsent(new TilePair(outOfOrder, row1, col1, row2, col2), tilePair -> (outOfOrder ? -1 : 1) * getHeatFlux(tile1, tile2) * DELTA_T * CONTACT_AREA);
        //dt * A * flux / c
        return (outOfOrder ? -1 : 1) * result / tile1.getHeatCapacity();
    }

    private static double getHeatFlux(Tile tile1, Tile tile2) {
        double averageThermalConductivity = (tile1.getThermalConductivity() + tile2.getThermalConductivity()) / 2;
        double deltaTemperature = tile1.getTemperature() - tile2.getTemperature();
        double deltaX = SIDE_LENGTH;

        return -averageThermalConductivity * deltaTemperature / deltaX;
    }

    private double getTemperatureDeltaOld(Tile tile1, int row1, int col1, Tile tile2, int row2, int col2) {
        return getHeatFlux(tile1, tile2) * DELTA_T * CONTACT_AREA / tile1.getHeatCapacity();
    }

    private static final class TilePair {
        final int row1, col1, row2, col2;

        public TilePair(int row1, int col1, int row2, int col2) {
            this(isOutOfOrder(row1, col1, row2, col2), row1, col1, row2, col2);
        }

        TilePair(boolean isOutOfOrder, int row1, int col1, int row2, int col2) {
            // Always assign #1 to be smallest in row, or smallest in col if row is equal.
            if (isOutOfOrder) {
                this.row1 = row2;
                this.col1 = col2;
                this.row2 = row1;
                this.col2 = col1;
            } else {
                this.row1 = row1;
                this.col1 = col1;
                this.row2 = row2;
                this.col2 = col2;
            }

        }

        /**
         * Smaller is defined as having a smaller row value, or, if the rows are the same, a smaller column value.
         *
         * @param row1 row 1
         * @param col1 column 1
         * @param row2 row 2
         * @param col2 column 2
         * @return if the first row/col pair is the "smaller" one or not
         */
        static boolean isOutOfOrder(int row1, int col1, int row2, int col2) {
            if (row1 < row2)
                return false;
            else if (row1 > row2)
                return true;
            else if (col1 < col2)
                return false;
            else if (col1 > col2)
                return true;

            // They're the same, so order doesn't matter
            return false;
        }

        @Override
        public int hashCode() {
            int result = row1;
            result = 31 * result + col1;
            result = 31 * result + row2;
            result = 31 * result + col2;
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TilePair tilePair = (TilePair) o;

            return row1 == tilePair.row1 && col1 == tilePair.col1 && row2 == tilePair.row2 && col2 == tilePair.col2;
        }
    }
}
