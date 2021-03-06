# Diary

## 4th December 2020

Bug in c code that prints string, seems to be happening when there are multiple \n or \t in print
followed by a second print.

```
let
in
        print("\t\t");
        print("A")
end
```

Fixed Bug - string compare is comparing the string pointers and not the string themselves.

Fixed Bug - function result not being placed back into rax. I missed some code in the FuncDec visitor which does this.

## 3rd Decemeber 2020

More tests are now passing. There are still some errors in the regression tests.
## 2nd Decemeber 2020

Fixing bugs around types and how they affect the translation. 
In particular, sequences and expression sequences, return types of calls.

## 30th November 2020

Translate Visitor appears to complete.

I am not happy with the Binder and how it attaches types and definitions to AST.
In theory I think its a great idea. Need to modify it so that types and definitions
must be set on particular AST nodes. Perhaps a secondary tree that contains this information.

If binder encounters an error and cannot set the type, it will set the expression type to VOID.

## 21th November 2020
Completed record expresssion translation and testing.

Modified parser to better handle dec list source code. Previously the parser would fail
on source code such as var a:int = 1, as there was no related level created to place locals.
I modified the parser to create an implicit function dec and let exp so that a level /frame
would be created.

Idea regarding testing trees, why no render as XML and test using XPath ?

## 20th November 2020

In progress - FieldExpressionList. Builds a sequence of statements that initialize a record.

Renamer - In FunctionDeclaration renamer, we check if formal argument type definition is null.
If it is we assume the type is primitive. I am not sure this is a good way to do this.

Binder - In FunctionDeclaration formals arguments, which are represented as a DecList of VarDec classes are not processed by their respective vistor methods. We create a RECORD within the FunDec
visitor method.

PrettyPrinter - In FunctionDeclaration formals arguments, which are represented as a DecList of VarDec classes are not processed by their respective vistor methods. We create a RECORD within the FunDec
visitor method.


## 19th November 2020

Refactored ast function dec to use var decs as formals.

~~Need to check all the unit tests still.

~~Bug in TranslateVisitorTest.forTest, strange reference to STRING.
## 18th November 2020

Chaning the function dec ast to use  a dec list of vardecs instead of 
a field list. This will allow easier hashtable lookups in the translate
visitor. The function dec visit method adds the formals as variable declarations.
This makes sense as the compiler treats these as new variable declations. This
change has broken the original Semant class and the Grm file, so these
need to be fixed.

Started translate visitor

TODO: Implement Loop End;

## 11th November 2020

Inliner and Pruner. I think it is complete now.

TODO: Add base theory class that allows me to test all the E2E tests.

## 6th November 2020

Fixed nested function failure in inliner.

Pruner has not been implemented.

Refactored Tasks so they dont reference the static instance of the TaskRegister.

## 5th November 2020

Fixed some bugs in binder, renamer and pretty printer. I am still not certain
these classes are 100% correct. In particular the renamr tests don't look like
valid syntax.

Refactored renamer tests to use junit theories.

~~Nested functions fail in inliner.~~

## 3rd November 2020

Modified code to use a primitives prelude and an enclosing main function.

I am checking the cloner visitor and pretty printer. Adding an equality checker for AST's
would be useful for testing.

Added some theory type tests for pretty printer.

I plan to move the compilation test from bash to junit.

## 30th October 2020

Cloner is broken after modifing code to use declists.
Its likely alot of the absyn visitors are broken now.

We had originally assumed that a function body cannot be null,
this is not the case now. It can be null in the case of primitive
declarations, this could be refactored into the base absyn visitor.

There are also prolems with the Semant phase. When should the level
be created, the level also has a reference to the frame and its associated
label, which corresponds to a specific function. We need to make sure that
that we bind our runtime functions.

Level -> Function Dec ( Name ) -> Activation Record ( Label )


Outermost Level
    
    Primitives...

    Tiger Main Level

        User Code.


Also how to be provide the frame to the semant phase ?

