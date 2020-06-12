.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L2:
movq %rbx, %rax # default move
movq %r12, %rbx # default move
movq %r13, %r12 # default move
movq %r14, %r13 # default move
movq %r15, %r14 # default move
movq $L0, %rcx # default name
movq %rcx, %rdi # move arg to temp
call print # exp call ( no return value )
movq %r14, %r15 # default move
movq %r13, %r14 # default move
movq %r12, %r13 # default move
movq %rbx, %r12 # default move
movq %rax, %rbx # default move
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
