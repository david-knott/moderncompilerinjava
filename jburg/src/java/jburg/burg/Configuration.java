package jburg.burg;

import jburg.burg.inode.InodeAdapter;
import jburg.burg.inode.InlineInodeAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configuration settings from the grammar and command line;
 * shared by the generator and emitters.
 */
public class Configuration
{
    /**
     * When not null, the annotation get/set methods
     * i-nodes use to self-manage their annotations.
     */
    public AnnotationAccessor annotationAccessor;

	/** 
	 *  I-node adapter class name.  The adapter is instantiated by name.
	 *  @note: specified as an alternative to the I-node class' name,
	 *  which selects a builtin adapter.
	 */
	public String iNodeAdapterClass;

    /**
     *  When set, generate a label() function
     *  that discards null i-nodes.
     */
	public boolean generateNullTolerantLabeller = false;

    /**
     * Cache closure computations if the number of
     * elements in the computation exceeds this threshold.
     */
    public int closureCacheThreshold = 7;
    
    /**
     * Volatile cost functions.
     */
    public List<String> volatileCostFunctions = new ArrayList<String>();

    /**
     *  The type of the INodes' opcode.  Defaults to int but
     *  can be an enum for maintainability.
     */
    public String opcodeType = "int";
    
	/**  Name of the initial parameter to the label routine. */
	public String initialParamName = "to_be_labelled";
	
    /**
     * The type of the generated nonterminals.  Defaults to int
     * but can be an enum for maintainability and error detection.
     */
    public String ntType = "int";
    
	/** I-node adapter to use */
	public InodeAdapter iNodeAdapter;
	
	/** Name of the node in the reducer */
	public String reducerNodeName = "__p";

	/** Name of the stack of reduced values */
	public String reducedValuesName = "__reducedValues";

    /**
     *  The name of the i-node class that's being labeled and reduced.
     */
    public String iNodeClass = null;

    /** The package name of the generated reducer. */
    public String packageName = null;

    /**  The name of the generated BURM's base class. */
    public String baseClassName = null;

    /**  The name of the annotation's base class. */
    public String annotationBaseClassName = null;

    /**
     * Memory allocator to use; only some
     * targets support this.
     */
    public String allocator = null;

    /**  Default error handler.  null means use hard-coded default, i.e., throw an exception. */
    public String defaultErrorHandler = null;

    /** Return types declared in the specification. */
    public Set<String> returnTypes = new HashSet<String>();

    /** Initialization sequence for static annotations. */
    public String initStaticAnnotation = null;

    /**  Command-line options.  */
    public Options options;

    /**
     *  The goal states' names become symbolic constants
     *  in the generated reducer or the nonterminal enumeration.
     */
    public Set<String>  goalStateNames;

    public String getAnnotationInterfaceName()
    {
        return options.annotationInterfaceName;
    }

    Configuration(Options options)
    {
        this.options = options;
    }
}