The level constructor doesn't support creating a level without either a frame or a parent level.

The answer to both questions might be separate the frame and level classes.


## 29th October 2020

Modified  TaskContext to use DecList instead of Program.
Added new prelude tiger file with the built in functions.
Still need to modify the grammar to use DecList as the top level
object.  

The primitive.tih file is a declist, the input program file is an exp.
We wrap the input program with a function which allows us to link it
into the runtime function.

We need to wrap the input stream rather that the ast. I think the pretty
printer is used for this. 

Problem is that the parser expects an expression or a declist. I added a rule
to the grammar to allow either a declist or a expression to be the root 
of the parse tree.


## 28th October 2020

Modifying code so that the external functions are passed in as a prelude file. This is to support
the new functionality where the entire expression is wrapped in a function, which is linked into
the c runtime main call.

## 20th October 2020

Cycle tests on graph.

TODO: Implement outer tiger main wrapper. If tiger program does not contain an outer function
wrapper, create it.

Question, what happens if the program does have a main function ?

Wrap the created AST in an outer function call


```
1 + 2
```
becomes
```
function _main() = (
    1 + 2
)

```

## 16th October 2020

Changed cycle detection in function call graph to use DFS.
Transistive closure is not being used. I am implementing tests
on the call graph to ensure is is correct.


## 15th October 2020

Transitive Closure cycle detection is still in progress.

## 8rd October 2020 ##
Added inliner and pruner visitors. Not quote finished yet.
Need to use a call graph to figure out what can be pruned and
inlined.

a -> b -> c 

If a function dec is not present in the call graph it can be
pruned from then source. 

Calculating the transisitive closure of the graph will have generate
all paths where

TODO: Calculate the transitive closure, draw the transitive closure.

## 3rd October 2020 ##
Added type visitor.

## 30th September 2020 ##

I have refactored the type checker and binder. The binder sets the AST definitions
for calls, types and variables. Similarly the Type.Type is set on certain AST nodes.
These are used in the TypeChecking phase. This will alll need to be unit tested.

There is a bug in the renamer code. Perhaps I should wait until the type checker is done
before fixing this.


## 23th September 2020 ##

I am not happy with the Binder, TypeChecker and Renamer visitors. They are a bit messy.
In particular sometimes I set a global variable ( Type ), this smells a bit as its easy
to forget to set it, also it is not always set. Lets investigate this a bit further.

Still not happy, added in a dummy exp for the int & string definition in the type symbol table.


## 22th September 2020 ##

Implemented CallExp visitor method. I still need to implement the associated test.

## 17th September 2020 ##

Started the TypeChecker phase. Checking for for loop variable assignment using
a list that stores all the variable declarations and comparing assignment expression
with it.

I still need to implement code that binds the type to expressions.

## 12 September 2020 ##

TODO: change FieldList.ty field to use NameTy rather than Symbol.

## 11 September 2020 ##
Binder isn't quite right. There is a problem when a type is int or string.
Enable renamer to see if happening.

Bug with short form and long form argumennts combined.

Added a new escaping visitor

In Absyn.PrettyPrinter parenthese no longer stack for free. for example
((((((((((0)))))))))) is pretty printed as (0)

Started on recursive function and type renaming.

There is a bug in Absyn.Renamer for nested functions, it does rename them.

# 10th September ##
Continuing work on Binder, displaying type definitions in PrettyPrinter.
Absyn.Typable is not used for now.

Fixed bug in Parser that was doubling up on FunctionDecs and TypeDecs.

# 9th September ##
Continuing work on Binder.

We bind AST uses ( eg VarExp ) to their definitions ( eg VarDec ), this gets around
the fact that the binder scopes are destroyed at the end of traversal. Additionally 
we set the Types on AST nodes that are typable.

Some bugs in the AST pretty printer were also fixed.

TODO: Report errors from the binder.
TODO: Finish setting types on the AST.
TODO: Figure out how to test the binder.

## 7th September ##
Continuing work on Binder. 

