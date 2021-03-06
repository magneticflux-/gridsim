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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mitchell Skaggs
 */
@DisplayName("A World")
class WorldTest {
    private World world;

    @Test
    @DisplayName("is instantiated with new World()")
    void isInstantiatedWithNew() {
        new World(1, 1);
    }

    @Nested
    @DisplayName("when initialized as 5x5")
    class WhenNew {
        @BeforeEach
        void createNewWorld() {
            world = new World(5, 5);
        }

        @Test
        @DisplayName("has 5 rows")
        void hasCorrectRows() {
            assertThat(world.getRows(), is(5));
        }

        @Test
        @DisplayName("has 5 columns")
        void hasCorrectCols() {
            assertThat(world.getCols(), is(5));
        }
    }
}
