/*  Generated Tue Aug 11 09:11:43 IST 2020 by JBurg version 2.0.2  */

package Intel;
	import Tree.*;
	import static Tree.TreeKind.*;
public class CodeGen  {
    java.util.Stack __reducedValues = new java.util.Stack();
	private Emitter emitter;
	public void setEmitter(Emitter emitter) {
		this.emitter = emitter;
	}
final public static int __jumpType_NT = 1;
final public static int __sxpType_NT = 2;
final public static int __memType_NT = 3;
final public static int __labelType_NT = 4;
final public static int __moveType_NT = 5;
final public static int __nameType_NT = 6;
final public static int __moveType2_NT = 7;
final public static int __constType_NT = 8;
final public static int __cjumpType_NT = 9;
final public static int __binopType_NT = 10;
final public static int __expType_NT = 11;
final public static int __tempType_NT = 12;
final public static int __moveType3_NT = 13;
final public static int nStates = 13;
public  JBurgAnnotation label(Tree.IR to_be_labelled) {
    int             arity   = to_be_labelled.getArity();
    JBurgAnnotation result  = this.getJBurgAnnotation(to_be_labelled);

    for(int i = 0; i < arity; i++) {
        result.addChild(this.label(to_be_labelled.getNthChild(i)));
    }

    return result;
}

/* memType */

private  Temp.Temp action_1(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			Temp.Temp temp = Temp.Temp.create();
    			return temp;
    		}
}

/* binopType */

private  Temp.Temp action_2(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t2 = (Temp.Temp)__reducedValues.pop();Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			return null;
    		}
}

/* sxpType */

private  Temp.Temp action_3(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t = (Temp.Temp)__reducedValues.pop();
    		{
    			return null;
    		}
}

/* cjumpType */

private  Temp.Temp action_4(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t2 = (Temp.Temp)__reducedValues.pop();Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			return null;
    		}
}

/* jumpType */

private  Temp.Temp action_5(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp exp = (Temp.Temp)__reducedValues.pop();
    		{
    			return null;
    		}
}

/* constType */

private  Temp.Temp action_6(Tree.IR __p) throws java.lang.Exception {

    		{
    			Temp.Temp temp = Temp.Temp.create();
    			int intValue = ((Tree.CONST)__p).value;
    			emitter.emit();
    			System.out.println("Const");
    		    //emit(new OPER("movq $" + cnst.value + ", %`d0 # const", L(temp, null), null));
    			return temp;
    		}
}

/* labelType */

private  Temp.Temp action_7(Tree.IR __p) throws java.lang.Exception {

    		{
    			Tree.LABEL label = ((Tree.LABEL)__p);
    			//emit(new Assem.LABEL(op.label.toString() + ":", op.label));
    			System.out.println("Label");
    			return null;
    		}
}

/* nameType */

private  Temp.Temp action_8(Tree.IR __p) throws java.lang.Exception {

    		{
    			Temp.Temp temp = Temp.Temp.create();
    			Tree.NAME name = ((Tree.NAME)__p);
    			System.out.println("Name");
    			//emit(new Assem.OPER("movq $" + op.label + ", %`d0 # default name", L(temp, null), null));
    			return temp;
    		}
}

/* tempType */

private  Temp.Temp action_9(Tree.IR __p) throws java.lang.Exception {

    		{
    			System.out.println("Temp");
    			return ((Tree.TEMP)__p).temp;
    		}
}

/* moveType */

private  Temp.Temp action_10(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t2 = (Temp.Temp)__reducedValues.pop();Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			System.out.println("Move1");
    			//emit(new Assem.MOVE("movq %`s0, %`d0 # load 0", t1, t2));
    			return null;
    		}
}

/* moveType2 */

private  Temp.Temp action_11(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t2 = (Temp.Temp)__reducedValues.pop();Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			System.out.println("Move2");
    			//emit(new Assem.OPER("movq %`s0, (%`s1) # store", null, new TempList(srcTemp, new TempList(dstTemp))));
    			return null;
    		}
}

/* moveType3 */

private  Temp.Temp action_12(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t2 = (Temp.Temp)__reducedValues.pop();Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			System.out.println("Move3");
    			return null;
    		}
}

/* moveType3 */