Question: How can be have a separate binder visitor from the translation ?
All the binder scopes will have been destroyed when the binder visitor has
completed its AST traversal.

Question: What happens if recursive type declaration are NOT contiguous.

## 4st September ##
Working on separating out the binding from the type checking.
Started on a default abstract syntax visitor which is used to
cut down on code repetition when we are only interested in certain
types in the tree traversal.


## 3st September ##
Finished command line parser. I did some refactoring on the tasks module. 
~~ I am now looking at having different streams for output and input. ~~

compiler --input some_file.tig --output another_file.asm / input from some_file, output to another_file
compiler  / takes input from standard in, writes output to standard out.

## 1st September ##
Starting command line parser. Still need to implement short code arguments.

## 29th August 2020 ##
Added timing code. The cumulative count has been commented out as it doesn't work. I am using a
stack to timers. In the main method there is a timer start, there are also timers started and stoped
for the various tasks. The sequence is as follows
* Main is called, start the 'rest' timer
* Tasks register calls tasks
* Each task starts a new timer and pauses the previously running timer
* WHen task completes, it resumes the previously running timer
* A main exit, it stops the 'rest' timer.

This approach allows me to time the entire main call, rather than just the tasks. The cumulative
figure is incorrect as is shows the timing for each function in sequence. The problem is the
rest function is started and stopped between each task call. Maybe the 'rest' timer should be 
displayed for each stop / start event ?

Bug: using inst-stats flag seems to stop the reg allocation from happing.
Bug: instruction count does not take into account moves that were removed by coalescing. I think I
would prefer to actually remove the MOVEs from the instruction list. 

TODO: Introduce flag to write to either system out of a file path.
TODO: Separate out debug information from assembly code. If user specifies a file location, write debug to system.out
If user does not specify a file location, write debug and assembly to system out.

## 27th August 2020 ##
Fixed escaping bug, all tests pass again. 

TODO: Still need to fix code gen.

## 26th August 2020 ##
Problem with task arguments. First need to see why utility bash scripts are not recognising the
escapes-compute. I was seeing differences due to the new code defaulting to every variable escaping.

## 25th August 2020 ##
Added code to move named memory locations into registers, this fixed most errors.

TODO: Fix bug in nested binop expressions, see function_six_arg.tig for example. Bug was actually 
in Intel frame where we move arguments into their correct places within the function. This bug
was present in the original version, I just hadn't noticed it.

~~For the factorial.tig test file, the problem appears to be a non terminating
recursive loop. This is likey due to either a comparison bug or arithmetic operand bug.~~

TODO: Detect cyles in task register.

Command line parsing next.

Rules 

* zero args except for tiger file name, compiles for x64, writes assembly to standard out.
* if args present, we process them and figure out what dependencies need to be executed.
* build a sequence of tasks and then execute them.
* create a special argument to write assembly to file
* create special argument that reads tiger from standard in.

Once a basic command line arg parser is in place, we can continue debuging the code gen.
Without the command line parser, I will need to comment out all the pretty print code.
RUn GDB and find what is causing the seg fault. The static link code looks correct.

## 24th August 2020 ##
Added pretty printer functionality into the translate and canon tasks. This
is to assist in tracking down a bug in the jburg code generation. I am focusing
on the factorial.tig test at the moment. It passes in master but fails in the
jburg feature branch. The seg fault occurs at Label L7 where a value in a
register is being saved to the frame, this is the static link. It looks correct. 

    movq $-8, %rax # move -8 to rax
    movq %rax, %rax # does nothing 
    add %rbp, %rax # add -8 to  base pointer address
    movq %rdi, (%rax) # store value in rdi into the -8 fromm the base pointer

The jburg implementation appears to be using a higher cost tiling, which should be easy to 
fix by tweaking the  tile costs. However, the even with the subopitmal tiling pattern, 
I would still expect the code to compile and work. So I think we need to further examine 
the assembly and use GDB to trace the code execution. 

I also need to implement the command argument functionality as I keep having to comment out 
stuff. I want to be able to view the canon tree and non allocated assmebly without all the other 
debug information.

