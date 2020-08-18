package Intel;

import Temp.Temp;
import Tree.BINOP;

/**
 * Represents a addition or subtraction from a memory address
 * stored in a register.
 */
public class BinopOffsetExpression {

    /**
     * The address value stored in the register.
     */
    final Temp base;
    /**
     * The offset from the address value.
     */
    final Integer offset;
    /**
     * The Tree.BINOP element. Only PLUS and MINUS are supported.
     */
    final BINOP binop;

    /**
     * The default constructor.
     * @param binop
     * @param base
     * @param offset
     */
	public BinopOffsetExpression(BINOP binop, Temp base, Integer offset) {
        this.base = base;
        this.offset = offset; 
        this.binop = binop;
	}
}
