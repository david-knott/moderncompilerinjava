# Current Tasks

For register allocation we need to build the interference graph using all the code blocks,
not one by one. I am have created a simple GraphColouring class, but I need to refactor before I can proceed with register allocation.

## Immediate Work

Started register allocation. ( CodeFrag.java )

1. Spill temporary, working on inserting the spill moves into instruction list.

## Reorganising the DataFrag > Process > Canon , Blocks, Trace > CodeGen code

Implement liveout class using reverse control flow edges.

Possible bug in LiveOut class, where the BitSet capacity is not correct.

Added new RegisterSpiller classes and a test class. Appending and prepending the
new instuctions isn't working yet.

Need to figure out how to either use the TempMap or create my own version of it.

Note the CodeGen package is specifically for an intel x64 instruction set. This should be abstracted in the same way the Frame is

CodeFrag - how to get the spilled temps from the spilled nodes ?

String literals are not implemented in DataFrag.

FInd out how appels linearise function works. The last sequence will have a null entry is his implementation, which I would
rather avoid.

SEQ normalise function works, but I am not happy with it. And there are still errors if the only thing in the list is a Stmt that is not a SEQ.

IntelFrame contructor, where we create a new frame for a function. Loops through the formals list, checking
if they variables should escape or not. not sure how the moving of values from calling registers into the temps works for memory locations defined in the frame. We create variables either in the frame or in a temp based on the formals and the escape analysis we do before the semant stage.

Need to add code to move our calling convention register or frame location into the callee frame ( same process as temp). OR maybe we dont need to do this. The

I just implemented the callee save and restore code in procEntryExit1

Need to build a recusive normalise function that converts a sequence of sequence into more
easy to read ones.

Added a indicative method for CodeFrag for the recursive calling of register allocation. Still need to apply this to the entire programme. General algoirthm is

-   Generate flow graph for each code fragment
-   Add this to our interference graph and build it for the entire programme

---

Refactoring canonicaliation & codegen phases

Need to document the current functionality from end of the ranslate to ir phase to the start of register allocation. This all in the Main.java file.

Ideally, the compiler could be a service that could be called as a jar file.

Once complete, I can refactor.

Another motivation for this is that I am not sure if we should run the register allocation across their entire program ( all proc frags ) in one pass, or across each proc frag individually. The current version of the code will not support this due to its structure.

Investigate if data frags, assembly which contains string literals, can be held in same list as proc frags.

Current implementation has lots of print streams interleved with business logic. This makes refactoring hard
