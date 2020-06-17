.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L19:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $0, %rax # const
movq %rax, %rsi # move arg 1 to temp
movq $10, %rcx # const
movq %rcx, %rdi # move arg 0 to temp
call initArray # default call
movq %rax, %rax # rax to temp 
movq %rax, %rax # default move
movq %rax, %rax # spill
movq %rax, -8(%rbp) # spill
movq $0, %rax # const
movq $16, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq $10, %rcx # const
movq %rcx, (%rax) # store
movq $9, %rax # const
movq $16, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq $9, %rcx # const
movq %rcx, (%rax) # store
movq $5, %rax # const
movq $16, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq $5, %rcx # const
movq %rcx, (%rax) # store
movq $5, %rax # const
movq $16, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq $0, %rax # const
movq $16, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq $9, %rax # const
movq $16, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L18
L18:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
