# Current Tasks 

Reorganising the DataFrag > Process > Canon , Blocks, Trace > CodeGen code

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


