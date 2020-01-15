Calculating static links

Case 1, 

Consider a function a() calling a function b(). Where b() is directly contained within a() in the source code 

Pseudo Code

function a() { <!-- level n -->
  var aa;
  var bb;
  
  function b() { <! -- level n + 1 -->
    var cc;
    
    //accessing a variable aa that is a non local variable
    var dd = aa + cc;
  }

  b();
}

When this is compiled to assembly, the assembly for b needs to access stack data in a. To do this we
use a static link. The static link is a pointer to memory adress of the FP for a. We use this with aa's
offset. We know the offset for aa from a's activation record.

We pass this pointer as an extra argument into b when we call it.


Case 2,

Consider function c() calling function a(), where function o() contains both of these.

Pseudo Code

function o() {
  var oo;
  
  function a(){ <!-- level n -->
    oo = oo + 1;
  }

  function b(){ <!-- level n -->
    function c() { <!-- level n + 1 -->
      a();
    }
    c();
  }

}
o()

When function c() calls function a(), when need access to the frame that contains the variable oo. 
This is the stack frame below a(). This must be computed using static links. We calculate how many
levels there are between function c() and function a() and subtract 1. This is how many static links we
must traverse to get the frame reference for o, which contains the frame address for o. This frame address
should be passed to the function c when it is called.
