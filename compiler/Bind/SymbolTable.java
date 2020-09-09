package Bind;

import java.io.PrintStream;
import java.util.Hashtable;

import Symbol.Symbol;
import Util.Assert;

class SymbolTable {

    private final InnerTable root;
    private InnerTable current;

    private class InnerTable {
        public final Hashtable<Symbol, SymbolTableElement> table = new Hashtable<Symbol, SymbolTableElement>();
        public final InnerTable parent;

        InnerTable() {
            this.parent = null;
        }

        InnerTable(InnerTable parent) {
            this.parent = parent;
        }
    }

    private SymbolTable(InnerTable innerTable) {
        this.root = this.current = innerTable;
    }

    public SymbolTable() {
        this.root = this.current = new InnerTable();
    }

    public SymbolTable(Hashtable<Symbol, SymbolTableElement> initial) {
        this.root = this.current = new InnerTable();
        for (Symbol key : initial.keySet()) {
            this.root.table.put(key, initial.get(key));
        }
    }

    public SymbolTable rootSymbolTable() {
        return new SymbolTable(this.root);
    }

    public void put(Symbol symbol, SymbolTableElement o) {
        Assert.assertNotNull(symbol);
        Assert.assertNotNull(o);
        if(this.current.table.containsKey(symbol)) {
            throw new Error("Duplicate symbol in scope.");
        }
        this.current.table.put(symbol, o);
    }


    public boolean contains(Symbol symbol) {
        return this.contains(symbol, true);
    }

    public boolean contains(Symbol symbol, boolean rec) {
        Assert.assertNotNull(symbol);
        InnerTable currentScope = this.current;
        do {
            if (currentScope.table.containsKey(symbol)) {
                return true;
            }
            currentScope = currentScope.parent;
        } while (rec == true && currentScope != null);
        return false;
    }

    /**
     * Finds symbol in current scope or parent.
     * @param symbol
     * @return the symbol element or throws an exception.
     */
    public SymbolTableElement lookup(Symbol symbol) {
        Assert.assertNotNull(symbol);
        InnerTable currentScope = this.current;
        do {
            if (currentScope.table.containsKey(symbol)) {
                return currentScope.table.get(symbol);
            }
            currentScope = currentScope.parent;
        } while (currentScope != null);
        throw new Error("Cannot find symbol " + symbol);
    }

    /**
     * Create a new scope for the given namespace
     */
    public void beginScope() {
        this.current = new InnerTable(this.current);
    }

    /**
     * Destroy the most recently used scope namespace.
     */
    public void endScope() {
        this.current = this.current.parent;
    }

    public void debug(PrintStream printStream) {
        // try(PrintStream printStream = new PrintStream(outputStream)) {
        printStream.println("## scope debug information");
        printStream.format("%10s%10s\n", "symbol", "type");
        for (var keys : this.current.table.keySet()) {
            // SymbolElement type = this.current.table.get(keys);
            // printStream.format("%10s%10s\n", keys, type);
        }
        // }
    }
}