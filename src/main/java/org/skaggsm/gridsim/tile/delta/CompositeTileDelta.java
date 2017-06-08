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

import org.skaggsm.gridsim.tile.Tile;

/**
 * @author Mitchell Skaggs
 */
public class CompositeTileDelta extends TileDelta {
    private final TileDelta tileDelta1;
    private final TileDelta tileDelta2;

    public CompositeTileDelta(TileDelta tileDelta1, TileDelta tileDelta2) {
        super(tileDelta1.row, tileDelta1.col);
        checkEqualRowAndCol(tileDelta1, tileDelta2);

        this.tileDelta1 = tileDelta1;
        this.tileDelta2 = tileDelta2;
    }

    private void checkEqualRowAndCol(TileDelta tileDelta1, TileDelta tileDelta2) {
        if (tileDelta1.row != tileDelta2.row)
            throw new IllegalArgumentException("Rows of composite TileDeltas must be the same!");
        if (tileDelta1.col != tileDelta2.col)
            throw new IllegalArgumentException("Cols of composite TileDeltas must be the same!");
    }

    @Override
    public void apply(Tile tile) {
        tileDelta1.apply(tile);
        tileDelta2.apply(tile);
    }
}
