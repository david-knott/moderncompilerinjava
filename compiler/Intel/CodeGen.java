/*  Generated Tue Aug 18 09:26:53 IST 2020 by JBurg version 2.0.2  */

package Intel;
	import Tree.*;
	import static Tree.TreeKind.*;
public class CodeGen  {
    java.util.Stack __reducedValues = new java.util.Stack();
	private Reducer reducer;
	public void setReducer(Reducer reducer) {
		this.reducer = reducer;
	}

	private Emitter emitter;
	public void setEmitter(Emitter emitter) {
		this.emitter = emitter;
	}
final public static int __indirectWithDisplacementAndScale_NT = 1;
final public static int __move_NT = 2;
final public static int __temp_NT = 3;
final public static int __indirect_NT = 4;
final public static int __indirectWithDisplacement_NT = 5;
final public static int __binopOffsetExpression_NT = 6;
final public static int __integerConstant_NT = 7;
final public static int __exp_NT = 8;
final public static int __stmt_NT = 9;
final public static int nStates = 9;
public  JBurgAnnotation label(Tree.IR to_be_labelled) {
    int             arity   = to_be_labelled.getArity();
    JBurgAnnotation result  = this.getJBurgAnnotation(to_be_labelled);

    for(int i = 0; i < arity; i++) {
        result.addChild(this.label(to_be_labelled.getNthChild(i)));
    }

    return result;
}

/* binopOffsetExpression */

private  BinopOffsetExpression action_1(Tree.IR __p) throws java.lang.Exception {
    IntegerConstantExpression arg1 = (IntegerConstantExpression)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.binopOffsetExpression(__p,arg0,arg1));
}

/* indirectWithDisplacementAndScale */

private  IndirectWithDisplacementAndScaleExpression action_2(Tree.IR __p) throws java.lang.Exception {
    BinopOffsetExpression arg1 = (BinopOffsetExpression)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.indirectWithDisplacementAndScale(__p,arg0,arg1));
}

/* indirectWithDisplacement */

private  IndirectWithDisplacementExpression action_3(Tree.IR __p) throws java.lang.Exception {
    BinopOffsetExpression arg0 = (BinopOffsetExpression)__reducedValues.pop();
    		return (reducer.indirectWithDisplacement(__p,arg0));
}

/* indirect */

private  IndirectExpression action_4(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp arg = (Temp.Temp)__reducedValues.pop();
    		return (reducer.indirect(__p,arg));
}

/* move */

private  Temp.Temp action_5(Tree.IR __p) throws java.lang.Exception {
    IndirectExpression arg1 = (IndirectExpression)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.loadindirect(__p,arg0,arg1));
}

/* move */

private  Temp.Temp action_6(Tree.IR __p) throws java.lang.Exception {
    IndirectWithDisplacementExpression arg1 = (IndirectWithDisplacementExpression)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.loadindirectWithDisplacement(__p,arg0,arg1));
}

/* move */

private  Temp.Temp action_7(Tree.IR __p) throws java.lang.Exception {
    IndirectWithDisplacementAndScaleExpression arg1 = (IndirectWithDisplacementAndScaleExpression)__reducedValues.pop();Temp.Temp arg0 = (Temp.Temp)__reducedValues.pop();
    		return (reducer.loadindirectWithDisplacementAndScale(__p,arg0,arg1));
}

/* temp */

private  Temp.Temp action_8(Tree.IR __p) throws java.lang.Exception {

    		return (reducer.temp(__p));
}

/* integerConstant */

private  IntegerConstantExpression action_9(Tree.IR __p) throws java.lang.Exception {

    		return (reducer.integerConstant(__p));
}

/* exp */

