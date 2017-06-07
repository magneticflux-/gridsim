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

package org.skaggsm.gridsim.tile;

/**
 * @author Mitchell Skaggs
 */
public enum DefaultMaterial implements Material {
    GRANITE(3.39, 790, 2750),
    COPPER(401, 385, 8960),
    BERYLLIUM(200, 1820, 1850),
    STEEL(18, 466, 7900);

    private final double thermalConductivity; // J / (m * K * s)
    private final double heatCapacity; // J /(kg * K)
    private final double density; // kg / m^3

    DefaultMaterial(double thermalConductivity, double heatCapacity, double density) {
        this.thermalConductivity = thermalConductivity;
        this.heatCapacity = heatCapacity;
        this.density = density;
    }

    @Override
    public double getThermalConductivity() {
        return thermalConductivity;
    }

    @Override
    public double getSpecificHeatCapacity() {
        return heatCapacity;
    }

    @Override
    public double getDensity() {
        return density;
    }
}
