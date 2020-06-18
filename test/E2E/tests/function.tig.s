.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
addq $0, %rsp
# start main
L2:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq %rbp, %rdi # move arg 0 to temp
call L0 # default call
movq %rax, %rax # default move
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
movq %rbx, %rbx # default move
movq %r12, %rcx # default move
movq %r13, %rdx # default move
movq %r14, %rsi # default move
movq %r15, %rdi # default move
movq $3, %rax # const
movq %rax, %rax # default move
movq %rdi, %r15 # default move
movq %rsi, %r14 # default move
movq %rdx, %r13 # default move
movq %rcx, %r12 # default move
movq %rbx, %rbx # default move
jmp L3
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
