package Tile;



public class Rule implements Comparable<Rule> {
    final public TilePattern tilePattern;
    final public int cost;
    final public Action action;

    public Rule(TilePattern tilePattern, int cost, Action action) {
        this.tilePattern = tilePattern;
        this.cost = cost;
        this.action = action;
    }

    @Override
    public int compareTo(Rule o) {
        return this.hashCode() == o.hashCode() ? 0 : ( this.hashCode() > o.hashCode() ? -1 : 1);
    }
}