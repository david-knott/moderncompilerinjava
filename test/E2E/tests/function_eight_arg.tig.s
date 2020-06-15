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
movq $8, %rax # const
pushq %rax # move arg 8 to stack
movq $7, %r10 # const
pushq %r10 # move arg 7 to stack
movq $6, %r11 # const
pushq %r11 # move arg 6 to stack
movq $5, %r9 # const
movq %r9, %r9 # move arg 5 to temp
movq $4, %r8 # const
movq %r8, %r8 # move arg 4 to temp
movq $3, %rcx # const
movq %rcx, %rcx # move arg 3 to temp
movq $2, %rdx # const
movq %rdx, %rdx # move arg 2 to temp
movq $1, %rsi # const
movq %rsi, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
call L0 # default call
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
movq %rdx, %rdx # default move
movq %rcx, %rsi # default move
movq %r8, %rdi # default move
movq %r9, %r8 # default move
movq 16(%rbp), %rcx # load to offset
movq 24(%rbp), %r9 # load to offset
movq 32(%rbp), %r10 # load to offset
movq %rbx, %rbx # default move
movq %r12, %r11 # default move
movq %r13, %r12 # default move
movq %r14, %r13 # default move
movq %r15, %r14 # default move
movq %rax, %rax # mul lexp -> r
movq %rax, %rax # mul r -> rax
imul %rdx # mul rax * rexp 
movq %rax, %rax # mul rax -> r
movq %rax, %rax # add lexp -> r
add %rsi, %rax
movq %rax, %rax # add lexp -> r
add %rdi, %rax
movq %rax, %rax # add lexp -> r
add %r8, %rax
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq %rax, %rax # add lexp -> r
add %r9, %rax
movq %rax, %rax # add lexp -> r
add %r10, %rax
movq %rax, %rax # default move
movq %r14, %r15 # default move
movq %r13, %r14 # default move
movq %r12, %r13 # default move
movq %r11, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
