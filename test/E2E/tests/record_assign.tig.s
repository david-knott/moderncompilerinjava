.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $-8, %rsp
# start main
L9:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $24, %rax # const
movq %rax, %rdi # move arg 0 to temp
call initRecord # move call
movq %rax, %rax # rax to temp 
movq $0, %rcx # const
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $10, %rcx # const
movq %rcx, (%rdx) # store
movq $8, %rcx # const
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $20, %rcx # const
movq %rcx, (%rdx) # store
movq $16, %rcx # const
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $0, %rcx # const
movq %rcx, (%rdx) # store
movq %rax, %rax # default move
movq %rax, %rax # spill s
movq %rax, -8(%rbp) # spill s
movq $0, %rcx # const
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq $20, %rcx # const
movq %rcx, (%rax) # store
movq $8, %rcx # const
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq $21, %rcx # const
movq %rcx, (%rax) # store
movq $0, %rcx # const
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq $8, %rcx # const
movq -8(%rbp), %rax # spill l
movq %rax, %rax # spill l
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
call printi # exp call ( no return value )
movq %r15, %r15 # default move
movq %r14, %r14 # default move
movq %r13, %r13 # default move
movq %r12, %r12 # default move
movq %rbx, %rbx # default move
jmp L8
L8:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
