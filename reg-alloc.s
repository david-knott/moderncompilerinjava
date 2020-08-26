.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L2:
movq %rbx, %rbx # move(t, t)
movq %r12, %r12 # move(t, t)
movq %r13, %r13 # move(t, t)
movq %r14, %r14 # move(t, t)
movq %r15, %r15 # move(t, t)
movq $1, %rsi # integerExpression
movq $2, %rdx # integerExpression
movq $3, %rcx # integerExpression
movq $4, %r8 # integerExpression
movq $5, %r9 # integerExpression
movq $6, %rax # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
movq %rsi, %rsi # move reg arg 1 to temp
movq %rdx, %rdx # move reg arg 2 to temp
movq %rcx, %rcx # move reg arg 3 to temp
movq %r8, %r8 # move reg arg 4 to temp
movq %r9, %r9 # move reg arg 5 to temp
pushq %rax # move reg arg 6 to stack
call L0
movq %rax, %rax # rax to temp 
movq %rax, %rdi # move reg arg 0 to temp
call printi
movq %rbx, %rbx # move(t, t)
movq %r12, %r12 # move(t, t)
movq %r13, %r13 # move(t, t)
movq %r14, %r14 # move(t, t)
movq %r15, %r15 # move(t, t)
movq $L1, %rcx # default name
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
subq $8, %rsp
# start main
# start 
L4:
movq $-8, %rax # bin(i, t)
movq %rax, %rax # add lexp -> r
add %rbp, %rax
movq %rdi, (%rax) # store
movq %rsi, %rsi # move(t, t)
movq %rdx, %rdx # move(t, t)
movq %rcx, %rcx # move(t, t)
movq %r8, %r8 # move(t, t)
movq %r9, %r9 # move(t, t)
movq 16(%rbp), %rdi # add lexp -> r
movq %rdi, %rdi # move(t, t)
movq %rbx, %rbx # move(t, t)
movq %r12, %r12 # move(t, t)
movq %r13, %r13 # move(t, t)
movq %r14, %r14 # move(t, t)
movq %r15, %r15 # move(t, t)
movq %rsi, %rsi # add lexp -> r
add %rdx, %rsi
movq %rsi, %rsi # add lexp -> r
add %rcx, %rsi
movq %rsi, %rsi # add lexp -> r
add %r8, %rsi
movq %rsi, %rsi # add lexp -> r
add %r9, %rsi
movq %rsi, %rax # add lexp -> r
add %rdi, %rax
movq %rax, %rax # move(t, t)
movq %rbx, %rbx # move(t, t)
movq %r12, %r12 # move(t, t)
movq %r13, %r13 # move(t, t)
movq %r14, %r14 # move(t, t)
movq %r15, %r15 # move(t, t)
movq $L3, %rcx # default name
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
