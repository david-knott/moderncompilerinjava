package jburg.burg;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AnnotationSemantics stores the semantic
 * information for a specific annotation
 * equivalence class; this corresponds to
 * the information encoded in the generated
 * JBurgAnnotation.
 */
public class AnnotationSemantics
{
    private final JBurgGenerator        generator;
    private final List<JBurgRule>       rules;
    private final ClosureGraph          closureGraph;
    private final boolean               fixedArity;
    private final int                   minimumArity;

    private final ProductionMap         patternMatches;
    private final ProductionMap         allProductions;
    //  Factored common subtrees of each pattern matcher.
    //  TODO: These can be further coalesced by using the right comparator.
    private final CommonSubtreeMap      commonSubtrees;
    
    AnnotationSemantics(JBurgGenerator generator, List<JBurgRule> rules)
    {
        this.generator      = generator;
        this.rules          = rules;
        this.closureGraph   = new ClosureGraph(generator);
        this.patternMatches = new ProductionMap();
        this.allProductions = new ProductionMap();
        this.commonSubtrees = new CommonSubtreeMap();

        boolean fixedArity  = true;
        int     minimumArity = Integer.MAX_VALUE;

        // First pass: add the pattern rules and the states
        // they produce to the annotation's semantic information.
        // Also compute the minimum arity and fixed-arity properties;
        // note that this was known to the caller and should be passed in
        // to make this logic simpler.
        for (JBurgRule rule: rules) {

            fixedArity  &= !rule.patternMatcher.hasNaryness();
            minimumArity = Math.min(minimumArity, rule.patternMatcher.getNominalArity());

            patternMatches.addToSet(rule.getGoalState(), rule);
            allProductions.addToSet(rule.getGoalState(), rule);
            closureGraph.addClosures(rule.getGoalState());

            for ( JBurgPatternMatcher subgoal: rule.patternMatcher.getParameterizedSubtrees() )
                subgoal.findFactors(commonSubtrees);
        }

        this.fixedArity     = fixedArity;
        this.minimumArity   = minimumArity;

        // Second pass: set the pattern matchers' "fixed arity context"
        // property, so the pattern matchers can elide arity checks.
        for (JBurgRule rule: rules) {

            if ( fixedArity )
                rule.patternMatcher.setFixedArityContext(true);
        }

        //  Add the closure graph to the productions.
        for ( Map.Entry<String, List<ClosureRecord>> closures: closureGraph.entrySet() ) {
            for ( ClosureRecord closure: closures.getValue() )
                allProductions.addToSet(closures.getKey(), closure);
        }
    }

    public List<JBurgRule> getRules()
    {
        return rules;
    }

    public ClosureGraph getClosureGraph()
    {
        return closureGraph;
    }

    public ProductionMap getPatternMatches()
    {
        return patternMatches;
    }

    public ProductionMap getAllProductions()
    {
        return allProductions;
    }

    public CommonSubtreeMap getCommonSubtrees()
    {
        return commonSubtrees;
    }

    /**
     * @return all the keys in the productions map,
     * i.e., all the nonterminal states this annotation
     * can produce.
     */
    public Set<String> getAllGoalStates()
    {
        return allProductions.keySet();
    }

    /**
     *  @return true if all rules in the equivalence class have fixed-arity operands.
     */
    public boolean hasFixedArity()
    {
        return this.fixedArity;
    }

    /**
     *  @return the minumum arity of the rules in the equivalence class.
     */
    public int getMinimumArity()
    {
        return this.minimumArity;
    }

    /**
     * @return true iff this annotation equivalence class could
     * be represented by a single annotation instance.
     */
    public boolean canBeSingleton()
    {
        boolean result = hasFixedArity() && getMinimumArity() == 0;

        if (result) {
            for (String nonterminal: getAllGoalStates()) {

                result &= findOptimalCost(nonterminal) != null;

                if (!result)
                    break;
            }
        }

        return result;
    }

    /**
     *  @return the name of the equivalence class in a format
     *  that can be used to name a JBurgAnnotation subclass.
     */
    public String getSpecializedClassName()
    {
        if ( hasFixedArity() )
            return String.format("%s_%s_%d", generator.config.getAnnotationInterfaceName(), rules.get(0).getOperator(), getMinimumArity());
        else
            return String.format("%s_%s_%d_n", generator.config.getAnnotationInterfaceName(), rules.get(0).getOperator(), getMinimumArity());
    }

    /**
     * Find the compiler-compile time optimal production
     * from this annotation's set of productions.
     * @param nonterminal the nonterminal of interest.
     * @return the optimal production, or null if it cannot be determined.
     */
    JBurgProduction findOptimalProduction(String nonterminal)
    {
        int bestCost = Integer.MAX_VALUE;
        JBurgProduction result = null;

        for ( JBurgProduction production: allProductions.get(nonterminal) ) {

            if ( production.computesConstantCost(allProductions) ) {

                int cost = production.getConstantCost(allProductions);

                if ( cost < bestCost ) {
                    result = production;
                    bestCost = cost;
                }
            } else {
                //  Can't be determined at compiler-compile time.
                result = null;
                break;
            }
        }

        return result;
    }

    /**
     * Find the compiler-compile time optimial cost for a given nonterminal
     * from this annotation's set of productions.
     * @param nonterminal the nonterminal of interest.
     * @return the optimal cost, or null if it cannot be determined.
     */
    Integer findOptimalCost(String nonterminal)
    {
        int bestCost = Integer.MAX_VALUE;
        Integer result = null;

        List<JBurgProduction> currentProductions = allProductions.get(nonterminal);

        for ( JBurgProduction production: currentProductions ) {

            if ( production.computesConstantCost(allProductions) ) {

                int cost = production.getConstantCost(allProductions);

                if ( cost < bestCost ) {
                    bestCost = cost;
                    result = bestCost;
                }
            } else {
                //  Can't be determined at compiler-compile time.
                result = null;
                break;
            }
        }

        return result;
    }
    
    
    /**
     * Productions keyed by the state they produce.
     */
    public static class ProductionMap  extends Multimap<String, JBurgProduction>
    {
    }

    public static class CommonSubtreeMap extends Multimap<JBurgPatternMatcher,JBurgPatternMatcher>
    {
    }
}
