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
pushq %rax # move arg to stack
movq $6, %r10 # const
pushq %r10 # move arg to stack
movq $5, %r9 # const
movq %r9, %r9 # move arg to temp
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
movq %rsi, %rsi # default move
movq %rdx, %rdx # default move
movq %rcx, %rdi # default move
movq %r8, %r8 # default move
movq %r9, %r9 # default move
movq 16(%rbp), %rax # load to offset
movq 24(%rbp), %rcx # load to offset
movq %rbx, %rbx # default move
movq %r12, %r10 # default move
movq %r13, %r11 # default move
movq %r14, %r12 # default move
movq %r15, %r13 # default move
movq %rsi, %rsi # add lexp -> r
add %rdx, %rsi
movq %rsi, %rdx # add lexp -> r
add %rdi, %rdx
movq %rdx, %rdx # add lexp -> r
add %r8, %rdx
movq %rdx, %rdx # add lexp -> r
add %r9, %rdx
movq %rdx, %rdx # add lexp -> r
add %rax, %rdx
movq %rdx, %rax # add lexp -> r
add %rcx, %rax
movq %rax, %rax # default move
movq %r13, %r15 # default move
movq %r12, %r14 # default move
movq %r11, %r13 # default move
movq %r10, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
