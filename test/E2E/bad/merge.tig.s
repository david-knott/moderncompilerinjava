.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L77:
movq $-8, %rax # const
movq %rbp, %rcx # add lexp -> r
add %rax, %rcx
movq %rcx, %rax # default move
movq %rax, -32(%rbp) # spill store
movq $0, %rax # zero rax
call getchar # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -32(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L24 # default call
movq %rax, %rcx # default exp
movq %rax, -16(%rbp) # spill store
movq $-8, %rax # const
movq %rbp, %rcx # add lexp -> r
add %rax, %rcx
movq %rcx, %rax # default move
movq %rax, -40(%rbp) # spill store
movq $0, %rax # zero rax
call getchar # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -40(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L24 # default call
movq %rax, %rcx # default exp
movq %rbp, %rcx # default move
movq %rcx, -24(%rbp) # spill store
movq %rax, %rdx # move arg 2 to temp
movq -16(%rbp), %rax # spill load
movq %rax, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L25 # default call
movq %rax, %rcx # default exp
movq %rax, %rsi # move arg 1 to temp
movq -24(%rbp), %rax # spill load
movq %rax, %rcx # spill load
movq %rcx, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L27 # default call
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
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
je L73
L74:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move arg 1 to temp
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L26 # default call
movq $L70, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
movq $8, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move arg 1 to temp
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L27 # default call
L75:
jmp L78
L73:
movq $L67, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
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
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jl L64
L65:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jg L61
L62:
movq $L60, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
L63:
L66:
jmp L80
L64:
movq $L59, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
movq $0, %rax # const
movq %rax, %rcx # minus lexp -> r
movq -16(%rbp), %rax # spill load
sub %rax, %rcx
movq %rcx, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L54 # default call
jmp L66
L61:
movq -16(%rbp), %rax # spill load
movq %rax, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L54 # default call
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
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jg L56
L57:
L58:
jmp L82
L56:
movq $10, %rcx # const
movq -16(%rbp), %rax # spill load
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq %rax, %rsi # move arg 1 to temp
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L54 # default call
movq $10, %rcx # const
movq -16(%rbp), %rax # spill load
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq $10, %rcx # const
movq %rax, %rdx # mul lexp -> r
movq %rdx, %rax # mul r -> rax
imul %rcx # mul rax * rexp 
movq %rax, %rdx # mul rax -> r
movq -16(%rbp), %rax # spill load
sub %rdx, %rax
movq %rax, -24(%rbp) # spill store
movq $L55, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -24(%rbp), %rax # spill load
add %rcx, %rax
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call chr # default call
movq %rax, %rcx # default exp
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
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
subq $56, %rsp
# start main
# sink 
L85:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq %rdx, %rax # default move
movq %rax, -24(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
je L51
L52:
movq $0, %rcx # const
movq -24(%rbp), %rax # spill load
cmp %rcx, %rax
je L48
L49:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rcx # default load
movq $0, %rdx # const
movq -24(%rbp), %rax # spill load
add %rdx, %rax
movq (%rax), %rax # default load
cmp %rax, %rcx
jl L45
L46:
movq $16, %rax # const
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call initRecord # default call
movq %rax, -40(%rbp) # spill store
movq $0, %rcx # const
movq -40(%rbp), %rax # spill load
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $0, %rcx # const
movq -24(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, (%rdx) # store
movq $8, %rcx # const
movq -40(%rbp), %rax # spill load
add %rcx, %rax
movq %rax, -56(%rbp) # spill store
movq $8, %rcx # const
movq -24(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdx # move arg 2 to temp
movq -16(%rbp), %rax # spill load
movq %rax, %rsi # move arg 1 to temp
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L25 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -56(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq -40(%rbp), %rax # spill load
L47:
L50:
L53:
jmp L84
L51:
movq -24(%rbp), %rax # spill load
jmp L53
L48:
movq -16(%rbp), %rax # spill load
jmp L50
L45:
movq $16, %rax # const
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call initRecord # default call
movq %rax, -32(%rbp) # spill store
movq $0, %rcx # const
movq -32(%rbp), %rax # spill load
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, (%rdx) # store
movq $8, %rcx # const
movq -32(%rbp), %rax # spill load
add %rcx, %rax
movq %rax, -48(%rbp) # spill store
movq -24(%rbp), %rax # spill load
movq %rax, %rdx # move arg 2 to temp
movq $8, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move arg 1 to temp
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L25 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -48(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq -32(%rbp), %rax # spill load
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
movq $8, %rax # const
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call initRecord # default call
movq $0, %rcx # const
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq $0, %rcx # const
movq %rcx, (%rdx) # store
movq %rax, -32(%rbp) # spill store
movq -32(%rbp), %rax # spill load
movq %rax, %rsi # move arg 1 to temp
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L0 # default call
movq %rax, %rcx # default exp
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -32(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rax # default load
movq $1, %rcx # const
cmp %rcx, %rax
je L30
L31:
movq $0, %rax # const
L32:
jmp L86
L30:
movq $16, %rax # const
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call initRecord # default call
movq %rax, -24(%rbp) # spill store
movq $0, %rcx # const
movq -24(%rbp), %rax # spill load
movq %rax, %rdx # add lexp -> r
add %rcx, %rdx
movq -16(%rbp), %rax # spill load
movq %rax, (%rdx) # store
movq $8, %rcx # const
movq -24(%rbp), %rax # spill load
add %rcx, %rax
movq %rax, -40(%rbp) # spill store
movq $8, %rax # const
movq %rbp, %rcx # minus lexp -> r
sub %rax, %rcx
movq (%rcx), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L24 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -40(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq -24(%rbp), %rax # spill load
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
subq $56, %rsp
# start main
# sink 
L89:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -24(%rbp) # spill store
movq $0, %rax # const
movq %rax, -16(%rbp) # spill store
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L2 # default call
movq $0, %rcx # const
movq -24(%rbp), %rax # spill load
add %rcx, %rax
movq %rax, -32(%rbp) # spill store
movq $-8, %rax # const
add %rbp, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L1 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -32(%rbp), %rax # spill load
movq %rcx, (%rax) # store
L22:
movq $-8, %rax # const
add %rbp, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L1 # default call
movq %rax, %rcx # default exp
movq $1, %rcx # const
cmp %rcx, %rax
je L23
L20:
movq -16(%rbp), %rax # spill load
jmp L88
L23:
movq $10, %rcx # const
movq -16(%rbp), %rax # spill load
imul %rcx # mul rax * rexp 
movq %rax, -40(%rbp) # spill store
movq $-8, %rax # const
add %rbp, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -40(%rbp), %rax # spill load
add %rcx, %rax
movq %rax, -48(%rbp) # spill store
movq $L21, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -48(%rbp), %rax # spill load
sub %rcx, %rax
movq %rax, -16(%rbp) # spill store
movq $-8, %rax # const
add %rbp, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq %rax, -56(%rbp) # spill store
movq $0, %rax # zero rax
call getchar # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -56(%rbp), %rax # spill load
movq %rcx, (%rax) # store
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
L16:
movq $L10, %rax # default name
movq %rax, %rsi # move arg 1 to temp
movq $-8, %rax # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call stringEqual # default call
movq $1, %rcx # const
cmp %rcx, %rax
je L12
L13:
movq $L11, %rax # default name
movq %rax, %rsi # move arg 1 to temp
movq $-8, %rax # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call stringEqual # default call
movq $1, %rcx # const
cmp %rcx, %rax
je L17
L15:
jmp L90
L12:
L17:
movq $-8, %rax # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq %rax, -16(%rbp) # spill store
movq $0, %rax # zero rax
call getchar # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -16(%rbp), %rax # spill load
movq %rcx, (%rax) # store
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
subq $32, %rsp
# start main
# sink 
L93:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq $-8, %rax # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, -24(%rbp) # spill store
movq $L3, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -24(%rbp), %rax # spill load
cmp %rcx, %rax
jge L5
L6:
movq $0, %rax # const
L7:
jmp L92
L5:
movq $1, %rax # const
movq %rax, -16(%rbp) # spill store
movq $-8, %rax # const
movq $-8, %rcx # const
add %rbp, %rcx
movq (%rcx), %rcx # default load
add %rcx, %rax
movq (%rax), %rax # default load
movq $-8, %rcx # const
add %rcx, %rax
movq (%rax), %rax # default load
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, -32(%rbp) # spill store
movq $L4, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call ord # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -32(%rbp), %rax # spill load
cmp %rcx, %rax
jle L8
L9:
movq $0, %rax # const
movq %rax, -16(%rbp) # spill store
L8:
movq -16(%rbp), %rax # spill load
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
