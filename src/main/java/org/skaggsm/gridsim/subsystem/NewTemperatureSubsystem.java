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

import static org.skaggsm.gridsim.subsystem.TemperatureSubsystem.CONTACT_AREA;
import static org.skaggsm.gridsim.subsystem.TemperatureSubsystem.DELTA_T;

/**
 * @author Mitchell Skaggs
 */
public class NewTemperatureSubsystem extends PerAdjacencySubsystem {
    @Override
    protected List<TileDelta> getTileDeltasForAdjacency(int row1, int col1, int row2, int col2, World world) {
        Tile tile1 = world.getTile(row1, col1);
        Tile tile2 = world.getTile(row2, col2);
        double heatFlux = TemperatureSubsystem.getHeatFlux(tile1, tile2) * DELTA_T * CONTACT_AREA;
        return Arrays.asList(
                new ChangeTemperatureTileDelta(row1, col1, heatFlux / tile1.getHeatCapacity()),
                new ChangeTemperatureTileDelta(row2, col2, -heatFlux / tile2.getHeatCapacity())
        );
    }
}
