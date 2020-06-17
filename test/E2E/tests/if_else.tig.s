.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L6:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $2, %rax # const
movq %rax, %rax # default move
movq $2, %rcx # const
cmp %rcx, %rax
je L2
L3:
movq $L1, %rax # default name
movq %rax, %rdi # move arg 0 to temp
call print # exp call ( no return value )
L4:
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L5
L2:
movq $L0, %rax # default name
movq %rax, %rdi # move arg 0 to temp
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
