.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $-16, %rsp
# start main
L10:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $10, %rax # const
movq %rax, %rax # default move
movq %rax, %rax # spill s
movq %rax, -8(%rbp) # spill s
L7:
movq $0, %rcx # const
cmp %rcx, %rax
jg L8
L0:
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L9
L11:
L8:
movq $5, %rcx # const
cmp %rcx, %rax
je L1
L2:
L3:
movq $4, %rcx # const
cmp %rcx, %rax
je L4
L5:
L6:
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
movq %rax, -16(%rbp) # spill s
jmp L7
L1:
jmp L0
L12:
jmp L3
L4:
jmp L0
L13:
jmp L6
L9:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
