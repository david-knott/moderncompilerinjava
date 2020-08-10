package Tile;

public class MatchThing {
    public final Rule rule;
    public final Match match;
    public MatchThing(Rule rule, Match match, int cost) {
        this.rule = rule;
        this.match = match;
    }
}