.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L7:
movq %rbx, %rax # default move
movq %r12, %rbx # default move
movq %r13, %r12 # default move
movq %r14, %r13 # default move
movq %r15, %r14 # default move
movq $10, %rcx # const
movq %rcx, %r15 # default move
L4:
movq $0, %rcx # const
cmp %rcx, %r15
jg L5
L0:
movq %r14, %r15 # default move
movq %r13, %r14 # default move
movq %r12, %r13 # default move
movq %rbx, %r12 # default move
movq %rax, %rbx # default move
jmp L6
L8:
L5:
movq $5, %rcx # const
cmp %rcx, %r15
je L1
L2:
L3:
movq %r15, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq $1, %rcx # const
movq %r15, %rdx # minus lexp -> r
sub %rcx, %rdx
movq %rdx, %r15 # default move
jmp L4
L1:
jmp L0
L9:
jmp L3
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
