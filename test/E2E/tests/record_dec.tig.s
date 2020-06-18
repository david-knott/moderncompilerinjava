.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L3:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $16, %rax # const
movq %rax, %rdi # move arg 0 to temp
call initRecord # default call
movq %rax, %rax # default move
movq $0, %rcx # const
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $10, %rcx # const
movq %rcx, (%rdx) # store
movq $8, %rcx # const
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $0, %rcx # const
movq %rcx, (%rdx) # store
movq %rax, %rax # default move
movq $0, %rcx # const
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
