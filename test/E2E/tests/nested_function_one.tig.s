.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L3:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $10, %rax # const
movq %rax, %rsi # move arg 1 to temp
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
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L0:
pushq %rbp
movq %rsp, %rbp
addq $-16, %rsp
# start main
L5:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -16(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq %rbp, %rdi # move arg 0 to temp
call L1 # default call
movq %rax, %rax # rax to temp 
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L4
L4:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L1:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L7:
movq %rdi, -8(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %r12, %rcx # default move
movq %r13, %rsi # default move
movq %r14, %rdi # default move
movq %r15, %r8 # default move
movq $-8, %rax # const
movq %rax, %rax # add lexp -> r
add %rbp, %rax
movq (%rax), %rax # default load
movq $-16, %rdx # const
movq %rax, %rax # add lexp -> r
add %rdx, %rax
movq (%rax), %rax # default load
movq $-8, %rdx # const
movq %rdx, %rdx # add lexp -> r
add %rbp, %rdx
movq (%rdx), %rdx # default load
movq $-16, %r9 # const
movq %rdx, %rdx # add lexp -> r
add %r9, %rdx
movq (%rdx), %rdx # default load
movq %rax, %rax # mul lexp -> r
movq %rax, %rax # mul r -> rax
imul %rdx # mul rax * rexp 
movq %rax, %rax # mul rax -> r
movq %rax, %rax # default move
movq %r8, %r15 # default move
movq %rdi, %r14 # default move
movq %rsi, %r13 # default move
movq %rcx, %r12 # default move
movq %rbx, %rbx # default move
jmp L6
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
