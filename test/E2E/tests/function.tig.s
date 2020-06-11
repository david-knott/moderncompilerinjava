.globl tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L2:
movl %ebx, %ebx # default move
movl %r12d, %r12d # default move
movl %r13d, %r13d # default move
movl %r14d, %r14d # default move
movl %r15d, %r15d # default move
movl %rbp, %edi
call L0 # default call
movl %eax, %eax # default move
movl %eax, %edi
call itoa # exp call ( no return value )
movl %r15d, %r15d # default move
movl %r14d, %r14d # default move
movl %r13d, %r13d # default move
movl %r12d, %r12d # default move
movl %ebx, %ebx # default move
jmp L1
L1:

# end main
movq %rbp, %rsp
popq %rbp
ret
.text
L0:
pushq %rbp
movq %rsp, %rbp
addq $-4, %rsp
# start main
L4:
movl %ebx, %ebx # default move
movl %r12d, %ecx # default move
movl %r13d, %edx # default move
movl %r14d, %esi # default move
movl %r15d, %edi # default move
movl $0, %eax # const
movl %eax, %eax # add lexp -> r
add %rbp, %eax
movl %eax, (%eax) # default load
movl %edi, %eax # default move
movl $3, %eax # const
movl %eax, %eax # default move
movl %edi, %r15d # default move
movl %esi, %r14d # default move
movl %edx, %r13d # default move
movl %ecx, %r12d # default move
movl %ebx, %ebx # default move
jmp L3
L3:

# end main
movq %rbp, %rsp
popq %rbp
ret
