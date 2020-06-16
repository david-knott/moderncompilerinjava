.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L10:
movq %rbx, %rax # default move
movq %r12, %rbx # default move
movq %r13, %r12 # default move
movq %r14, %r13 # default move
movq %r15, %r14 # default move
movq $4, %rcx # const
movq %rcx, %rcx # default move
movq $0, %rdx # const
movq %rdx, %r15 # default move
movq $1, %rdx # const
movq %rcx, %rcx # minus lexp -> r
sub %rdx, %rcx
movq %rcx, %rcx # default move
movq %rcx, %rcx # spill
movq %rcx, -8(%rbp) # spill
movq -8(%rbp), %rcx # spill
movq %rcx, %rcx # spill
cmp %rcx, %r15
jle L6
L7:
L8:
movq %r14, %r15 # default move
movq %r13, %r14 # default move
movq %r12, %r13 # default move
movq %rbx, %r12 # default move
movq %rax, %rbx # default move
jmp L9
L6:
L4:
L5:
movq %r15, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq -8(%rbp), %rcx # spill
movq %rcx, %rcx # spill
cmp %rcx, %r15
je L1
L2:
movq $1, %rcx # const
movq %r15, %rdx # add lexp -> r
add %rcx, %rdx
movq %rdx, %r15 # default move
L3:
jmp L4
L11:
jmp L5
L1:
L0:
jmp L8
L12:
jmp L3
L9:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
