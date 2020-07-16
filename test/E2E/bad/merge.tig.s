.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L77:
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $-8, %rcx # const
movq %rbp, %r14 # add lexp -> r
add %rcx, %r14
call getchar # move call
movq %rax, (%r14) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L24 # move call
movq %rax, %r14 # rax to temp 
movq $-8, %rcx # const
movq %rbp, %r15 # add lexp -> r
add %rcx, %r15
call getchar # move call
movq %rax, (%r15) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L24 # move call
movq %rax, %r15 # rax to temp 
movq %r14, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
movq %r15, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
movq %r15, %rdx # move reg arg 2 to temp
movq %r14, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L25 # move call
movq %rax, %rsi # rax to temp 
movq %rbp, %rdi # move reg arg 0 to temp
call L27 # exp call ( no return value )
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
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
movq %rsi, -16(%rbp) # spill store
movq $0, %rax # const
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
je L73
L74:
movq $0, %rax # const
movq -16(%rbp), %rsi # spill load
add %rax, %rsi
movq (%rsi), %rsi # default load
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rdi # default load
call L26 # exp call ( no return value )
movq $L70, %rdi # default name
call print # exp call ( no return value )
movq $8, %rax # const
movq -16(%rbp), %rsi # spill load
add %rax, %rsi
movq (%rsi), %rsi # default load
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rdi # default load
call L27 # exp call ( no return value )
L75:
jmp L78
L73:
movq $L67, %rdi # default name
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
movq %rsi, -16(%rbp) # spill store
movq $0, %rax # const
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
jl L64
L65:
movq $0, %rax # const
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
jg L61
L62:
movq $L60, %rdi # default name
call print # exp call ( no return value )
L63:
L66:
jmp L80
L64:
movq $L59, %rdi # default name
call print # exp call ( no return value )
movq $0, %rax # const
movq -16(%rbp), %rsi # spill load
sub %rsi, %rax
movq %rax, %rsi # move reg arg 1 to temp
movq %rbp, %rdi # move reg arg 0 to temp
call L54 # exp call ( no return value )
jmp L66
L61:
movq -16(%rbp), %rsi # spill load
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
subq $24, %rsp
# start main
# sink 
L83:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %rcx # const
movq -24(%rbp), %rsi # spill load
cmp %rcx, %rsi
jg L56
L57:
L58:
movq -16(%rbp), %r15 # spill load
jmp L82
L56:
movq $10, %rcx # const
movq -24(%rbp), %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq %rax, %rsi # move reg arg 1 to temp
movq $8, %rcx # const
movq %rbp, %rax # minus lexp -> r
sub %rcx, %rax
movq (%rax), %rdi # default load
call L54 # exp call ( no return value )
movq $10, %rcx # const
movq -24(%rbp), %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq $10, %rcx # const
imul %rcx # imul rax * r 
movq -24(%rbp), %rsi # spill load
sub %rax, %rsi
movq %rsi, %r15 # default move
movq $L55, %rdi # default name
call ord # move call
movq %r15, %rdi # add lexp -> r
add %rax, %rdi
call chr # move call
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
subq $40, %rsp
# start main
# sink 
L85:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -40(%rbp) # spill store
movq %rdx, -32(%rbp) # spill store
movq %r12, -24(%rbp) # spill store
movq %r13, -16(%rbp) # spill store
movq $0, %rcx # const
movq -40(%rbp), %rsi # spill load
cmp %rcx, %rsi
je L51
L52:
movq $0, %rcx # const
movq -32(%rbp), %rdx # spill load
cmp %rcx, %rdx
je L48
L49:
movq $0, %rcx # const
movq -40(%rbp), %rsi # spill load
add %rcx, %rsi
movq (%rsi), %rcx # default load
movq $0, %rsi # const
movq -32(%rbp), %rdx # spill load
add %rsi, %rdx
movq (%rdx), %rdx # default load
cmp %rdx, %rcx
jl L45
L46:
movq $16, %rdi # const
call initRecord # move call
movq %rax, %r12 # rax to temp 
movq $0, %rdx # const
movq %r12, %rcx # add lexp -> r
add %rdx, %rcx
movq $0, %rsi # const
movq -32(%rbp), %rdx # spill load
add %rsi, %rdx
movq (%rdx), %rdx # mem to temp
movq %rdx, (%rcx) # temp to mem
movq $8, %rcx # const
movq %r12, %r13 # add lexp -> r
add %rcx, %r13
movq $8, %rcx # const
movq -32(%rbp), %rdx # spill load
add %rcx, %rdx
movq (%rdx), %rdx # default load
movq -40(%rbp), %rsi # spill load
movq $8, %rdi # const
movq %rbp, %rcx # minus lexp -> r
sub %rdi, %rcx
movq (%rcx), %rdi # default load
call L25 # move call
movq %rax, (%r13) # store
L47:
movq %r12, %rdx # default move
L50:
L53:
movq %rdx, %rax # default move
movq -24(%rbp), %r12 # spill load
movq -16(%rbp), %r13 # spill load
jmp L84
L51:
movq -32(%rbp), %rdx # spill load
jmp L53
L48:
movq -40(%rbp), %rsi # spill load
movq %rsi, %rdx # default move
jmp L50
L45:
movq $16, %rdi # const
call initRecord # move call
movq %rax, %r12 # rax to temp 
movq $0, %rdx # const
movq %r12, %rcx # add lexp -> r
add %rdx, %rcx
movq $0, %rdx # const
movq -40(%rbp), %rsi # spill load
add %rdx, %rsi
movq (%rsi), %rdx # mem to temp
movq %rdx, (%rcx) # temp to mem
movq $8, %rcx # const
movq %r12, %r13 # add lexp -> r
add %rcx, %r13
movq -32(%rbp), %rdx # spill load
movq $8, %rcx # const
movq -40(%rbp), %rsi # spill load
add %rcx, %rsi
movq (%rsi), %rsi # default load
movq $8, %rdi # const
movq %rbp, %rcx # minus lexp -> r
sub %rdi, %rcx
movq (%rcx), %rdi # default load
call L25 # move call
movq %rax, (%r13) # store
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
subq $24, %rsp
# start main
# sink 
L87:
movq %rdi, -8(%rbp) # store to offset
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $8, %rdi # const
call initRecord # move call
movq %rax, %r14 # rax to temp 
movq $0, %rcx # const
movq %rcx, 0(%r14) # store to offset
movq %r14, %rsi # move reg arg 1 to temp
movq $8, %rdx # const
movq %rbp, %rcx # minus lexp -> r
sub %rdx, %rcx
movq (%rcx), %rdi # default load
call L0 # move call
movq %rax, %r15 # rax to temp 
movq $0, %rcx # const
add %rcx, %r14
movq (%r14), %rcx # default load
movq $1, %rdx # const
cmp %rdx, %rcx
je L30
L31:
movq $0, %rax # const
L32:
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L86
L30:
movq $16, %rdi # const
call initRecord # move call
movq %rax, %r14 # rax to temp 
movq %r15, 0(%r14) # store to offset
movq $8, %rcx # const
movq %r14, %r15 # add lexp -> r
add %rcx, %r15
movq $8, %rdx # const
movq %rbp, %rcx # minus lexp -> r
sub %rdx, %rcx
movq (%rcx), %rdi # default load
call L24 # move call
movq %rax, (%r15) # store
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
subq $32, %rsp
# start main
# sink 
L89:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -32(%rbp) # spill store
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %r14 # const
movq %rbp, %rdi # move reg arg 0 to temp
call L2 # exp call ( no return value )
movq $0, %rcx # const
movq -32(%rbp), %rsi # spill load
add %rcx, %rsi
movq %rsi, %r15 # default move
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rsi # default load
movq %rbp, %rdi # move reg arg 0 to temp
call L1 # move call
movq %rax, (%r15) # store
L22:
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rsi # default load
movq %rbp, %rdi # move reg arg 0 to temp
call L1 # move call
movq $1, %rcx # const
cmp %rcx, %rax
je L23
L20:
movq %r14, %rax # default move
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L88
L23:
movq $10, %rcx # const
movq %r14, %rax # imul l -> rax
imul %rcx # imul rax * r 
movq %rax, %r14 # default move
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rdi # default load
call ord # move call
add %rax, %r14
movq $L21, %rdi # default name
call ord # move call
sub %rax, %r14
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %r15 # default load
movq $-8, %rcx # const
add %rcx, %r15
call getchar # move call
movq %rax, (%r15) # store
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
subq $16, %rsp
# start main
# sink 
L91:
movq %rdi, -8(%rbp) # store to offset
movq %r15, -16(%rbp) # spill store
L16:
movq $L10, %rsi # default name
movq $-8, %rdx # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rdx
movq (%rdx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rdi # default load
call stringEqual # move call
movq $1, %rcx # const
cmp %rcx, %rax
je L12
L13:
movq $L11, %rsi # default name
movq $-8, %rdx # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rdx
movq (%rdx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rdi # default load
call stringEqual # move call
movq $1, %rcx # const
cmp %rcx, %rax
je L17
L15:
movq -16(%rbp), %r15 # spill load
jmp L90
L12:
L17:
movq $-8, %rcx # const
movq $-8, %rdx # const
add %rbp, %rdx
movq (%rdx), %rdx # default load
add %rdx, %rcx
movq (%rcx), %r15 # default load
movq $-8, %rcx # const
add %rcx, %r15
call getchar # move call
movq %rax, (%r15) # store
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
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $-8, %rdx # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rdx
movq (%rdx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rdi # default load
call ord # move call
movq %rax, %r14 # rax to temp 
movq $L3, %rdi # default name
call ord # move call
cmp %rax, %r14
jge L5
L6:
movq $0, %rax # const
L7:
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L92
L5:
movq $1, %r15 # const
movq $-8, %rcx # const
movq $-8, %rdx # const
add %rbp, %rdx
movq (%rdx), %rdx # default load
add %rdx, %rcx
movq (%rcx), %rcx # default load
movq $-8, %rdx # const
add %rdx, %rcx
movq (%rcx), %rdi # default load
call ord # move call
movq %rax, %r14 # rax to temp 
movq $L4, %rdi # default name
call ord # move call
cmp %rax, %r14
jle L8
L9:
movq $0, %r15 # const
L8:
movq %r15, %rax # default move
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
