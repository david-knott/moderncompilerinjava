.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
sub $0, %rsp
L5:
mov %rbx, %rbx # visit(MOVE)
mov %r12, %r8 # visit(MOVE)
mov %r13, %rsi # visit(MOVE)
mov %r14, %rdx # visit(MOVE)
mov %r15, %rcx # visit(MOVE)
mov $10, %rax
mov %rax, %r9 # visit(MOVE)
L2:
mov $0, %rax
cmp %r9, %rax
jne L3
L0:
mov %r9, %rax
mov %rbx, %rbx # visit(MOVE)
mov %r8, %r12 # visit(MOVE)
mov %rsi, %r13 # visit(MOVE)
mov %rdx, %r14 # visit(MOVE)
mov %rcx, %r15 # visit(MOVE)
jmp L4
L6:
L3:
mov $1, %rax
sub %rax, %r9
mov %r9, %r9 # visit(MOVE)
mov $L1, %rax
mov %rax, %rdi
call print
jmp L2
L4:

movq %rbp, %rsp
pop %rbp
ret
.data
L1:
	.long  0x8
	.ascii "beatrice"

