.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L1:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $7, %rax # const
movq %rax, %rax # default move
movq $4, %rcx # const
movq %rcx, %rcx # default move
movq %rax, %rax # minus lexp -> r
sub %rcx, %rax
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L0
L0:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