private  Temp.Temp action_10(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* exp */

private  Temp.Temp action_11(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* stmt */

private  Temp.Temp action_12(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

private  void dispatchAction(JBurgAnnotation ___node,int iRule) throws java.lang.Exception {
     Tree.IR __p = ___node.getNode();
    switch(iRule) {
        case 1: {
            __reducedValues.push(this.action_1(__p));
        }
        break;
        case 2: {
            __reducedValues.push(this.action_2(__p));
        }
        break;
        case 3: {
            __reducedValues.push(this.action_3(__p));
        }
        break;
        case 4: {
            __reducedValues.push(this.action_4(__p));
        }
        break;
        case 5: {
            __reducedValues.push(this.action_5(__p));
        }
        break;
        case 6: {
            __reducedValues.push(this.action_6(__p));
        }
        break;
        case 7: {
            __reducedValues.push(this.action_7(__p));
        }
        break;
        case 8: {
            __reducedValues.push(this.action_8(__p));
        }
        break;
        case 9: {
            __reducedValues.push(this.action_9(__p));
        }
        break;
        case 10: {
            this.reduceAntecedent(___node,__integerConstant_NT);
            __reducedValues.push(this.action_10(__p));
        }
        break;
        case 11: {
            this.reduceAntecedent(___node,__temp_NT);
            __reducedValues.push(this.action_11(__p));
        }
        break;
        case 12: {
            this.reduceAntecedent(___node,__move_NT);
            __reducedValues.push(this.action_12(__p));
        }
        break;
        default: {
            throw new IllegalStateException("Unmatched reduce action " + iRule);
        }
    }
}


class JBurgAnnotation_BINOP_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_1fbc7afb(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__integerConstant_NT), this.getNthChild(0).getCost(__exp_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_binopOffsetExpression = -1;
    public   JBurgAnnotation_BINOP_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __binopOffsetExpression_NT: {
                result = getCostForRule_1fbc7afb(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __binopOffsetExpression_NT: {
                if ((cachedCostFor_binopOffsetExpression == -1)) {
                    cachedCostFor_binopOffsetExpression = getPatternMatchCost(__binopOffsetExpression_NT);
                } 
                result = cachedCostFor_binopOffsetExpression;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __binopOffsetExpression_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_1fbc7afb(goalState))) {
                    rule = 1;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (2);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            case 1: {
                result = subtree1;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else if ( subtree1 == null ) {
            subtree1 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (BINOP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_BINOP_2");
    }
}



class JBurgAnnotation_CONST_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_CONST_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __integerConstant_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_integerConstant = this.getPatternMatchCost(__integerConstant_NT);
                 int cost_exp = cost_integerConstant;
                result = cost_exp;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = 1;
            }
            break;
            case __integerConstant_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                rule = 10;
            }
            break;
            case __integerConstant_NT: {
                rule = 9;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (CONST);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_CONST_0");
    }
}



class JBurgAnnotation_MEM_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_4cdbe50f(int goalState)  {
         JBurgAnnotation factoredPath_0 = this.getNthChild(0).getArity() > 0? this.getNthChild(0).getNthChild(0): errorAnnotation;
         JBurgAnnotation factoredPath_1 = this.getNthChild(0).getArity() > 1? this.getNthChild(0).getNthChild(1): errorAnnotation;
        if ((this.getNthChild(0).getArity() == 2) && (this.getNthChild(0).getOperator() == BINOP)) {
            return (normalizedAdd(1, normalizedAdd(factoredPath_1.getCost(__binopOffsetExpression_NT), factoredPath_0.getCost(__exp_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }
    private  int getCostForRule_66d33a(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__binopOffsetExpression_NT)));
    }
    private  int getCostForRule_7cf10a6f(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__exp_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_indirectWithDisplacementAndScale = -1;private  int cachedCostFor_indirect = -1;private  int cachedCostFor_indirectWithDisplacement = -1;
    public   JBurgAnnotation_MEM_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __indirect_NT: {
                result = getCostForRule_7cf10a6f(goalState);
            }
            break;
            case __indirectWithDisplacement_NT: {
                result = getCostForRule_66d33a(goalState);
            }
            break;
            case __indirectWithDisplacementAndScale_NT: {
                result = getCostForRule_4cdbe50f(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __indirect_NT: {
                if ((cachedCostFor_indirect == -1)) {
                    cachedCostFor_indirect = getPatternMatchCost(__indirect_NT);
                } 
                result = cachedCostFor_indirect;
            }
            break;
            case __indirectWithDisplacement_NT: {
                if ((cachedCostFor_indirectWithDisplacement == -1)) {
                    cachedCostFor_indirectWithDisplacement = getPatternMatchCost(__indirectWithDisplacement_NT);
                } 
                result = cachedCostFor_indirectWithDisplacement;
            }
            break;
            case __indirectWithDisplacementAndScale_NT: {
                if ((cachedCostFor_indirectWithDisplacementAndScale == -1)) {
                    cachedCostFor_indirectWithDisplacementAndScale = getPatternMatchCost(__indirectWithDisplacementAndScale_NT);
                } 
                result = cachedCostFor_indirectWithDisplacementAndScale;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __indirect_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_7cf10a6f(goalState))) {
                    rule = 4;
                } 
            }
            break;
            case __indirectWithDisplacement_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_66d33a(goalState))) {
                    rule = 3;
                } 
            }
            break;
            case __indirectWithDisplacementAndScale_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_4cdbe50f(goalState))) {
                    rule = 2;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (1);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (MEM);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_MEM_1");
    }
}



class JBurgAnnotation_MOVE_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_7e0babb1(int goalState)  {
        return (normalizedAdd(10, normalizedAdd(this.getNthChild(1).getCost(__indirect_NT), this.getNthChild(0).getCost(__exp_NT))));
    }
    private  int getCostForRule_6debcae2(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__indirectWithDisplacement_NT), this.getNthChild(0).getCost(__exp_NT))));
    }
    private  int getCostForRule_5ba23b66(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__indirectWithDisplacementAndScale_NT), this.getNthChild(0).getCost(__exp_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_move = -1;
    public   JBurgAnnotation_MOVE_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __move_NT: {
                result = getCostForRule_7e0babb1(goalState);
                result = Math.min(result,getCostForRule_6debcae2(goalState));
                result = Math.min(result,getCostForRule_5ba23b66(goalState));
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                 int cost_move = this.getPatternMatchCost(__move_NT);
                 int cost_stmt = cost_move;
                result = cost_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __move_NT: {
                if ((cachedCostFor_move == -1)) {
                    cachedCostFor_move = getPatternMatchCost(__move_NT);
                } 
                result = cachedCostFor_move;
            }
            break;
            case __stmt_NT: {
                result = getClosureCost(__stmt_NT);
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __move_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_7e0babb1(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 5;
                } 
                currentCost = getCostForRule_6debcae2(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 6;
                } 
                currentCost = getCostForRule_5ba23b66(goalState);
                if ((bestCost > currentCost)) {
                    rule = 7;
                } 
            }
            break;
            case __stmt_NT: {
                if ((Integer.MAX_VALUE > getCost(__move_NT))) {
                    rule = 12;
                } 
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (2);
    }
    public  JBurgAnnotation getNthChild(int index)  {
         JBurgAnnotation result = null;
        switch(index) {
            case 0: {
                result = subtree0;
            }
            break;
            case 1: {
                result = subtree1;
            }
            break;
            default: {
                throw new IllegalStateException("Invalid index " + index);
            }
        }
        return (result);
    }
    public  void addChild(JBurgAnnotation child)  {
        if (subtree0 == null) {
            subtree0 = child;
        } else if ( subtree1 == null ) {
            subtree1 = child;
        } else  {
            throw new IllegalStateException("too many children");
        } 
    }
    public  int getOperator()  {
        return (MOVE);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_MOVE_2");
    }
}



class JBurgAnnotation_TEMP_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_TEMP_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __temp_NT: {
                result = 10;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                 int cost_temp = this.getPatternMatchCost(__temp_NT);
                 int cost_exp = cost_temp;
                result = cost_exp;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = 10;
            }
            break;
            case __temp_NT: {
                result = 10;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                rule = 11;
            }
            break;
            case __temp_NT: {
                rule = 8;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (TEMP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_TEMP_0");
    }
}


public  JBurgAnnotation getJBurgAnnotation(Tree.IR node)  {
    switch(node.getOperator()) {
        case BINOP: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_BINOP_2(node));
            } 
        }
        break;
        case CONST: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_CONST_0(node));
            } 
        }
        break;
        case MEM: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_MEM_1(node));
            } 
        }
        break;
        case MOVE: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_MOVE_2(node));
            } 
        }
        break;
        case TEMP: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_TEMP_0(node));
            } 
        }
        break;
        default: {
        }
        break;
    }
    return (new PlaceholderAnnotation(node,node.getArity()));
}

