package Tile;

abstract class TilePattern {

    public String name;

    abstract void accept(TilePatternVisitor patternVisitor);
}