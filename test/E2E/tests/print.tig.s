.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L2:
movl %ebx, %ebx # default move
movl %r12d, %r12d # default move
movl %r13d, %r13d # default move
movl %r14d, %r14d # default move
movl %r15d, %r15d # default move
movl $L0, %eax # default name
movl %eax, %edi
call print # exp call ( no return value )
movl %r15d, %r15d # default move
movl %r14d, %r14d # default move
movl %r13d, %r13d # default move
movl %r12d, %r12d # default move
movl %ebx, %ebx # default move
jmp L1
L1:

# end main
movq %rbp, %rsp
popq %rbp
ret
.data
L0:
	.long  0xa
	.ascii "test print"
