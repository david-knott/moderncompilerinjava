.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
sub $0, %rsp
L1:
mov %rbx, %rbx # visit(MOVE)
mov %r12, %r12 # visit(MOVE)
mov %r13, %r13 # visit(MOVE)
mov %r14, %r14 # visit(MOVE)
mov %r15, %r15 # visit(MOVE)
call flush
mov %r15, %r15 # visit(MOVE)
mov %r14, %r14 # visit(MOVE)
mov %r13, %r13 # visit(MOVE)
mov %r12, %r12 # visit(MOVE)
mov %rbx, %rbx # visit(MOVE)
jmp L0
L0:

movq %rbp, %rsp
pop %rbp
ret

