.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L77:
movq %rbx, %rbx # default move
movq %rbx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $-8, %rcx # const
movq %rbp, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, %rbx # default move
call getchar # move call
movq %rax, %rax # rax to temp 
movq %rax, (%rbx) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L24 # move call
movq %rax, %r12 # rax to temp 
movq $-8, %rcx # const
movq %rbp, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, %rbx # default move
call getchar # move call
movq %rax, %rax # rax to temp 
movq %rax, (%rbx) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L24 # move call
movq %rax, %rbx # rax to temp 
movq %r12, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
movq %rbx, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
movq %rbp, %rbp # default move
movq %rbx, %rdx # move reg arg 2 to temp
movq %r12, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L25 # move call
movq %rax, %rax # rax to temp 
movq %rax, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
movq -24(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L76
L76:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L27:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L79:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, %rax # spill store
movq %rax, -16(%rbp) # spill store
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
movq %rax, %rax # spill load
cmp %rcx, %rax
je L73
L74:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
movq %rax, %rax # spill load
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move reg arg 0 to temp
call L26 # exp call ( no return value )
movq $L70, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
movq $8, %rcx # const
movq -16(%rbp), %rax # spill load
movq %rax, %rax # spill load
movq %rax, %rax # add lexp -> r
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
L75:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L78
L73:
movq $L67, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
jmp L75
L78:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L70:
	.long  0x1
	.ascii " "
.data
L67:
	.long  0x2
	.ascii "\n"
.text
L26:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L81:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rcx # default move
movq %rcx, %rax # spill store
movq %rax, -16(%rbp) # spill store
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $0, %rax # const
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
cmp %rax, %rcx
jl L64
L65:
movq $0, %rax # const
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
cmp %rax, %rcx
jg L61
L62:
movq $L60, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
L63:
L66:
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L80
L64:
movq $L59, %rax # default name
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
movq $0, %rax # const
movq %rax, %rax # minus lexp -> r
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
sub %rcx, %rax
movq %rax, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L54 # exp call ( no return value )
jmp L66
L61:
movq -16(%rbp), %rax # spill load
movq %rax, %rcx # spill load
movq %rcx, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L54 # exp call ( no return value )
jmp L63
L80:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L60:
	.long  0x1
	.ascii "0"
.data
L59:
	.long  0x1
	.ascii "-"
.text
L54:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L83:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rsi # default move
movq %rsi, %rcx # spill store
movq %rcx, -24(%rbp) # spill store
movq %rbx, %rbx # default move
movq %rbx, %rbx # spill store
movq %rbx, -32(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -40(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $0, %rcx # const
movq -24(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
cmp %rcx, %rsi
jg L56
L57:
L58:
movq -32(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq -40(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r12 # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L82
L56:
movq $10, %rbx # const
movq -24(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rbx # div rax * rexp 
movq %rax, %rax # div rax -> r
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rax # const
movq %rbp, %rbx # minus lexp -> r
sub %rax, %rbx
movq (%rbx), %rax # default load
movq %rax, %rdi # move reg arg 0 to temp
call L54 # exp call ( no return value )
movq $10, %rbx # const
movq -24(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rbx # div rax * rexp 
movq %rax, %rax # div rax -> r
movq $10, %rbx # const
movq %rax, %rax # imul l -> rax
imul %rbx # imul rax * r 
movq %rax, %rax # imul rax -> t
movq -24(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq %rsi, %rbx # minus lexp -> r
sub %rax, %rbx
movq %rbx, %r12 # default move
movq $L55, %rbx # default name
movq %rbx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
movq %r12, %rbx # add lexp -> r
add %rax, %rbx
movq %rbx, %rdi # move reg arg 0 to temp
call chr # move call
movq %rax, %rax # rax to temp 
movq %rax, %rdi # move reg arg 0 to temp
call print # exp call ( no return value )
jmp L58
L82:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L55:
	.long  0x1
	.ascii "0"
.text
L25:
pushq %rbp
movq %rsp, %rbp
subq $48, %rsp
# start main
# sink 
L85:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rsi # default move
movq %rsi, %rcx # spill store
movq %rcx, -48(%rbp) # spill store
movq %rdx, %rsi # default move
movq %rsi, %rcx # spill store
movq %rcx, -40(%rbp) # spill store
movq %rbx, %rbx # default move
movq %rbx, %rbx # spill store
movq %rbx, -32(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $0, %rbx # const
movq -48(%rbp), %rcx # spill load
movq %rcx, %rsi # spill load
cmp %rbx, %rsi
je L51
L52:
movq $0, %rbx # const
movq -40(%rbp), %rcx # spill load
movq %rcx, %rsi # spill load
cmp %rbx, %rsi
je L48
L49:
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
jl L45
L46:
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r13 # rax to temp 
movq -40(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq 0(%rsi), 0(%r13) # mem to mem
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
movq %rsi, %rsi # move reg arg 1 to temp
movq $8, %rbx # const
movq %rbp, %rcx # minus lexp -> r
sub %rbx, %rcx
movq (%rcx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call L25 # move call
movq %rax, %rax # rax to temp 
movq %rax, (%r12) # store
movq %r13, %rax # default move
L47:
movq %rax, %rax # default move
L50:
movq %rax, %rax # default move
L53:
movq %rax, %rax # default move
movq -32(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq -24(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r12 # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L84
L51:
movq -40(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # default move
jmp L53
L48:
movq -48(%rbp), %rax # spill load
movq %rax, %rsi # spill load
movq %rsi, %rax # default move
jmp L50
L45:
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r13 # rax to temp 
movq -48(%rbp), %rbx # spill load
movq %rbx, %rsi # spill load
movq 0(%rsi), 0(%r13) # mem to mem
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
call L25 # move call
movq %rax, %rax # rax to temp 
movq %rax, (%r12) # store
movq %r13, %rax # default move
jmp L47
L84:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L24:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L87:
movq %rdi, -8(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %rbx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -40(%rbp) # spill store
movq %r14, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -32(%rbp) # spill store
movq %r15, %r15 # default move
movq $8, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %rax # rax to temp 
movq $0, %rbx # const
movq %rbx, 0(%rax) # store to offset
movq %rax, %r12 # default move
movq %r12, %rsi # move reg arg 1 to temp
movq $8, %rbx # const
movq %rbp, %rcx # minus lexp -> r
sub %rbx, %rcx
movq (%rcx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call L0 # move call
movq %rax, %r13 # rax to temp 
movq $0, %rcx # const
movq %r12, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq $1, %rcx # const
cmp %rcx, %rbx
je L30
L31:
movq $0, %rax # const
movq %rax, %rax # default move
L32:
movq %rax, %rax # default move
movq -24(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r12 # default move
movq -40(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r13 # default move
movq -32(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r14 # default move
movq %r15, %r15 # default move
jmp L86
L30:
movq $16, %rbx # const
movq %rbx, %rdi # move reg arg 0 to temp
call initRecord # move call
movq %rax, %r14 # rax to temp 
movq %r13, 0(%r14) # store to offset
movq $8, %rbx # const
movq %r14, %rcx # add lexp -> r
add %rbx, %rcx
movq %rcx, %r12 # default move
movq $8, %rbx # const
movq %rbp, %rcx # minus lexp -> r
sub %rbx, %rcx
movq (%rcx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call L24 # move call
movq %rax, %rax # rax to temp 
movq %rax, (%r12) # store
movq %r14, %rax # default move
jmp L32
L86:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L89:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rdx # default move
movq %rdx, %rax # spill store
movq %rax, -32(%rbp) # spill store
movq %rbx, %rbx # default move
movq %rbx, %rax # spill store
movq %rax, -24(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rax # spill store
movq %rax, -16(%rbp) # spill store
movq %r13, %rcx # default move
movq %rcx, %rax # spill store
movq %rax, -40(%rbp) # spill store
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $0, %rax # const
movq %rax, %r13 # default move
movq %rbp, %rdi # move reg arg 0 to temp
call L2 # exp call ( no return value )
movq $0, %rcx # const
movq -32(%rbp), %rbx # spill load
movq %rbx, %rdx # spill load
movq %rdx, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, %r12 # default move
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L1 # move call
movq %rax, %rax # rax to temp 
movq %rax, (%r12) # store
L22:
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L1 # move call
movq %rax, %rax # rax to temp 
movq $1, %rbx # const
cmp %rbx, %rax
je L23
L20:
movq %r13, %rax # default move
movq -24(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r12 # default move
movq -40(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L88
L23:
movq $10, %rbx # const
movq %r13, %rax # imul l -> rax
imul %rbx # imul rax * r 
movq %rax, %rax # imul rax -> t
movq %rax, %r12 # default move
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
movq %r12, %rbx # add lexp -> r
add %rax, %rbx
movq %rbx, %r12 # default move
movq $L21, %rbx # default name
movq %rbx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
movq %r12, %rbx # minus lexp -> r
sub %rax, %rbx
movq %rbx, %r13 # default move
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, %rbx # default move
call getchar # move call
movq %rax, %rax # rax to temp 
movq %rax, (%rbx) # store
jmp L22
L88:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L21:
	.long  0x1
	.ascii "0"
.text
L2:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L91:
movq %rdi, -8(%rbp) # store to offset
movq %rbx, %rbx # default move
movq %rbx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r15, %r15 # default move
L16:
movq $L10, %rbx # default name
movq %rbx, %rsi # move reg arg 1 to temp
movq $-8, %rdx # const
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rcx # default load
movq %rdx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call stringEqual # move call
movq %rax, %rax # rax to temp 
movq $1, %rbx # const
cmp %rbx, %rax
je L12
L13:
movq $L11, %rbx # default name
movq %rbx, %rsi # move reg arg 1 to temp
movq $-8, %rdx # const
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rcx # default load
movq %rdx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call stringEqual # move call
movq %rax, %rax # rax to temp 
movq $1, %rbx # const
cmp %rbx, %rax
je L17
L15:
movq -24(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq %r12, %r12 # default move
movq %r13, %r13 # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r14 # default move
movq %r15, %r15 # default move
jmp L90
L12:
L17:
movq $-8, %rdx # const
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rcx # default load
movq %rdx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq %rbx, %rbx # default move
call getchar # move call
movq %rax, %rax # rax to temp 
movq %rax, (%rbx) # store
jmp L16
L90:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L11:
	.long  0x2
	.ascii "\n"
.data
L10:
	.long  0x1
	.ascii " "
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L93:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rcx # default move
movq %rbx, %rbx # default move
movq %rbx, %rbx # spill store
movq %rbx, -24(%rbp) # spill store
movq %r12, %rcx # default move
movq %rcx, %rbx # spill store
movq %rbx, -16(%rbp) # spill store
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
movq $-8, %rcx # const
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rdx # default load
movq %rcx, %rbx # add lexp -> r
add %rdx, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
movq %rax, %rbx # default move
movq $L3, %rcx # default name
movq %rcx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
cmp %rax, %rbx
jge L5
L6:
movq $0, %rax # const
movq %rax, %rax # default move
L7:
movq %rax, %rax # default move
movq -24(%rbp), %rbx # spill load
movq %rbx, %rbx # spill load
movq %rbx, %rbx # default move
movq -16(%rbp), %rcx # spill load
movq %rcx, %rcx # spill load
movq %rcx, %r12 # default move
movq %r13, %r13 # default move
movq %r14, %r14 # default move
movq %r15, %r15 # default move
jmp L92
L5:
movq $1, %rbx # const
movq %rbx, %r12 # default move
movq $-8, %rdx # const
movq $-8, %rbx # const
movq %rbx, %rbx # add lexp -> r
add %rbp, %rbx
movq (%rbx), %rcx # default load
movq %rdx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq $-8, %rcx # const
movq %rbx, %rbx # add lexp -> r
add %rcx, %rbx
movq (%rbx), %rbx # default load
movq %rbx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
movq %rax, %rbx # default move
movq $L4, %rcx # default name
movq %rcx, %rdi # move reg arg 0 to temp
call ord # move call
movq %rax, %rax # rax to temp 
cmp %rax, %rbx
jle L8
L9:
movq $0, %rax # const
movq %rax, %r12 # default move
L8:
movq %r12, %rax # default move
jmp L7
L92:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L4:
	.long  0x1
	.ascii "9"
.data
L3:
	.long  0x1
	.ascii "0"
