.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L6:
movq %rbx, %rax # default move
movq %r12, %rbx # default move
movq %r13, %r12 # default move
movq %r14, %r13 # default move
movq %r15, %r14 # default move
movq $2, %rcx # const
movq %rcx, %rcx # default move
movq $1, %rdx # const
cmp %rdx, %rcx
je L2
L3:
movq $L1, %rcx # default name
movq %rcx, %rdi # move arg 0 to temp
call print # exp call ( no return value )
L4:
movq %r14, %r15 # default move
movq %r13, %r14 # default move
movq %r12, %r13 # default move
movq %rbx, %r12 # default move
movq %rax, %rbx # default move
jmp L5
L2:
movq $L0, %rcx # default name
movq %rcx, %rdi # move arg 0 to temp
call print # exp call ( no return value )
jmp L4
L5:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L1:
	.long  0x4
	.ascii "else"
.data
L0:
	.long  0x4
	.ascii "then"
