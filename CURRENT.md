# Current Tasks 

## Reorganising the DataFrag > Process > Canon , Blocks, Trace > CodeGen code

How to spill a temp to memory in the case of a definition, how to retrieve a value from memory
and place it in a temp. How do we reference the memory location when we retreive the value 
back from the frame and place it into a new temp ?

How do we ensure that the code is architecture independant. I assume it needs to do into the frame,

Test that the store operation is placed after the definition of the variable.

Test that the load operation is placed before the usage of the variable.

Need to figure out how to either use the TempMap or create my own version of it.

Note the CodeGen package is specifically for an intel x64 instruction set. This should be abstracted in the same
way the Frame is

## Later

For register allocation we need to build the interference graph using all the code blocks, 
not one by one. I am have created a simple GraphColouring class, but I need to refactor before I can proceed with register allocation.


--------------------------------------------

Refactoring canonicaliation & codegen phases

Need to document the current functionality from end of the ranslate to ir phase to the start of register allocation. This all  in the Main.java file. 

Ideally, the compiler could be a service that could be called as a jar file.

Once complete, I can refactor. 

Another motivation for this is that I am not sure if we should run the register allocation across their entire program ( all proc frags ) in one pass, or across each proc frag individually.  The current version of the code will not support this due to its structure.

Investigate if data frags, assembly which contains string literals, can be held in same list as proc frags.

Current implementation has lots of print streams interleved with business logic. This makes refactoring hard