private static final JBurgSubgoal[][] ___subgoals_by_rule = 
{
    null,
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__integerConstant_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0,0),
        new JBurgSubgoal(__binopOffsetExpression_NT,false, 0,0,1)
    },
    {
        new JBurgSubgoal(__binopOffsetExpression_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__indirect_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__indirectWithDisplacement_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__exp_NT,false, 0,0),
        new JBurgSubgoal(__indirectWithDisplacementAndScale_NT,false, 0,1)
    },
    null,
    null,
    null,
    null,
    null
};
final public static int ERROR_TRAP = 268435456;

    /**
     * Reduce a labeled subtree.
     * @param p the annotation describing the root of the subtree.
     * @param goalState the required goal state.
     */
    public void reduce(JBurgAnnotation p , int goalState) throws java.lang.Exception
	{
		reduceAntecedent(p,goalState);
		// Release the annotation's data.
		p.release();
	}

    public void reduceAntecedent(JBurgAnnotation p , int goalState) throws java.lang.Exception
	{
        int iRule  = -1;

		if ( ( goalState != 0 )  ) {
            iRule = p.getRule(goalState);
		} else {
			// Find the minimum-cost path.
            int minCost  = Integer.MAX_VALUE;

            for(int i = 0; i <= nStates; i++)
			{
				if ( ( minCost > p.getCost(i) )  ) {
                    iRule = p.getRule(i);
                    minCost = p.getCost(i);
                    goalState = i;
				}
			}
		}

		if ( ( iRule > 0 )  ) {
			reduceSubgoals(p, iRule);
			dispatchAction(p, iRule );
		} else {
			throw new IllegalStateException ( String.format("Unable to find a rule to process \"%s\", operator=%s, goal=%s", p, p.getOperator(), goalState) );
		}
	}

    private void reduceSubgoals(JBurgAnnotation p ,int rule_num) throws java.lang.Exception
	{
		if ( ___subgoals_by_rule[rule_num] != null )
		{
			for ( JBurgSubgoal sg : ___subgoals_by_rule[rule_num] )
			{
				if ( !sg.isNary() )
				{
					reduce ( sg.getNode(p), sg.getGoalState());
				}
				else
				{
					// Aggregate the operands of an n-ary operator into a single container.
					JBurgAnnotation sub_parent = sg.getNode(p);
                    int arity = sub_parent.getArity();
					java.util.Vector<Object> variadic_result = new java.util.Vector<Object>(arity - sg.startIndex);

					for ( int i = sg.startIndex; i < arity; i++ )
					{
						reduce(sub_parent.getNthChild(i), sg.getGoalState());
						variadic_result.add(__reducedValues.pop());
					}
					__reducedValues.push(variadic_result);
				}
			}
		}
	}

    /**
     * Get the cost of an n-ary tail of candidate reductions.
     * @param Node the parent of the candidates.
     * @param goalState the desired goal state.
     * @param startIndex the starting position of
     * the n-ary tail in the parent's children.
     * @return the sum of the n-ary children's costs
     * for the desired goal state, capped at 
     * Integer.MAX_VALUE - 1 if all child costs
     * were strictly less than Integer.MAX_VALUE.
     */
    private int getNaryCost(JBurgAnnotation p , int goalState, int startIndex)
	{
		long accumCost = 0;

		for ( int i = startIndex; accumCost < Integer.MAX_VALUE && i < p.getArity(); i++ )
        {
            // Don't allow the cost of a series of feasible child reductions
            // to rise above MAX_VALUE; the concept of "feasability" and the
            // concept of "cost" need to be disconnected.
            int  childCost = p.getNthChild(i).getCost(goalState);

            if ( childCost >= Integer.MAX_VALUE )
                return Integer.MAX_VALUE;
            else
                accumCost += childCost;
        }

		return accumCost < Integer.MAX_VALUE? (int)accumCost: Integer.MAX_VALUE - 1;
	}
	
    /**
     * Reduce a subtree to the least-cost solution available.
     * @param root the root of the subtree.
     */
    public void burm(Tree.IR root) throws java.lang.Exception
	{
		/* Use the least-cost goal state available. */
		burm(root, 0);
	}
	
    /**
     * Reduce a subtree to a specific goal state.
     * @param root the root of the subtree.
     * @param goal_state the desired goal.
     */
    public void burm(Tree.IR root, int goal_state) throws java.lang.Exception
	{
		JBurgAnnotation annotatedTree = label(root);

        try
        {
            reduce(annotatedTree, goal_state);
        }
        catch ( Exception cant_reduce )
        {
            this.__problemTree = annotatedTree;
            throw cant_reduce;
        }

	}
	
    /**
     * Get the (presumably only) final
     * result of a series of reductions.
     */
    public Object getResult()
	{
		return __reducedValues.pop();
	}

    /**
     * @return the sum of the input costs, normalized as follows:
     * <li>If either parameter equals Integer.MAX_VALUE, return Integer.MAX_VALUE.
     * <li>If the sum is less than Integer.MAX_VALUE, return the sum.
     * <li>Otherwise return Integer.MAX_VALUE-1.
     */
    public int normalizedAdd(int c1, int c2)
    {
        int result = Integer.MAX_VALUE;

        if ( c1 < Integer.MAX_VALUE && c2 < Integer.MAX_VALUE )
        {
            long accum = (long)c1 + (long)c2;
            result = accum < Integer.MAX_VALUE?
                (int)accum:
                Integer.MAX_VALUE-1;
        }

        return result;
    }

	private JBurgAnnotation __problemTree;
	
    public void dump(java.io.PrintWriter debug_output)
	{
		if ( null == __problemTree )
		{
			debug_output.print("<bailed reason=\"no problem tree\"/>");
			
			return;
		}
		debug_output.print("<jburg>");
		dumpSubgoals(debug_output);
		debug_output.print("<label>");
		describeNode(__problemTree,debug_output);
		debug_output.print("</label></jburg>");
	}
	
    public void dump(Tree.IR node,java.io.PrintWriter debug_output)
	{
		JBurgAnnotation anno = label(node);
		debug_output.println("<?xml version=\"1.0\"?>");
		debug_output.println("<BurmDump date=\"" + new java.util.Date() + "\">");
		debug_output.println("<jburg>");
		dumpSubgoals(debug_output);
		debug_output.println("<label>");
		describeNode(anno,debug_output);
		debug_output.println("</label>\n</jburg>\n</BurmDump>");
		debug_output.flush();
	
	}

    public void describeNode(JBurgAnnotation node,java.io.PrintWriter debug_output)
	{
		if ( node == null )
            return;

		String self_description = node.getSelfDescription();
		try {
			self_description = java.net.URLEncoder.encode(self_description,"UTF-8");
		
		} catch ( Exception cant_encode ) {
            // Just use the description as given.
		}

		debug_output.printf("<node operator=\"%d\" selfDescription=\"%s\"/>", node.getOperator(), self_description);

		for(int i = 0; i <= nStates; i++) {

			if ( node.getCost(i) < Integer.MAX_VALUE ) {

				debug_output.print ( "<goal");
				debug_output.print ( " name=\"" + stateName[i] + "\"");
				debug_output.print ( " rule=\"" + node.getRule(i) + "\"");
				debug_output.print ( " cost=\"" + node.getCost(i) + "\"");
				debug_output.println ( "/>" );
			}
		}

		for (int i = 0; i < node.getArity(); i++ )
            describeNode ( node.getNthChild(i),debug_output);
		debug_output.println ( "</node>" );
	
	}

    private void dumpSubgoals(java.io.PrintWriter debug_output)
	{
		for ( int rule_num = 0; rule_num < ___subgoals_by_rule.length; rule_num++ )
        {
            if ( ___subgoals_by_rule[rule_num] != null )
            {
                debug_output.printf("<subgoals rule=\"%d\">\n", rule_num);

                for ( JBurgSubgoal sg : ___subgoals_by_rule[rule_num] )
                {
                    debug_output.printf("<subgoal goal=\"%s\" nary=\"%s\" startIndex=\"%s\"", stateName[sg.getGoalState()], sg.isNary(), sg.startIndex);

                    if ( sg.accessPath.length > 0 )
                    {
                        debug_output.println(">");
                        for ( int idx: sg.accessPath )
                        debug_output.printf("<accessPath index=\"%d\"/>\n", idx);
                        debug_output.printf("</subgoal>\n");
                    }
                    else
                    {
                        debug_output.println("/>");
                    }
                }
                debug_output.printf("</subgoals>\n");
            }
		}
	}

    static final String[] stateName = new String[] { null, "indirectWithDisplacementAndScale","move","temp","indirect","indirectWithDisplacement","binopOffsetExpression","integerConstant","exp","stmt" };

    	/** 
         *  JBurgAnnotation is a data structure internal to the
    	 *  JBurg-generated BURM that annotates a Tree.IR with
    	 *  information used for dynamic programming and reduction.
    	 */
    	public abstract static class JBurgAnnotation
    	{
    		/**  The Tree.IR this JBurgAnnotation describes.  */
    		Tree.IR m_node; 
    		JBurgAnnotation ( Tree.IR newNode)
    		{
    			m_node = newNode;
    		}

    		/** @return this node's wrapped Tree.IR. */ 
    		public Tree.IR getNode()  
    		{
    			return m_node; 
    		}

    		/** @return this node's operator. */
    		public int getOperator() 
    		{
    			return m_node.getOperator();
    		}

            public String getSelfDescription()
            {
                return "?";
            }

    		/** @return the nth child of this node.  */
    		public abstract JBurgAnnotation getNthChild(int idx);

    		/** @return this node's child count.  */
    		public abstract int getArity();

    		/** Add a new child to this node.  */
    		public abstract void addChild(JBurgAnnotation new_child);

    		/** Release this node's data.  */
    		public abstract void release();

    		/** @return the wrapped node's toString().  */
    		public String toString() 
    		{
    			return m_node.toString(); 
    		}

    		/** @return the current best cost to reach a goal state.  */
    		public abstract int getCost( int goalState ) ;

    		/** * @return the rule to fire for a specific goal state. */
    		public abstract int getRule ( int goalState ) ;

    		/**
    		 *  A closure's transformation rule succeeded.
    		 *  If this path is selected for reduction, then all the actions  must be run in sequence, beginning with the original;
    		 *  so the order of the rules matters.  We disallow transformation rules with  cycles (a node should never 
    		 *  transition back to a goal state that has already been reduced).*/
    		public abstract void recordAntecedent ( int iGoalState, int newAntecedentState );
    	
    	}

    /**
     * Common superclass of node-specific annotations.
     */
	abstract static class JBurgSpecializedAnnotation extends JBurgAnnotation
	{
		JBurgSpecializedAnnotation(Tree.IR node)
		{
			super(node);
		}

		public JBurgAnnotation getNthChild(int idx)
		{
			throw new IllegalStateException(this.getClass().getName() + " has no children.");
		}

		public void addChild(JBurgAnnotation new_child)
		{
			throw new IllegalStateException(this.getClass().getName() + " cannot have children.");
		}

		public void release ()
		{
		}

		public void recordAntecedent ( int iGoalState, int newAntecedentState )
		{
			throw new IllegalStateException(this.getClass().getName() + " cannot record antecedents.");
		}
	}

    /**
     * An annotation that denotes an unused node in the tree.
     */
	private static class PlaceholderAnnotation extends JBurgSpecializedAnnotation
	{
		PlaceholderAnnotation(Tree.IR node, int capacity)
		{
			super(node);
            this.children = new JBurgAnnotation[capacity];
		}

        JBurgAnnotation [] children;
        int actualChildCount = 0;

		public int getCost(int state)     { return Integer.MAX_VALUE; }
		public int getRule(int state)     { return -1; }
		public int getArity()                           { return actualChildCount; }

		public JBurgAnnotation getNthChild(int idx)     { return children[idx]; }
		public void addChild(JBurgAnnotation newChild)  { children[actualChildCount++] = newChild; }
	}

    /**
     * An annotation that describes a pattern-match failure.
     */
    private final static JBurgAnnotation errorAnnotation = new JBurgSpecializedAnnotation(null)
    {
		public int getRule(int state) { return -1; }
		public int getCost(int state) { return Integer.MAX_VALUE; }
		public int getArity()                       { return 0; }
    };

    /**
     * JBurgSubgoal describes a possible reduction of a subtree.
     */
	static class JBurgSubgoal
	{
        /** The goal state that this subgoal produces. */
		private int goalState;
        /** Is this subgoal an n-ary tail? */
		private boolean isNary;
        /** If this is an n-ary subgoal, where does it start? */
		private int startIndex ;
        /** Path from the ancestor node to the root of the subtree. */
		private int[] accessPath ;

        public  JBurgSubgoal(int goal_state,boolean is_nary,int start_index,int... access_path)
		{
			this.goalState = goal_state;
			this.isNary = is_nary;
			this.startIndex = start_index;
			this.accessPath = access_path;
		}
		public int getGoalState() { return this.goalState; }
		public boolean isNary() { return this.isNary; }
        /**
         * Traverse the access path from the ancestor subtree
         * to the root of this subgoal's subtree.
         * @param ancestor the ancestor node.
         */
		public JBurgAnnotation getNode(JBurgAnnotation ancestor)
		{
			JBurgAnnotation result = ancestor;
			for ( int idx: this.accessPath )
				result = result.getNthChild(idx);
			return result;
		}
	
	}
}
