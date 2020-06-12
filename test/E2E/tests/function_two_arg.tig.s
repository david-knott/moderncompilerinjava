.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L2:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq %rbp, %rdi # move arg to temp
movq $5, %rax # const
movq %rax, %rsi # move arg to temp
movq $9, %rcx # const
movq %rcx, %rdx # move arg to temp
call L0 # default call
movq %rax, %rax # default move
movq %rax, %rdi # move arg to temp
call itoa # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
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
addq $-8, %rsp
# start main
L4:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rdx, %rcx # default move
movq %rbx, %rbx # default move
movq %r12, %rdx # default move
movq %r13, %rsi # default move
movq %r14, %rdi # default move
movq %r15, %r8 # default move
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq %rax, %rax # default move
movq %r8, %r15 # default move
movq %rdi, %r14 # default move
movq %rsi, %r13 # default move
movq %rdx, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L3:

# end main
movq %rbp, %rsp
popq %rbp
ret