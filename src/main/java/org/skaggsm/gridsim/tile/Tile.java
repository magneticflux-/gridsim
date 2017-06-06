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
public class Tile {
    private double temperature = 273; // K
    private double thermalConductivity = 10.591; // J / (m * K * s)
    private double heatCapacity = 0.6; // J /(g * K)
    private double mass = 1; // kg

    public double getTemperatureC() {
        return getTemperature() - 273;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setTemperatureC(double temperature) {
        setTemperature(temperature + 273);
    }

    public double getThermalConductivity() {
        return thermalConductivity;
    }

    public double getHeatCapacity() {
        return heatCapacity * (mass * 1000);
    }

    public double getMass() {
        return mass;
    }
}