## 20th August 2020 ##
Some minor refactoring. The emitted assembly is not correct. I need to
compare the working branch pre register allocation assembly with this one
to see the differences. This may reveal what the problem is.
~~ TODO: Remove old emitter class and associated tests ~~
~~ TODO: BINOP operation checks and move call and exp call implementation ~~

## 19th August 2020 ##
Added assembly emsssion to the JRurg reducer class.
TODO: Remove old emitter class and associated tests
TODO: BINOP operation checks and move call and exp call implementation

## 18th August 2020 ##
Redoing JBurg. Returning objects from JBurg specifications that I reduce in a
Reducer class. I am not sure if its the right way to do this. The instruction
selection pass is the next thing I need to do, so I will continue with this
until its complete and can replace the Maximum Munch implementation.

## 17th August 2020 ##
I converted the StmtList to a Tree.SEQ after caonicalisation. I am hoping that
the following instruction selection phase with the new Jburg implementation will
be able to use the SEQ based structure.

I have wired up an Intel.Task to hit the JBurg code generator. Unfortunately its
not working correctly yet.

## 16th August 2020 ##
Implemented more instruction matching and tests with JBurg

Created new tasks for Canon module so we can separate it from the the intel codegen
phase. I am stuck on what representation we should use for the canon rep ( lir rep ).
The current implementation from Appels book returns a StmtList however the canon
is done as part of the code gen, so we have a referecnce to the current frame.

Do I create a new datatructure for the StmtList and Frame, or modify the existing
Canon module so that it can return a representation that can use the ProcFrag structure.
I think I can use a Sequence as we ignore the seq in our IR. Basically replace the 
StmtList (head, tail) with a SEQ(left, right) where left and right are used as a head
and tail respectively.


