/*  Generated Mon Aug 10 18:17:40 IST 2020 by JBurg version 2.0.2  */

package Target.x64;

public class ./test  {
    java.util.Stack __reducedValues = new java.util.Stack();
final public static int __exp_NT = 1;
final public static int __stmt_NT = 2;
final public static int nStates = 2;
public  JBurgAnnotation label(Tree.IR to_be_labelled) {
    int             arity   = to_be_labelled.getArity();
    JBurgAnnotation result  = this.getJBurgAnnotation(to_be_labelled);

    for(int i = 0; i < arity; i++) {
        result.addChild(this.label(to_be_labelled.getNthChild(i)));
    }

    return result;
}

/* exp */

private  int action_1(Tree.IR __p) throws java.lang.Exception {
    int CALL = (int)__reducedValues.pop();
    		{	
    			return 0;
    		}
}

/* exp */

private  int action_2(Tree.IR __p) throws java.lang.Exception {
    int right = (int)__reducedValues.pop();int left = (int)__reducedValues.pop();
    		{
    			var binop = (BINOP)__p;
    			var cst = (CONST)__p.getNthChild(right);
    			var exp = (EXP)__p.getNthChild(left);
    		}
}

/* exp */

private  int action_3(Tree.IR __p) throws java.lang.Exception {
    int right = (int)__reducedValues.pop();int left = (int)__reducedValues.pop();
    		{
    			//emit code..
    		}
}

/* exp */

private  int action_4(Tree.IR __p) throws java.lang.Exception {
    int call = (int)__reducedValues.pop();
    		{
    			return 0;
    		}
}

/* stmt */

private  int action_5(Tree.IR __p) throws java.lang.Exception {
    int r2 = (int)__reducedValues.pop();int r2 = (int)__reducedValues.pop();int r1 = (int)__reducedValues.pop();int o1 = (int)__reducedValues.pop();
    		{
    			return 0;
    		}
}

/* stmt */

private  int action_6(Tree.IR __p) throws java.lang.Exception {
    int r2 = (int)__reducedValues.pop();int r1 = (int)__reducedValues.pop();
    		{
    			return 0;
    		}
}

/* exp */

private  int action_7(Tree.IR __p) throws java.lang.Exception {
    int r2 = (int)__reducedValues.pop();int r1 = (int)__reducedValues.pop();
    		{
    			return r;
    		}
}

/* exp */

private  int action_8(Tree.IR __p) throws java.lang.Exception {
    int rx2 = (int)__reducedValues.pop();int rx1 = (int)__reducedValues.pop();
    		{
    			retuen r;
    		}
}

private  void dispatchAction(JBurgAnnotation ___node,int iRule) throws java.lang.Exception {
     Tree.IR __p = ___node.getNode();
    switch(iRule) {
        case 1: {
            this.reduceAntecedent(___node,__CALL_NT);
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
        default: {
            throw new IllegalStateException("Unmatched reduce action " + iRule);
        }
    }
}


class JBurgAnnotation_BINOP_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_3ab39c39(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__Const_NT), this.getNthChild(0).getCost(__Exp_NT))));
    }
    private  int getCostForRule_2eee9593(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__Exp_NT), this.getNthChild(0).getCost(__Exp_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_exp = -1;
    public   JBurgAnnotation_BINOP_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = getCostForRule_3ab39c39(goalState);
                result = Math.min(result,getCostForRule_2eee9593(goalState));
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                if ((cachedCostFor_exp == -1)) {
                    cachedCostFor_exp = getPatternMatchCost(__exp_NT);
                } 
                result = cachedCostFor_exp;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_3ab39c39(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 2;
                } 
                currentCost = getCostForRule_2eee9593(goalState);
                if ((bestCost > currentCost)) {
                    rule = 3;
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



class JBurgAnnotation_EXP_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_7907ec20(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__CALL_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_exp = -1;
    public   JBurgAnnotation_EXP_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = getCostForRule_7907ec20(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                if ((cachedCostFor_exp == -1)) {
                    cachedCostFor_exp = getPatternMatchCost(__exp_NT);
                } 
                result = cachedCostFor_exp;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_7907ec20(goalState))) {
                    rule = 4;
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
        return (EXP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_EXP_1");
    }
}



class JBurgAnnotation_MOVE_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_546a03af(int goalState)  {
         JBurgAnnotation factoredPath_0 = this.getNthChild(0).getArity() > 0? this.getNthChild(0).getNthChild(0): errorAnnotation;
         JBurgAnnotation factoredPath_1 = this.getNthChild(0).getArity() > 1? this.getNthChild(0).getNthChild(1): errorAnnotation;
         JBurgAnnotation factoredPath_2 = this.getNthChild(0).getArity() > 2? this.getNthChild(0).getNthChild(2): errorAnnotation;
        if ((this.getNthChild(0).getArity() == 3) && (this.getNthChild(0).getOperator() == BINOP)) {
            return (normalizedAdd(1, normalizedAdd(normalizedAdd(normalizedAdd(this.getNthChild(1).getCost(__reg_NT), factoredPath_2.getCost(__reg_NT)), factoredPath_1.getCost(__reg_NT)), factoredPath_0.getCost(__op_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }
    private  int getCostForRule_721e0f4f(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__reg_NT), this.getNthChild(0).getCost(__reg_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_stmt = -1;
    public   JBurgAnnotation_MOVE_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                result = getCostForRule_546a03af(goalState);
                result = Math.min(result,getCostForRule_721e0f4f(goalState));
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __stmt_NT: {
                if ((cachedCostFor_stmt == -1)) {
                    cachedCostFor_stmt = getPatternMatchCost(__stmt_NT);
                } 
                result = cachedCostFor_stmt;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __stmt_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_546a03af(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 5;
                } 
                currentCost = getCostForRule_721e0f4f(goalState);
                if ((bestCost > currentCost)) {
                    rule = 6;
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



class JBurgAnnotation_PLUS_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_28864e92(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__reg_NT), this.getNthChild(0).getCost(__reg_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_exp = -1;
    public   JBurgAnnotation_PLUS_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = getCostForRule_28864e92(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                if ((cachedCostFor_exp == -1)) {
                    cachedCostFor_exp = getPatternMatchCost(__exp_NT);
                } 
                result = cachedCostFor_exp;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_28864e92(goalState))) {
                    rule = 7;
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
        return (PLUS);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_PLUS_2");
    }
}



class JBurgAnnotation_XXX_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_6ea6d14e(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__regx_NT), this.getNthChild(0).getCost(__regx_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_exp = -1;
    public   JBurgAnnotation_XXX_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                result = getCostForRule_6ea6d14e(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __exp_NT: {
                if ((cachedCostFor_exp == -1)) {
                    cachedCostFor_exp = getPatternMatchCost(__exp_NT);
                } 
                result = cachedCostFor_exp;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __exp_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_6ea6d14e(goalState))) {
                    rule = 8;
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
        return (XXX);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_XXX_2");
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
        case EXP: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_EXP_1(node));
            } 
        }
        break;
        case MOVE: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_MOVE_2(node));
            } 
        }
        break;
        case PLUS: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_PLUS_2(node));
            } 
        }
        break;
        case XXX: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_XXX_2(node));
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
    null,
    {
        new JBurgSubgoal(__Exp_NT,false, 0,0),
        new JBurgSubgoal(__Const_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__Exp_NT,false, 0,0),
        new JBurgSubgoal(__Exp_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__CALL_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__op_NT,false, 0,0,0),
        new JBurgSubgoal(__reg_NT,false, 0,0,1),
        new JBurgSubgoal(__reg_NT,false, 0,0,2),
        new JBurgSubgoal(__reg_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__reg_NT,false, 0,0),
        new JBurgSubgoal(__reg_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__reg_NT,false, 0,0),
        new JBurgSubgoal(__reg_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__regx_NT,false, 0,0),
        new JBurgSubgoal(__regx_NT,false, 0,1)
    }
};

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

		reduce(annotatedTree, goal_state);
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