private  Temp.Temp action_13(Tree.IR __p) throws java.lang.Exception {
    Temp.Temp t2 = (Temp.Temp)__reducedValues.pop();Temp.Temp t1 = (Temp.Temp)__reducedValues.pop();
    		{
    			System.out.println("Move4");
    			return null;
    		}
}

/* expType */

private  Temp.Temp action_14(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* expType */

private  Temp.Temp action_15(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* expType */

private  Temp.Temp action_16(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* expType */

private  Temp.Temp action_17(Tree.IR __p) throws java.lang.Exception {

    		return ((Temp.Temp)__reducedValues.pop());
}

/* expType */

private  Temp.Temp action_18(Tree.IR __p) throws java.lang.Exception {

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
            __reducedValues.push(this.action_10(__p));
        }
        break;
        case 11: {
            __reducedValues.push(this.action_11(__p));
        }
        break;
        case 12: {
            __reducedValues.push(this.action_12(__p));
        }
        break;
        case 13: {
            __reducedValues.push(this.action_13(__p));
        }
        break;
        case 14: {
            this.reduceAntecedent(___node,__memType_NT);
            __reducedValues.push(this.action_14(__p));
        }
        break;
        case 15: {
            this.reduceAntecedent(___node,__labelType_NT);
            __reducedValues.push(this.action_15(__p));
        }
        break;
        case 16: {
            this.reduceAntecedent(___node,__binopType_NT);
            __reducedValues.push(this.action_16(__p));
        }
        break;
        case 17: {
            this.reduceAntecedent(___node,__tempType_NT);
            __reducedValues.push(this.action_17(__p));
        }
        break;
        case 18: {
            this.reduceAntecedent(___node,__constType_NT);
            __reducedValues.push(this.action_18(__p));
        }
        break;
        default: {
            throw new IllegalStateException("Unmatched reduce action " + iRule);
        }
    }
}


class JBurgAnnotation_BINOP_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_721e0f4f(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__expType_NT), this.getNthChild(0).getCost(__expType_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_binopType = -1;
    public   JBurgAnnotation_BINOP_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __binopType_NT: {
                result = getCostForRule_721e0f4f(goalState);
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                 int cost_binopType = this.getPatternMatchCost(__binopType_NT);
                 int cost_expType = cost_binopType;
                result = cost_expType;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __binopType_NT: {
                if ((cachedCostFor_binopType == -1)) {
                    cachedCostFor_binopType = getPatternMatchCost(__binopType_NT);
                } 
                result = cachedCostFor_binopType;
            }
            break;
            case __expType_NT: {
                result = getClosureCost(__expType_NT);
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __binopType_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_721e0f4f(goalState))) {
                    rule = 2;
                } 
            }
            break;
            case __expType_NT: {
                if ((Integer.MAX_VALUE > getCost(__binopType_NT))) {
                    rule = 16;
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



class JBurgAnnotation_CJUMP_2  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_6ea6d14e(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__expType_NT), this.getNthChild(0).getCost(__expType_NT))));
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_cjumpType = -1;
    public   JBurgAnnotation_CJUMP_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __cjumpType_NT: {
                result = getCostForRule_6ea6d14e(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __cjumpType_NT: {
                if ((cachedCostFor_cjumpType == -1)) {
                    cachedCostFor_cjumpType = getPatternMatchCost(__cjumpType_NT);
                } 
                result = cachedCostFor_cjumpType;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __cjumpType_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_6ea6d14e(goalState))) {
                    rule = 4;
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
        return (CJUMP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_CJUMP_2");
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
            case __constType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                 int cost_constType = this.getPatternMatchCost(__constType_NT);
                 int cost_expType = cost_constType;
                result = cost_expType;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __constType_NT: {
                result = 1;
            }
            break;
            case __expType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __constType_NT: {
                rule = 6;
            }
            break;
            case __expType_NT: {
                rule = 18;
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



class JBurgAnnotation_JUMP_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_6833ce2c(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__expType_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_jumpType = -1;
    public   JBurgAnnotation_JUMP_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __jumpType_NT: {
                result = getCostForRule_6833ce2c(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __jumpType_NT: {
                if ((cachedCostFor_jumpType == -1)) {
                    cachedCostFor_jumpType = getPatternMatchCost(__jumpType_NT);
                } 
                result = cachedCostFor_jumpType;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __jumpType_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_6833ce2c(goalState))) {
                    rule = 5;
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
        return (JUMP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_JUMP_1");
    }
}



class JBurgAnnotation_LABEL_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_LABEL_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __labelType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                 int cost_labelType = this.getPatternMatchCost(__labelType_NT);
                 int cost_expType = cost_labelType;
                result = cost_expType;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                result = 1;
            }
            break;
            case __labelType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __expType_NT: {
                rule = 15;
            }
            break;
            case __labelType_NT: {
                rule = 7;
            }
            break;
        }
        return (rule);
    }
    public  int getArity()  {
        return (0);
    }
    public  int getOperator()  {
        return (LABEL);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_LABEL_0");
    }
}



class JBurgAnnotation_MEM_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_2aaf7cc2(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__expType_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_memType = -1;
    public   JBurgAnnotation_MEM_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __memType_NT: {
                result = getCostForRule_2aaf7cc2(goalState);
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                 int cost_memType = this.getPatternMatchCost(__memType_NT);
                 int cost_expType = cost_memType;
                result = cost_expType;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                result = getClosureCost(__expType_NT);
            }
            break;
            case __memType_NT: {
                if ((cachedCostFor_memType == -1)) {
                    cachedCostFor_memType = getPatternMatchCost(__memType_NT);
                } 
                result = cachedCostFor_memType;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __expType_NT: {
                if ((Integer.MAX_VALUE > getCost(__memType_NT))) {
                    rule = 14;
                } 
            }
            break;
            case __memType_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_2aaf7cc2(goalState))) {
                    rule = 1;
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

    private  int getCostForRule_1888ff2c(int goalState)  {
        return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__expType_NT), this.getNthChild(0).getCost(__expType_NT))));
    }
    private  int getCostForRule_35851384(int goalState)  {
         JBurgAnnotation factoredPath_0 = this.getNthChild(0).getArity() > 0? this.getNthChild(0).getNthChild(0): errorAnnotation;
        if ((this.getNthChild(0).getArity() == 1) && (this.getNthChild(0).getOperator() == MEM)) {
            return (normalizedAdd(1, normalizedAdd(this.getNthChild(1).getCost(__expType_NT), factoredPath_0.getCost(__expType_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }
    private  int getCostForRule_649d209a(int goalState)  {
         JBurgAnnotation factoredPath_1 = this.getNthChild(1).getArity() > 0? this.getNthChild(1).getNthChild(0): errorAnnotation;
        if ((this.getNthChild(1).getArity() == 1) && (this.getNthChild(1).getOperator() == MEM)) {
            return (normalizedAdd(1, normalizedAdd(factoredPath_1.getCost(__expType_NT), this.getNthChild(0).getCost(__expType_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }
    private  int getCostForRule_6adca536(int goalState)  {
         JBurgAnnotation factoredPath_0 = this.getNthChild(0).getArity() > 0? this.getNthChild(0).getNthChild(0): errorAnnotation;
         JBurgAnnotation factoredPath_1 = this.getNthChild(1).getArity() > 0? this.getNthChild(1).getNthChild(0): errorAnnotation;
        if ((this.getNthChild(0).getArity() == 1) && (this.getNthChild(0).getOperator() == MEM) && (this.getNthChild(1).getArity() == 1) && (this.getNthChild(1).getOperator() == MEM)) {
            return (normalizedAdd(1, normalizedAdd(factoredPath_1.getCost(__expType_NT), factoredPath_0.getCost(__expType_NT))));
        } else  {
            return (Integer.MAX_VALUE);
        } 
    }private  JBurgAnnotation subtree0 = null;private  JBurgAnnotation subtree1 = null;private  int cachedCostFor_moveType2 = -1;private  int cachedCostFor_moveType = -1;private  int cachedCostFor_moveType3 = -1;
    public   JBurgAnnotation_MOVE_2(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __moveType_NT: {
                result = getCostForRule_1888ff2c(goalState);
            }
            break;
            case __moveType2_NT: {
                result = getCostForRule_35851384(goalState);
            }
            break;
            case __moveType3_NT: {
                result = getCostForRule_649d209a(goalState);
                result = Math.min(result,getCostForRule_6adca536(goalState));
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __moveType_NT: {
                if ((cachedCostFor_moveType == -1)) {
                    cachedCostFor_moveType = getPatternMatchCost(__moveType_NT);
                } 
                result = cachedCostFor_moveType;
            }
            break;
            case __moveType2_NT: {
                if ((cachedCostFor_moveType2 == -1)) {
                    cachedCostFor_moveType2 = getPatternMatchCost(__moveType2_NT);
                } 
                result = cachedCostFor_moveType2;
            }
            break;
            case __moveType3_NT: {
                if ((cachedCostFor_moveType3 == -1)) {
                    cachedCostFor_moveType3 = getPatternMatchCost(__moveType3_NT);
                } 
                result = cachedCostFor_moveType3;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __moveType_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_1888ff2c(goalState))) {
                    rule = 10;
                } 
            }
            break;
            case __moveType2_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_35851384(goalState))) {
                    rule = 11;
                } 
            }
            break;
            case __moveType3_NT: {
                 int bestCost = Integer.MAX_VALUE;
                 int currentCost = getCostForRule_649d209a(goalState);
                if ((bestCost > currentCost)) {
                    bestCost = currentCost;
                    rule = 12;
                } 
                currentCost = getCostForRule_6adca536(goalState);
                if ((bestCost > currentCost)) {
                    rule = 13;
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



class JBurgAnnotation_NAME_0  extends JBurgSpecializedAnnotation 
{

    public   JBurgAnnotation_NAME_0(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __nameType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __nameType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __nameType_NT: {
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
        return (NAME);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_NAME_0");
    }
}



class JBurgAnnotation_SXP_1  extends JBurgSpecializedAnnotation 
{

    private  int getCostForRule_357246de(int goalState)  {
        return (normalizedAdd(1, this.getNthChild(0).getCost(__expType_NT)));
    }private  JBurgAnnotation subtree0 = null;private  int cachedCostFor_sxpType = -1;
    public   JBurgAnnotation_SXP_1(Tree.IR node)  {
        super(node);
    }
    private  int getPatternMatchCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __sxpType_NT: {
                result = getCostForRule_357246de(goalState);
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __sxpType_NT: {
                if ((cachedCostFor_sxpType == -1)) {
                    cachedCostFor_sxpType = getPatternMatchCost(__sxpType_NT);
                } 
                result = cachedCostFor_sxpType;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __sxpType_NT: {
                if ((Integer.MAX_VALUE > getCostForRule_357246de(goalState))) {
                    rule = 3;
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
        return (SXP);
    }
    public  String getSelfDescription()  {
        return ("JBurgAnnotation_SXP_1");
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
            case __tempType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    private  int getClosureCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                 int cost_tempType = this.getPatternMatchCost(__tempType_NT);
                 int cost_expType = cost_tempType;
                result = cost_expType;
            }
            break;
        }
        return (result);
    }
    public  int getCost(int goalState)  {
         int result = Integer.MAX_VALUE;
        switch(goalState) {
            case __expType_NT: {
                result = 1;
            }
            break;
            case __tempType_NT: {
                result = 1;
            }
            break;
        }
        return (result);
    }
    public  int getRule(int goalState)  {
         int rule = -1;
        switch(goalState) {
            case __expType_NT: {
                rule = 17;
            }
            break;
            case __tempType_NT: {
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
        case CJUMP: {
            if ((node.getArity() == 2)) {
                return (new JBurgAnnotation_CJUMP_2(node));
            } 
        }
        break;
        case CONST: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_CONST_0(node));
            } 
        }
        break;
        case JUMP: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_JUMP_1(node));
            } 
        }
        break;
        case LABEL: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_LABEL_0(node));
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
        case NAME: {
            if ((node.getArity() == 0)) {
                return (new JBurgAnnotation_NAME_0(node));
            } 
        }
        break;
        case SXP: {
            if ((node.getArity() == 1)) {
                return (new JBurgAnnotation_SXP_1(node));
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
        new JBurgSubgoal(__expType_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0),
        new JBurgSubgoal(__expType_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0),
        new JBurgSubgoal(__expType_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0)
    },
    null,
    null,
    null,
    null,
    {
        new JBurgSubgoal(__expType_NT,false, 0,0),
        new JBurgSubgoal(__expType_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0,0),
        new JBurgSubgoal(__expType_NT,false, 0,1)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0),
        new JBurgSubgoal(__expType_NT,false, 0,1,0)
    },
    {
        new JBurgSubgoal(__expType_NT,false, 0,0,0),
        new JBurgSubgoal(__expType_NT,false, 0,1,0)
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
