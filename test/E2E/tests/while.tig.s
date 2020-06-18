.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L4:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $4, %rax # const
movq %rax, %rax # default move
movq %rax, %rax # spill s
movq %rax, -8(%rbp) # spill s
L1:
movq $0, %rcx # const
cmp %rcx, %rax
jg L2
L0:
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L5:
L2:
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
movq %rax, %rdi # move arg 0 to temp
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
call printi # exp call ( no return value )
movq $1, %rcx # const
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
movq %rax, %rax # minus lexp -> r
sub %rcx, %rax
movq %rax, %rax # default move
movq %rax, %rax # spill s
movq %rax, -8(%rbp) # spill s
jmp L1
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
