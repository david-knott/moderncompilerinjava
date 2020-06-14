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
movq $5, %rax # const
movq %rax, %r9 # move arg to temp
movq $4, %r8 # const
movq %r8, %r8 # move arg to temp
movq $3, %rcx # const
movq %rcx, %rcx # move arg to temp
movq $2, %rdx # const
movq %rdx, %rdx # move arg to temp
movq $1, %rsi # const
movq %rsi, %rsi # move arg to temp
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
movq %rdx, %rdx # default move
movq %rcx, %rcx # default move
movq %r8, %rsi # default move
movq %r9, %rdi # default move
movq %rbx, %rbx # default move
movq %r12, %r8 # default move
movq %r13, %r9 # default move
movq %r14, %r10 # default move
movq %r15, %r11 # default move
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq %rax, %rax # add lexp -> r
add %rsi, %rax
movq %rax, %rax # add lexp -> r
add %rdi, %rax
movq %rax, %rax # default move
movq %r11, %r15 # default move
movq %r10, %r14 # default move
movq %r9, %r13 # default move
movq %r8, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
