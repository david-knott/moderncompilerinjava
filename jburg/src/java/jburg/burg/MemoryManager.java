package jburg.burg;

import antlr.collections.AST;
import org.antlr.stringtemplate.*;

import jburg.emitter.EmitLang;

/**
 * MemoryManager represents the target compiler's
 * memory management API. This is distinct from
 * its allocation API; use @Allocator when the
 * target compiler can allocate with placement new,
 * use MemoryManager when the target compiler
 * allocates with some other routine and needs
 * to register the allocated annotations.
 */
public class MemoryManager
{
    MemoryManager(AST root)
    {
        this.parameterName = root.getFirstChild();
        this.procall = parameterName.getNextSibling();
    }

    AST parameterName;
    AST procall;
}
