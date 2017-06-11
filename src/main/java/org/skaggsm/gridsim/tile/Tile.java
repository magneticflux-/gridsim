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

import com.google.common.util.concurrent.AtomicDouble;

/**
 * @author Mitchell Skaggs
 */
public class Tile {
    private Material material;
    private AtomicDouble temperature; // K
    private double sideLength; // m

    public Tile(double sideLength) {
        this(sideLength, DefaultMaterial.GRANITE);
    }

    public Tile(double sideLength, Material material) {
        this(sideLength, 273, material);
    }

    public Tile(double sideLength, double temperature, Material material) {
        this.sideLength = sideLength;
        this.temperature = new AtomicDouble(temperature);
        this.material = material;
    }

    public Tile(double sideLength, double temperature) {
        this(sideLength, temperature, DefaultMaterial.GRANITE);
    }

    public double getTemperature() {
        return temperature.get();
    }

    public void setTemperature(double temperature) {
        this.temperature.set(temperature);
    }

    public double getThermalConductivity() {
        return material.getThermalConductivity();
    }

    public double getHeatCapacity() {
        return material.getSpecificHeatCapacity() * getMass();
    }

    public double getMass() {
        return getDensity() * getVolume();
    }

    public double getDensity() {
        return material.getDensity();
    }

    public double getVolume() {
        return sideLength * sideLength * sideLength;
    }

    public Material getMaterial() {
        return material;
    }

    public Tile setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public void addTemperature(double temperatureDelta) {
        this.temperature.addAndGet(temperatureDelta);
    }
}