## 14th August 2020 ##
Implementing new instruction selection using jburg ( http://jburg.sourceforge.net/ )
I ran into a problem attempting to match load / store using a tile for indirect scaled
addressing mode.

I was using a rule like this

    move = MOVE(exp arg0, MEM(BINOP(exp arg1, BINOP(exp arg2, CONST(void) arg3)))) : 1

but the generated jburg tree matcher would not match. The only way I could this rule to
match was to replace the exp non terminals with terminals for TEMPS. Obviously this is not
going work as the tree is never going to have temps in those places. To address this I had
to create an auxilary rule that captures this sub pattern

    /* indirect addressing with displacement and scaled index */
    arrayIndex =  MEM(BINOP(exp arg0, BINOP(exp arg1, CONST(void) arg2))) : 1
    {
        emitter.startLoadIndirectDispScaled(arg0, arg1, arg2);
        return null;
    }

    move = MOVE(exp arg0, arrayIndex arg1) : 1
    {
        emitter.endLoadIndirectDispScaled(arg0, arg1);
        return null;
    }

I am not happy with this as I need to pass the arguments from the arrayIndex tree into
the move for code emission.

## 5th August 2020 ##
Refactoring code to use a command / chain of responsibility pattern for arguments


## 16 July 2020 ##
Compiler works ( at least all the tests work ). It implements the full iterated
coalescing graph colouring algorithm as outlined in A Appels book. 

I need to refactor the Semant / Translate classes into a TypeChecker and
Translator visitor.

The error reporting needs to be implemented correctly.

The parser will fail on the first error it encounters. The grammar should
be modified to include an error production that allows to parser to recover
after an error and continue its parse. All errors would then be reported to
the client.

I would like to be able to generate graphs for the register allocation & coalescing

I would like to be able to display the programs liveness analysis

Well done me !

## 10 July 2020 ##
Fixed another bug, in maximum munch MEM_TO_MEM, we were using a MEM assembly 
function instead of a OPER. This was causing the coalesce function to not work
correctly.

Fixed simple bug in Instr comparator.

All items pass, except for merge_simple.

## 9th July 2020 ##
Currently failing

Test Result: ./good/array_assign.tig failed.
./run_good.sh: line 12: warning: command substitution: ignored null byte in input
./run_good.sh: line 13: warning: command substitution: ignored null byte in input
Test Result: ./good/binsearch.tig failed.
./run_good.sh: line 12: warning: command substitution: ignored null byte in input
./run_good.sh: line 13: warning: command substitution: ignored null byte in input
Test Result: ./good/merge_simple.tig failed.
./run_good.sh: line 12: warning: command substitution: ignored null byte in input
./run_good.sh: line 13: warning: command substitution: ignored null byte in input
Test Result: ./good/queens.tig failed.

I will focus on array_asign first as that is the simplist. Its also the only one
that seg faults.

It appears that certain temporaries that interfere are being coalesced to the same
register. This should not happen.

## 8th July 2020 ##
Pretty Print is prettier.

Coalesce in progress. Registers allocation is not correct.

Fix bugs in Semant Validation checking as per Epita spec.

Finish Coalesce

## 6th July 2020 ##
Fixed bug in store array assembly, index and base registers were reversed.

Bug in register allocation / code generation for stores and loads. Possible due
to 2 mems in a move.

Bug in writing \n to standard out, rendering as ^B

## 5th July 2020 ##
Fixed bug in register allocation. I had rax as in the calldefs. This was preventing the
register allocation from generating correct assignments. 

I have found another bug in escapes, where an array is defined at a lower level
and an variable is declared using the array subscript;

Subscription call...

init variable => get current frames static link, this points to parent frames frame pointer.
Use this to reference the varible access.

## 26th June 2020 ##
Bug in recursive declarion of functions. if a is declared before b  and a calls b, we get a label error.
Fixed static link bug. Queens and merge are now passing.

~~Bugs: Ordering of functions matters. If a function is called before it is defined the programme
will compile but will not assembly. This is due to missing labels

Bugs: If a variable is defined with the same name as a function we get java compilation errors

## 25th June 2020 ##

~~Static link is incorrect in a recursive function call.~

~~Test static link function when calling a function at the same level that is not recursive~

TODO: Reintroduce Sugared For Loop.

~~Defined N at top level~

## 24th June 2020 ##
Fixed binops, strings and ifthenelse. Still bug in queens and merge.

~~String E2E tests.

~~TODO: Binary operator and relative operator tests.
TODO: & and | operators have a bug. These translate into 'if cond1  then cond2 else 0' and
'if cond1 then 1 else cond2'. The code that creates the IR is not right. The IfThenElseExp.unCx
method is for instances where and IfThenElse is used as a conditional expression, specifically in
an IF statement. I suspect when it is used like this we jump to the parent IF's true label if
these expressions return 1 and we jump to the false label if they return 0.


## 23nd June 2020 ##
Continuing to add unit tests for semantic type checking. Fixed read only for loop bug.

~~TODO: Test string functions


## 22nd June 2020 ##

Fixed the for loop bug. It was due to an error in the 'AND' set operation. Jeez.
Started refactoring semantic analysis and adding new type checking tests.

TODO: Semant -> Visitor, 
~~TODO: Readonly Assignment to For Indexer~~

## 19th June 2020 ##
Fixed sorting bug in Temps which was breaking register allocation.

~~Still a bug in for loops if we at a printi() statement inside the loop.
This causes the loop to repeated spill. I have no idea why.~~

~~In the code generation, where there are combinations of instructions that use a move followed by an OPERATION
We need to be careful when spilling.~~

## 18th June 2020 ##

~~Register Allocation. Is it working ?~~

I added some code the dumps the live ranges is a visual format.

Investigating use/defs for calling functions.

*17th June 2020*
Working on record and array assignment.

Add rax to def list for function call that doesn't return a value.
This is to ensure it is not clobbered in the calling function. Unfortunately
this is breaking lots of other tests due to spill errors. This does fix record assign however.

I think there is a bug in the spilling. Focus on the while loop. It seems strange that a simple
while loop with 1 variable would require spilling.

~~MaximumMunch expression for store array is commented out as it doesn't work.~~

~~Record assign is generating a seg fault.~~

Modified runtime.c to use longs instead of ints. Longs are 64 bit

~~Multiplying the word size * 2 ( 16 ) when indexing the array makes test pass. I have no idea why. See translator.subScriptVar.~~

~~Test for 2 breaks in a while works, although I am not sure if it is working just by accident.~~

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

~~TODO: Make for loop indexer readonly~~

~~TODO: Modify Translator.call to create tree instructions that move to RV for a function that doesn't return VOID.
See lines 557 and 560~~

~~TODO: Investigate parameter passing & register allocation when I remove the register arguments from the caller register set.~~

~~TODO: Add tests for arrays and records with bounds checking enabled~~

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


~~TODO: If semant contains an error, translate is still executed. The compiler should halt in this case.~~

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

~~Bug in function calls with more than 0 arguments. Static link doesn't appear to be added.~~


*11th June 2020*

~~Problem with stack management for function calls. Seems to adding a rbp move before the call. Probably the static link~~

Figured out how to test output of compiler, see tests/E2E. This will run all compile all files in a specified directory
and check the output against a test.result file.
 
Main.java was refactored and some obsolete unit tests were removed.

*10th June 2020*

Finished new implementation of codegen tiling implementation for store and array store.
Realized that store operations only use temporaries, they do not define them.

The old code gen tiling implementation classes where removed.

~~TODO: Figure out how to unit test all the code snippets.~~

~~TODO: Check load array code gen tile.~~

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

~~TODO: Ensure that functions that do not return a value are handled correctly.~~

TODO: FIgure out how to calculate out going parameter space.

*29th May 2020*

~~TODO: Need to ensure that the memory accesses created for spills are used for defs and uses.~~


Modified test assem instruction to output a string representation of itself.

~~TODO: Add invalid escape sequence to the for each loop symbol to prevent it from being overwritten~~

~~TODO: Implement spill cost, in particular give temps that are for spilling infinite cost.~~

~~TODO: Enforce invariant that registers must be a subset of precoloured. There cannot be items in
the registers templist that are not also present in the precoloured.~~

*28th May 2020*

Started the potential spill colouring implementation. In the middle
of  forcing a spill, still need to save certain nodes as potential
spills.

Modified Translate.AccessList.append to use a function interface.

Rewrite the instructions that either use or define variables to save them
to the stack frame.

Test rewrite functionality. Bug in rewrite functionality. Adding 2 offset = 0
Can a use be used before a def ?

~~TODO: All append, prepend operations are OO. Set operations are functional.~~


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

~~TODO: Implement BitSet class with functional interface, so that or / and / andNot return a
new BitSet rather than modifying the calling instance.~~

TODO: Unit testing methodology for liveout equation should be better

*26th May 2020*

Added additional translate tests as well as some basic tiger programms
Started unit tests for graph colouring.

~~TODO: Reverse the flow calculation to speed up liveness analysis.~~

~~TODO: Find a better way to test the translate functions.~~

*25th May 2020*

Simplified the code, I introduced lots of classes in a previous attempt to simplify !
Move construction of Temp & Label instances into these classes. Added graphvis renderer
to be able to see the flow and colour graphs.

TODO: Refactor code so that Graphvis renderer is a hook plugin.

## Reorganising the DataFrag > Process > Canon , Blocks, Trace > CodeGen code

~~Implement liveout class using reverse control flow edges.~~

~~Possible bug in LiveOut class, where the BitSet capacity is not correct.~~

~~Added new RegisterSpiller classes and a test class. Appending and prepending the
new instuctions isn't working yet.~~

~~Need to figure out how to either use the TempMap or create my own version of it.~~

~~Note the CodeGen package is specifically for an intel x64 instruction set. This should be abstracted in the same way the Frame is~~

~~CodeFrag - how to get the spilled temps from the spilled nodes ?~~

~~String literals are not implemented in DataFrag.~~

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
