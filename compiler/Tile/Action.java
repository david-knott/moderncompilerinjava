package Tile;

import Temp.Temp;

@FunctionalInterface
interface Action {

    public Temp single(TilePatternMatcher tilePatternMatcher);

}