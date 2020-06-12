.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L1:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $8, %rax # const
movq %rax, %rax # default move
movq $2, %rcx # const
movq %rcx, %rcx # default move
movq %rax, %rax # div lexp -> r
movq %rax, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq %rax, %rax # div rax -> r
movq %rax, %rdi
call itoa # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L0
L0:

# end main
movq %rbp, %rsp
popq %rbp
ret
