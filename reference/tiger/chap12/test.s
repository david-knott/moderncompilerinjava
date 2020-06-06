.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
add $0, %rsp
L4:
mov $10, %rax
mov %rax, %rdi
mov $66, %rcx
mov %rcx, %rsi
call initArray
mov %rax, %rcx # visit(MOVE)
mov $0, %rax
mov %rax, %rax # visit(MOVE)
mov $8, %rdx
mov $0, %rsi
mov %rsi, %rax
mul %rdx
mov %rax, %rdx
# add %rsi, %rcx
movq (%rcx), %rax
mov %rax, %rax # visit(MOVE)
jmp L3
L3:

movq %rbp, %rsp
pop %rbp
ret

