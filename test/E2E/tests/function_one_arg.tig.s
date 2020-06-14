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
movq $7, %rax # const
movq %rax, %rsi # move arg to temp
movq %rbp, %rdi # move arg to temp
call L0 # default call
movq %rax, %rax # rax to temp 
movq %rax, %rdi # move arg to temp
call itoa # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L1
L1:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L0:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L4:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rbx, %rbx # default move
movq %r12, %rcx # default move
movq %r13, %rdx # default move
movq %r14, %rsi # default move
movq %r15, %rdi # default move
movq $5, %r8 # const
movq %r8, %r8 # add lexp -> r
add %rax, %r8
movq %r8, %rax # default move
movq %rdi, %r15 # default move
movq %rsi, %r14 # default move
movq %rdx, %r13 # default move
movq %rcx, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
