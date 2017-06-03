package org.skaggsm.gridsim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * @author Mitchell Skaggs
 */
@DisplayName("A World")
class WorldTest {
    private World world;

    @Test
    @DisplayName("is instantiated with new World()")
    void isInstantiatedWithNew() {
        new World();
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {
        @BeforeEach
        void createNewWorld() {
            world = new World();
        }

        @Test
        void isAWorld() {
            assertThat(world, instanceOf(World.class));
        }
    }
}
