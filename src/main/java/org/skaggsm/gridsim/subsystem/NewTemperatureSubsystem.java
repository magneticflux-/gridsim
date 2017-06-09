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

import java.util.Arrays;
import java.util.List;

/**
 * @author Mitchell Skaggs
 */
public class NewTemperatureSubsystem extends PerAdjacencySubsystem {
    public static final double SIDE_LENGTH = 1; // m
    public static final double CONTACT_AREA = SIDE_LENGTH * SIDE_LENGTH; // m^2
    public static final double DELTA_TIME = 1;

    @Override
    protected List<TileDelta> getTileDeltasForAdjacency(int row1, int col1, int row2, int col2, World world) {
        Tile tile1 = world.getTile(row1, col1);
        Tile tile2 = world.getTile(row2, col2);
        double heatFlux = getHeatFlux(tile1, tile2);
        return Arrays.asList(
                new ChangeTemperatureTileDelta(row1, col1, heatFlux / tile1.getHeatCapacity()),
                new ChangeTemperatureTileDelta(row2, col2, -heatFlux / tile2.getHeatCapacity())
        );
    }

    public static double getHeatFlux(Tile tile1, Tile tile2) {
        double averageThermalConductivity = (tile1.getThermalConductivity() + tile2.getThermalConductivity()) / 2;
        double deltaTemperature = tile1.getTemperature() - tile2.getTemperature();
        double dx = SIDE_LENGTH;
        double dt = DELTA_TIME;

        return -averageThermalConductivity * deltaTemperature * dt * CONTACT_AREA / dx;
    }
}
