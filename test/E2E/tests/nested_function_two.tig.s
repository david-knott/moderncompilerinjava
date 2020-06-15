.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L4:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $10, %rax # const
movq %rax, %rsi # move arg to temp
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
jmp L3
L3:
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
L6:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -16(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq %rbp, %rdi # move arg to temp
call L1 # default call
movq %rax, %rax # rax to temp 
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L5
L5:
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
L8:
movq %rdi, -8(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq %rbp, %rdi # move arg to temp
call L2 # default call
movq %rax, %rax # rax to temp 
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L7
L7:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L2:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L10:
movq %rdi, -8(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %r12, %rcx # default move
movq %r13, %rsi # default move
movq %r14, %rdi # default move
movq %r15, %r8 # default move
movq $-8, %rax # const
movq $-8, %r9 # const
movq %r9, %r9 # add lexp -> r
add %rbp, %r9
movq (%r9), %r9 # default load
movq %rax, %rax # add lexp -> r
add %r9, %rax
movq (%rax), %rax # default load
movq $-16, %r9 # const
movq %rax, %rax # add lexp -> r
add %r9, %rax
movq (%rax), %rax # default load
movq $-8, %r9 # const
movq $-8, %r10 # const
movq %r10, %r10 # add lexp -> r
add %rbp, %r10
movq (%r10), %r10 # default load
movq %r9, %r9 # add lexp -> r
add %r10, %r9
movq (%r9), %r9 # default load
movq $-16, %r10 # const
movq %r9, %r9 # add lexp -> r
add %r10, %r9
movq (%r9), %r9 # default load
movq %rax, %rax # div lexp -> r
movq %rax, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %r9 # div rax * rexp 
movq %rax, %rax # div rax -> r
movq %rax, %rax # default move
movq %r8, %r15 # default move
movq %rdi, %r14 # default move
movq %rsi, %r13 # default move
movq %rcx, %r12 # default move
movq %rbx, %rbx # default move
jmp L9
L9:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
