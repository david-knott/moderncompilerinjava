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
movq $0, %rax # const
movq %rax, %rsi # move arg 1 to temp
movq $10, %rcx # const
movq %rcx, %rdi # move arg 0 to temp
call initArray # default call
movq %rax, %rax # default move
movq %rax, %rcx # default move
movq $0, %rax # const
movq $8, %rdx # const
movq %rax, %rax # mul lexp -> r
movq %rax, %rax # mul r -> rax
imul %rax # mul rax * rexp 
movq %rax, %rax # mul rax -> r
movq %rcx, %rcx # add lexp -> r
add %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
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
