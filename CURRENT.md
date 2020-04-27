# Current Tasks 

## Reorganising the DataFrag > Process > Canon , Blocks, Trace > CodeGen code

I have implemented SimpleGraphColouring2 although I am not happy with it. Mainly due to how the
precoloured node colours are set after object constuction.

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


