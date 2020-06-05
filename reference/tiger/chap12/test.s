.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
add $-8, %rsp
L5:
mov %rbx, %rbx # visit(MOVE)
mov %r12, %r12 # visit(MOVE)
mov %r13, %r13 # visit(MOVE)
mov %r14, %r14 # visit(MOVE)
mov %r15, %rax # visit(MOVE)
movq %rax, %rax # spill
movq %rax, -8(%rbp) # spill
mov $10, %rax
mov %rax, %r15 # visit(MOVE)
L2:
mov $0, %rax
cmp %r15, %rax
jne L3
L0:
mov %r15, %rax
movq -8(%rbp), %rax # spill
movq %rax, %rax # spill
mov %rax, %r15 # visit(MOVE)
mov %r14, %r14 # visit(MOVE)
mov %r13, %r13 # visit(MOVE)
mov %r12, %r12 # visit(MOVE)
mov %rbx, %rbx # visit(MOVE)
jmp L4
L6:
L3:
mov $1, %rax
sub %rax, %r15
mov %r15, %r15 # visit(MOVE)
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

