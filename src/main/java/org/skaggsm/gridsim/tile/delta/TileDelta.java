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

package org.skaggsm.gridsim.tile.delta;

import org.jetbrains.annotations.NotNull;
import org.skaggsm.gridsim.World;
import org.skaggsm.gridsim.tile.Tile;

/**
 * @author Mitchell Skaggs
 */
public abstract class TileDelta {
    protected final int row, col;

    public TileDelta(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void apply(World world) {
        apply(getCorrespondingTile(world));
    }

    public abstract void apply(Tile tile);

    public Tile getCorrespondingTile(@NotNull World world) {
        return world.getTile(getRow(), getCol());
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
