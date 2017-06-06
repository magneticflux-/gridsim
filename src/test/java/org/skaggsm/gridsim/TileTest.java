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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skaggsm.gridsim.tile.Tile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Mitchell Skaggs
 */
@DisplayName("A Tile")
class TileTest {
    private Tile tile;

    @Nested
    @DisplayName("when new")
    class WhenNew {
        @BeforeEach
        void createNewTile() {
            tile = new Tile();
        }

        @DisplayName("is at 273K")
        @Test
        void isAtZeroCelsius() {
            assertThat(tile.getTemperature(), is(equalTo(273d)));
        }

        @DisplayName("has thermal conductivity of water (0.591)")
        @Test
        void hasThermalConductivityOfWater() {
            assertThat(tile.getThermalConductivity(), is(equalTo(0.591)));
        }
    }
}
