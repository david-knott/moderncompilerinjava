.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
# start main
# sink 
L47:
movq %rbx, -40(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -32(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r14, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r15, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -8(%rbp) # spill store
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r15 # rax to temp 
movq $2, %rbx # const
movq %rbx, 0(%r15) # store to offset
movq $8, %rbx # const
movq %r15, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r14 # default move
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r13 # rax to temp 
movq $4, %rbx # const
movq %rbx, 0(%r13) # store to offset
movq $8, %rbx # const
movq %r13, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r12 # default move
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq $6, %rbx # const
movq %rbx, 0(%rax) # store to offset
movq $0, %rbx # const
movq %rbx, 8(%rax) # store to offset
movq %rax, (%r12) # store
movq %r13, (%r14) # store
movq %r15, %rbx # default move
movq %rbx, -48(%rbp) # spill store
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r15 # rax to temp 
movq $3, %rbx # const
movq %rbx, 0(%r15) # store to offset
movq $8, %rbx # const
movq %r15, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r14 # default move
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r13 # rax to temp 
movq $5, %rbx # const
movq %rbx, 0(%r13) # store to offset
movq $8, %rbx # const
movq %r13, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r12 # default move
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq $7, %rbx # const
movq %rbx, 0(%rax) # store to offset
movq $0, %rbx # const
movq %rbx, 8(%rax) # store to offset
movq %rax, (%r12) # store
movq %r13, (%r14) # store
movq %r15, %rbx # default move
movq %rbx, %rdx # move reg arg 2 to temp
movq -48(%rbp), %rbx # spill load
movq %rbx, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L0 # move call
movq %rax, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L2 # exp call ( no return value )
movq -40(%rbp), %rbx # spill load
movq -32(%rbp), %rcx # spill load
movq %rcx, %r12 # default move
movq -24(%rbp), %rcx # spill load
movq %rcx, %r13 # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %r14 # default move
movq -8(%rbp), %rcx # spill load
movq %rcx, %r15 # default move
jmp L46
L46:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L2:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L49:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
je L43
L44:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move reg arg 0 to temp
call L1 # exp call ( no return value )
movq $L40, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
movq $8, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move reg arg 0 to temp
call L2 # exp call ( no return value )
L45:
jmp L48
L43:
movq $L37, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
jmp L45
L48:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L40:
	.long  0x1
	.ascii " "
.data
L37:
	.long  0x2
	.ascii "\n"
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L51:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rcx # default move
movq %rcx, %rax # spill store
movq %rax, -16(%rbp) # spill store
movq $0, %rax # const
movq -16(%rbp), %rcx # spill load
cmp %rax, %rcx
jl L34
L35:
movq $0, %rax # const
movq -16(%rbp), %rcx # spill load
cmp %rax, %rcx
jg L31
L32:
movq $L30, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
L33:
L36:
jmp L50
L34:
movq $L29, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
movq $0, %rax # const
movq -16(%rbp), %rcx # spill load
sub %rcx, %rax
movq %rax, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L24 # exp call ( no return value )
jmp L36
L31:
movq -16(%rbp), %rax # spill load
movq %rax, %rcx # spill load
movq %rcx, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L24 # exp call ( no return value )
jmp L33
L50:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L30:
	.long  0x1
	.ascii "0"
.data
L29:
	.long  0x1
	.ascii "-"
.text
L24:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L53:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rcx # spill store
movq %rcx, -24(%rbp) # spill store
movq %rbx, -32(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -40(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq $0, %rcx # const
movq -24(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
cmp %rcx, %rsi
jg L26
L27:
L28:
movq -32(%rbp), %rbx # spill load
movq -40(%rbp), %rcx # spill load
movq %rcx, %r12 # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %r13 # default move
jmp L52
L26:
movq $10, %rbx # const
movq -24(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rbx # div rax * rexp 
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rax # const
movq %rbp, %rbx # minus lexp -> r
sub %rax, %rbx
movq (%rbx), %rax # default load
movq %rax, %rdi # move reg arg 0 to temp
call L24 # exp call ( no return value )
movq $10, %rbx # const
movq -24(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rbx # div rax * rexp 
movq $10, %rbx # const
imul %rbx # imul rax * r 
movq -24(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # minus lexp -> r
sub %rax, %rbx
movq %rbx, %r12 # default move
movq $L25, %rbx # default name
movq %rbx, %rdi # move reg arg 0 to temp
call ord # move call
movq %r12, %rbx # add lexp -> r
add %rax, %rbx
movq %rbx, %rdi # move reg arg 0 to temp
call chr # move call
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
jmp L28
L52:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L25:
	.long  0x1
	.ascii "0"
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
# start main
# sink 
L55:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rcx # spill store
movq %rcx, -48(%rbp) # spill store
movq %rdx, %rsi # default move
movq %rsi, %rcx # spill store
movq %rcx, -40(%rbp) # spill store
movq %rbx, -32(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq $0, %rbx # const
movq -48(%rbp), %rcx # spill load
movq %rcx, %rsi # spill load
cmp %rbx, %rsi
je L21
L22:
movq $0, %rbx # const
movq -40(%rbp), %rcx # spill load
movq %rcx, %rsi # spill load
cmp %rbx, %rsi
je L18
L19:
movq $0, %rcx # const
movq -48(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rdx # default load
movq $0, %rcx # const
movq -40(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
cmp %rbx, %rdx
jl L15
L16:
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r13 # rax to temp 
movq $0, %rbx # const
movq %r13, %rdx # add lexp -> r
add %rbx, %rdx
movq $0, %rcx # const
movq -40(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, (%rdx) # temp to mem
movq $8, %rbx # const
movq %r13, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r12 # default move
movq $8, %rcx # const
movq -40(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rdx # move reg arg 2 to temp
movq -48(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq $8, %rbx # const
movq %rbp, %rcx # minus lexp -> r
sub %rbx, %rcx
movq (%rcx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call L0 # move call
movq %rax, (%r12) # store
movq %r13, %rax # default move
L17:
L20:
L23:
movq -32(%rbp), %rbx # spill load
movq -24(%rbp), %rcx # spill load
movq %rcx, %r12 # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %r13 # default move
jmp L54
L21:
movq -40(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # default move
jmp L23
L18:
movq -48(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # default move
jmp L20
L15:
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r13 # rax to temp 
movq $0, %rbx # const
movq %r13, %rdx # add lexp -> r
add %rbx, %rdx
movq $0, %rcx # const
movq -48(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, (%rdx) # temp to mem
movq $8, %rbx # const
movq %r13, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r12 # default move
movq -40(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rdx # move reg arg 2 to temp
movq $8, %rcx # const
movq -48(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rsi # move reg arg 1 to temp
movq $8, %rbx # const
movq %rbp, %rcx # minus lexp -> r
sub %rbx, %rcx
movq (%rcx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call L0 # move call
movq %rax, (%r12) # store
movq %r13, %rax # default move
jmp L17
L54:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
