.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
add $0, %rsp
L7:
mov %rbx, %rax # visit(MOVE)
mov %r12, %rbx # visit(MOVE)
mov %r13, %rcx # visit(MOVE)
mov %r14, %rdx # visit(MOVE)
mov %r15, %rsi # visit(MOVE)
mov $10, %rdi
mov %rdi, %rdi # visit(MOVE)
L4:
mov $0, %r8
cmp %r8, %rdi
jg L5
L0:
mov %rdi, %rdi
mov %rsi, %r15 # visit(MOVE)
mov %rdx, %r14 # visit(MOVE)
mov %rcx, %r13 # visit(MOVE)
mov %rbx, %r12 # visit(MOVE)
mov %rax, %rbx # visit(MOVE)
jmp L6
L8:
L5:
mov $5, %r8
cmp %r8, %rdi
je L1
L2:
mov $1, %r8
sub %r8, %rdi
mov %rdi, %rdi # visit(MOVE)
L3:
jmp L4
L1:
jmp L0
L9:
jmp L3
L6:

movq %rbp, %rsp
pop %rbp
ret

