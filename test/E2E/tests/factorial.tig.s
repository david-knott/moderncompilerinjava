.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L5:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $5, %rax # const
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
jmp L4
L4:
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
L7:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $1, %rcx # const
cmp %rcx, %rax
je L1
L2:
movq %rax, %rax # default move
movq %rax, %rcx # spill
movq %rcx, -16(%rbp) # spill
movq $1, %rcx # const
movq %rax, %rax # minus lexp -> r
sub %rcx, %rax
movq %rax, %rsi # move arg 1 to temp
movq $0, %rcx # const
movq %rcx, %rcx # add lexp -> r
add %rbp, %rcx
movq (%rcx), %rcx # default load
movq %rcx, %rdi # move arg 0 to temp
call L0 # default call
movq %rax, %rcx # rax to temp 
movq -16(%rbp), %rax # spill
movq %rax, %rax # spill
movq %rax, %rax # mul lexp -> r
movq %rax, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rax # mul rax -> r
movq %rax, %rax # default move
L3:
movq %rax, %rax # default move
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L6
L1:
movq $1, %rax # const
movq %rax, %rax # default move
jmp L3
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
