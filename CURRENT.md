## Diary
*17th June 2020*
Working on record and array assignment.

MaximumMunch expression for store array is commented out as it doesn't work.

Record assign is generating a seg fault.

Multiplying the word size * 2 ( 16 ) when indexing the array makes test pass. I have no idea why. See translator.subScriptVar.

Test for 2 breaks in a while works, although I am not sure if it is working just by accident.

~~TODO: Test 2 breaks in while~~
TODO: Rewrite for loop without translation to while, ensure it can use a break statement
TODO: Investigate alternatives to creating a new semantic scope for while breaks, readonly vars etc.

*16th June 2020*

Added tests for array, record and recursive functions. They all pass.

For loop is passing now, although the index variable could be modified. It appears that spilling works too.

~~For loop readonly index variable doesn't work. I wrapped the index token in < > to protect it.
Unfortunately when we attempt to use it within the loop the symbol cannot be found. Similarily
the FindEscape module throws an error for the same reason.~~


I have changed the grammar back to the original version were use use the unaltered symbol.

TODO: Make for loop indexer readonly
TODO: Modify Translator.call to create tree instructions that move to RV for a function that doesn't return VOID.
See lines 557 and 560
TODO: Investigate parameter passing & register allocation when I remove the register arguments from the caller register set.
TODO: Add tests for arrays and records with bounds checking enabled
TODO: Implement semant type checking with details writen to system out similar to javac.


*15th June 2020*

~~Should a function be able to access a variable defined in the parent level ? It does work, if it should !~~

https://cs.lmu.edu/~ray/notes/writingacompiler/

Started testing static link functionality. Working now

Learning how to examine the stack using gdb. In particular checking that
variables are in the correct place on the stack. 

First, For each frame check that memory location at rbp - 8 equals the parent frame base pointer.
Next ??

Handy link for gdb debugging.

https://web.stanford.edu/class/archive/cs/cs107/cs107.1196/lab7/


TODO: If semant contains an error, translate is still executed. The compiler should halt in this case.
*14th June 2020*
Started six argument function call ( actually 7 arguments as the first item is a static link). 
The last item will be pushed onto the stack. At present we push the 7th to the stack. We
meed to ensure we access this correctly from inside the caller.

Fixed calling convention bug where temps being moved into calling convention temporaries
were clobbering other calling convention registers, This mean the called functions were
getting the wrong arguments. THis was fixed by adding both the expression temporaries and
the calling convention registers into the 'use/src' field for the call instruction. 

Added green and red colours to test script that generates programs and tests them

*12th June 2020*

Changed code to use the x64 registers. Fixed intel stack frame bug. Function test now passes.

Bug in function calls with more than 0 arguments. Static link doesn't appear to be added.

Fixed

Bug with 3 arguments to function.  Instructions that move calling convention registers into
their correct places is colouring incorrectly.

movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rdx, %rcx # default move
movq %rcx, %rdx # default move

See that rdx -> rcx, the rcx -> rdx

I am wondering if I need to generate the interference graph across the entire programme.

If a var is in use[n] then it is live in at node n.

eg x <- y, // y must be live in.

If a var is in line in at node n, it is in live out at all nodes m in pred[m]

If a var is live out at node n and not defined at node n, the var is also live in at n.

eg var is used at node 2, therefore its live out at node 1, if not defined at node 1, must
also be live in at node 1.


*11th June 2020*

Problem with stack management for function calls. Seems to adding a rbp move before the call. Probably the static link

Figured out how to test output of compiler, see tests/E2E. This will run all compile all files in a specified directory
and check the output against a test.result file.
 
Main.java was refactored and some obsolete unit tests were removed.

*10th June 2020*

Finished new implementation of codegen tiling implementation for store and array store.
Realized that store operations only use temporaries, they do not define them.

The old code gen tiling implementation classes where removed.

TODO: Figure out how to unit test all the code snippets.

TODO: Check load array code gen tile.

*8th June 2020*
Introduced new patterns for array type patterns. Unfortunately there is a bug where both the index expression and value expression are both being assigned to eax. This is either due to incorrect use / defs on instructions or a problem with pattern matcher.

Problems with the tiling, in particular selection of instructions that write to memory and ones that write to temps, load and stores.
I was investigating if its possible to implement these without tiling, I dont think it is as the IR tree are ambigpous regarding
loading and storing. If MEM is left child of MOVE, it means store, anywhere else, it means fetch.

eg MOVE(MEM, exp) -> store exp value to location of MEM

MOVE(TEMP, MEM) -> load value in address of MEM into TEMP


*7th June 2020*

Reimplementing Tile Matching as some assembly was incorrect due to a bug in the previous implementation.
Employing a visitor pattern ( again ! ), to do the tile matching. 

Array read is working now. I had to use the 32 bit registers instead of the 64 bit ones. Array write isn't due
to tile matching bug.

*3rd May 2020*

~~Implementing coalescing algorithm~~

Going to ensure that the non coalesce version works first.

$ gcc -g -no-pie -Wimplicit-function-declaration  -Wl,--wrap,getchar test.s runtime.c  -o test.out && ./test.out
$ gcc -g -no-pie  -Wl,--wrap,getchar test.s runtime.c  -o test.out && ./test.out 

https://cs.lmu.edu/~ray/notes/gasexamples/

String print Wasn't working because I was writing the argument into rsi and not rdi

$ break test.s:1
$ si - next instruction
$ info registers

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
