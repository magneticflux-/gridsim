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

import org.skaggsm.gridsim.tile.Tile;

/**
 * @author Mitchell Skaggs
 */
public class World {

    private final Tile[][] tiles;
    private final int rows, cols;

    public World(int rows, int cols) {
        tiles = new Tile[rows][cols];
        for (Tile[] tiles : tiles)
            ArrayExtentionsKt.fill(tiles, () -> new Tile(1));

        this.rows = rows;
        this.cols = cols;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
