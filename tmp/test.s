.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
L7:
movl %ebx, %ebx # visit(MOVE)
movl %r12d, %r12d # visit(MOVE)
movl %r13d, %r13d # visit(MOVE)
movl %r14d, %r14d # visit(MOVE)
movl %r15d, %r15d # visit(MOVE)
movl $10, %eax
movl %eax, %edi
movl $66, %ecx
movl %ecx, %esi
call initArray
movl %eax, %eax # visit(MOVE)
movl %eax, %ecx # visit(MOVE)
movl $0, %eax
movl %eax, %eax # visit(MOVE)
movl $4, %edx
movl $1, %esi
movl %esi, %eax
mul %edx
movl %eax, %edx
add %esi, %ecx
movl (%ecx), %eax
movl $55, %eax
movl %eax, %eax # visit(MOVE)
movl $4, %edx
movl $1, %esi
movl %esi, %eax
mul %edx
movl %eax, %edx
add %esi, %ecx
movl (%ecx), %eax
movl %eax, %eax # visit(MOVE)
movl %r15d, %r15d # visit(MOVE)
movl %r14d, %r14d # visit(MOVE)
movl %r13d, %r13d # visit(MOVE)
movl %r12d, %r12d # visit(MOVE)
movl %ebx, %ebx # visit(MOVE)
jmp L6
L6:

movq %rbp, %rsp
popq %rbp
ret
