.globl tigermain
.text
tigermain:
push %rbp
mov %rsp, %rbp
sub $0, %rsp
L5:
mov $10, %rax
mov %rax, %r8
L2:
mov $0, %rax
cmp %r8, %rax
jne L3
L0:
jmp L4
L6:
L3:
mov $1, %rax
sub %rax, %r8
jmp L2
L4:

mov %rbp, %rsp
pop %rbp
ret
.data
L1:
	.long  0x4
	.ascii "test"

