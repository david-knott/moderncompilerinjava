.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L1:
movq %rbx, %rax # default move
movq %r12, %rbx # default move
movq %r13, %r12 # default move
movq %r14, %r13 # default move
movq %r15, %r14 # default move
movq $3, %rcx # const
movq %rcx, %rcx # default move
movq $4, %rdx # const
movq %rdx, %rdx # default move
movq %rcx, %rcx # add lexp -> r
add %rdx, %rcx
movq %rcx, %rdi # move arg to temp
call printi # exp call ( no return value )
movq %r14, %r15 # default move
movq %r13, %r14 # default move
movq %r12, %r13 # default move
movq %rbx, %r12 # default move
movq %rax, %rbx # default move
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
