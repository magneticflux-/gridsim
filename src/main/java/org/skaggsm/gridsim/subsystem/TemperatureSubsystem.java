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

/**
 * @author Mitchell Skaggs
 */
public class TemperatureSubsystem extends LocalSubsystem {
    private static final double SIDE_LENGTH = 1; // m
    private static final double CONTACT_AREA = SIDE_LENGTH * SIDE_LENGTH; // m^2
    private static final double DELTA_T = 2000;

    @Override
    protected TileDelta getTileDeltaForTile(int row, int col, World world) {
        Tile tile = world.getTile(row, col);

        double tileTemperatureDelta = 0;

        if (row - 1 >= 0)
            tileTemperatureDelta += getTemperatureDelta(tile, world.getTile(row - 1, col));
        if (row + 1 < world.getRows())
            tileTemperatureDelta += getTemperatureDelta(tile, world.getTile(row + 1, col));
        if (col - 1 >= 0)
            tileTemperatureDelta += getTemperatureDelta(tile, world.getTile(row, col - 1));
        if (col + 1 < world.getCols())
            tileTemperatureDelta += getTemperatureDelta(tile, world.getTile(row, col + 1));

        return new ChangeTemperatureTileDelta(row, col, tileTemperatureDelta);
    }

    private double getTemperatureDelta(Tile tile1, Tile tile2) {
        //dt * A * flux / c
        return getHeatFlux(tile1, tile2) * DELTA_T * CONTACT_AREA / tile1.getHeatCapacity();
    }

    private double getHeatFlux(Tile tile1, Tile tile2) {
        double averageThermalConductivity = (tile1.getThermalConductivity() + tile2.getThermalConductivity()) / 2;
        double deltaTemperature = tile1.getTemperature() - tile2.getTemperature();
        double deltaX = SIDE_LENGTH;

        return -averageThermalConductivity * deltaTemperature / deltaX;
    }
}
