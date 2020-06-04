.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
sub $0, %rsp
L2:
movq %rbx, %rbx
movq %r12, %rsi
movq %r13, %rdx
movq %r14, %rcx
movq $L0, %rdi
movq %rdi, %rdi
call print
movq %rbx, %rbx
movq %rsi, %r12
movq %rdx, %r13
movq %rcx, %r14
jmp L1
L1:

movq %rbp, %rsp
pop %rbp
ret
.data
L0:
	.long  0xe
	.ascii "hi david knott"

