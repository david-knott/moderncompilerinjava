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
movq $9, %rax # const
movq %rax, %rdx # move arg 2 to temp
movq $5, %rcx # const
movq %rcx, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
call L0 # move call
movq %rax, %rax # rax to temp 
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
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
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
