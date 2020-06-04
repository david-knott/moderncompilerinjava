.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
sub $0, %rsp
L1:
movq %rbx, %rbx
movq %r12, %rsi
movq %r13, %rdx
movq %r14, %rcx
movq $80, %rax
movq %rax, %rax
movq %rax, %rdi
call chr
movq %rax, %rax
movq %rax, %rdi
call print
movq %rbx, %rbx
movq %rsi, %r12
movq %rdx, %r13
movq %rcx, %r14
jmp L0
L0:

movq %rbp, %rsp
pop %rbp
ret

