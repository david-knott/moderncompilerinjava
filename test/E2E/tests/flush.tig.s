.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L1:
movl %ebx, %ebx # default move
movl %r12d, %r12d # default move
movl %r13d, %r13d # default move
movl %r14d, %r14d # default move
movl %r15d, %r15d # default move
call flush # exp call ( no return value )
movl %r15d, %r15d # default move
movl %r14d, %r14d # default move
movl %r13d, %r13d # default move
movl %r12d, %r12d # default move
movl %ebx, %ebx # default move
jmp L0
L0:

# end main
movq %rbp, %rsp
popq %rbp
ret