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
import org.skaggsm.gridsim.subsystem.TemperatureSubsystem;

import java.util.Collections;

/**
 * @author Mitchell Skaggs
 */
@DisplayName("An Engine")
class EngineTest {
    private Engine engine;
    private World world;

    @Nested
    @DisplayName("when new")
    class WhenNew {
        @BeforeEach
        void createNewEngine() {
            world = new World(5, 5);
            engine = new Engine(world, Collections.singletonList(new TemperatureSubsystem()));
        }

        @Test
        @DisplayName("can tick")
        void canTick() {
            engine.tick();
        }

        @DisplayName("when tick processed")
        class WhenTickProcessed {
            @BeforeEach
            void tickEngine() {
                engine.tick();
            }
        }
    }
}
