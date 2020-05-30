## Diary

*30th May 2020*
Fixed bug in cmp register allocation.
Fixed bug where functional label was not being written to asm.
For loop looks correct

TODO: Ensure that functions that do not return a value are handled correctly.

TODO: FIgure out how to calculate out going parameter space.

*29th May 2020*
Need to ensure that the memory accesses created for spills are used for defs and uses.
TODO: Test this !

Modified test assem instruction to output a string representation of itself.

TODO: Add invalid escape sequence to the for each loop symbol to prevent it from being overwritten

TODO: Implement spill cost, in particular give temps that are for spilling infinite cost.

TODO: Enforce invariant that registers must be a subset of precoloured. There cannot be items in
the registers templist that are not also present in the precoloured.

*28th May 2020*
Started the potential spill colouring implementation. In the middle
of  forcing a spill, still need to save certain nodes as potential
spills.

Modified Translate.AccessList.append to use a function interface.

Rewrite the instructions that either use or define variables to save them
to the stack frame.

Test rewrite functionality. Bug in rewrite functionality. Adding 2 offset = 0
Can a use be used before a def ?

TODO: All append, prepend operations are OO. Set operations are functional.


*27th May 2020*
Implemented Liveness equations using java.util.BitSet. Tests were not passing.
The issue was caused by the implementation of the BitSet. It changes the bits within
the target set, rather than creating a new one. This was affecting the equations.
I cloned the bit sets prior to using them and this fixed the problem. I may still
implement by own BitSet class that provides a functional interface.

Implemented the reverse live out calculation.

Figured out the example in the book is not identical to my test. The book contains
a node the has both use / def and is also a target for a jump. The assem language
doesn't support this and this introduce an extra node into the equations.

TODO: Implement BitSet class with functional interface, so that or / and / andNot return a
new BitSet rather than modifying the calling instance.
TODO: Unit testing methodology for liveout equation should be better

*26th May 2020*

Added additional translate tests as well as some basic tiger programms
Started unit tests for graph colouring.

TODO: Reverse the flow calculation to speed up liveness analysis.
TODO: Find a better way to test the translate functions.

*25th May 2020*

Simplified the code, I introduced lots of classes in a previous attempt to simplify !
Move construction of Temp & Label instances into these classes. Added graphvis renderer
to be able to see the flow and colour graphs.

TODO: Refactor code so that Graphvis renderer is a hook plugin.

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

#### Helpful Links
https://assignments.lrde.epita.fr/source_code/project_layout/src_regalloc.html
https://assignments.lrde.epita.fr/source_code/project_layout/src_target_ia32.html
https://gitlab.lrde.epita.fr/tiger/tc-base/-/tree/2022/src