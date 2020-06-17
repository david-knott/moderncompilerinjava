.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L2:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $-8, %rax # const
movq %rbp, %rcx # add lexp -> r
add %rax, %rcx
movq $4, %rax # const
movq %rax, (%rcx) # store
movq $3, %rax # const
movq %rax, %rax # default move
movq %rax, %rsi # move arg 1 to temp
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
movq %rbx, %rbx # default move
movq %r12, %rcx # default move
movq %r13, %rsi # default move
movq %r14, %rdi # default move
movq %r15, %r8 # default move
movq $-8, %rdx # const
movq %rdx, %rdx # add lexp -> r
add %rbp, %rdx
movq (%rdx), %rdx # default load
movq $-8, %r9 # const
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
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
