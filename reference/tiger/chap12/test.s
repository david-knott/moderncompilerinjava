.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
sub $0, %rsp
L3:
movq %rbx, %r9
movq %r12, %r8
movq %r13, %rdx
movq %r14, %rcx
movq 0(%rbp), %rbx
movq %rbx, %rdi
movq L1, %rsi
movq %rsi, %rsi
call print
movq %r9, %rbx
movq %r8, %r12
movq %rdx, %r13
movq %rcx, %r14
jmp L2
L2:

movq %rbp, %rsp
pop %rbp
ret
.data
L1:  db 2,'hi'
L0:  db 2,'hi'

