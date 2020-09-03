.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# start 
L77:
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $-8, %rax # bin(t, i)
movq %rbp, %r14 # add lexp -> r
add %rax, %r14
call getchar
movq %rax, (%r14) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L24
movq %rax, %r15 # rax to temp 
movq $-8, %rax # bin(t, i)
movq %rbp, %r14 # add lexp -> r
add %rax, %r14
call getchar
movq %rax, (%r14) # store
movq %rbp, %rdi # move reg arg 0 to temp
call L24
movq %rax, %r14 # rax to temp 
movq %rbp, %rdi # move reg arg 0 to temp
movq %r15, %rsi # move reg arg 1 to temp
call L27
movq %rbp, %rdi # move reg arg 0 to temp
movq %r14, %rsi # move reg arg 1 to temp
call L27
movq %rbp, %rdi # move reg arg 0 to temp
movq %r15, %rsi # move reg arg 1 to temp
movq %r14, %rdx # move reg arg 2 to temp
call L25
movq %rax, %rsi # rax to temp 
movq %rbp, %rdi # move reg arg 0 to temp
call L27
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
# start 
L79:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -16(%rbp) # spill store
movq $0, %rax # integerExpression
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
je L73
L74:
movq -8(%rbp), %rdi # mem(boe)
movq -16(%rbp), %rsi # spill load
movq 0(%rsi), %rsi # mem(boe)
call L26
movq $L70, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
movq -16(%rbp), %rsi # spill load
movq 8(%rsi), %rsi # mem(boe)
call L27
L75:
jmp L78
L73:
movq $L67, %rdi # default name
call print
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
# start 
L81:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -16(%rbp) # spill store
movq $0, %rax # integerExpression
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
jl L64
L65:
movq $0, %rax # integerExpression
movq -16(%rbp), %rsi # spill load
cmp %rax, %rsi
jg L61
L62:
movq $L60, %rdi # default name
call print
L63:
L66:
jmp L80
L64:
movq $L59, %rdi # default name
call print
movq $0, %rax # bin(i, t)
movq -16(%rbp), %rsi # spill load
sub %rsi, %rax
movq %rbp, %rdi # move reg arg 0 to temp
movq %rax, %rsi # move reg arg 1 to temp
call L54
jmp L66
L61:
movq %rbp, %rdi # move reg arg 0 to temp
movq -16(%rbp), %rsi # spill load
call L54
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
# start 
L83:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %rcx # integerExpression
movq -24(%rbp), %rsi # spill load
cmp %rcx, %rsi
jg L56
L57:
L58:
movq -16(%rbp), %r15 # spill load
jmp L82
L56:
movq -8(%rbp), %rdi # mem(boe)
movq $10, %rcx # bin(t, i)
movq -24(%rbp), %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq %rax, %rsi # move reg arg 1 to temp
call L54
movq $10, %rcx # bin(t, i)
movq -24(%rbp), %rsi # spill load
movq %rsi, %rax # div r -> rax
xor %rdx, %rdx # div clear bits rdx 
idiv %rcx # div rax * rexp 
movq $10, %rcx # bin(t, i)
imul %rcx # imul rax * r 
movq -24(%rbp), %rsi # spill load
movq %rsi, %r15 # minus lexp -> r
sub %rax, %r15
movq $L55, %rdi # default name
call ord
movq %r15, %rdi # add lexp -> r
add %rax, %rdi
call chr
movq %rax, %rdi # rax to temp 
call print
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
# start 
L85:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -40(%rbp) # spill store
movq %rdx, -32(%rbp) # spill store
movq %r12, -24(%rbp) # spill store
movq %r13, -16(%rbp) # spill store
movq $0, %rax # integerExpression
movq -40(%rbp), %rsi # spill load
cmp %rax, %rsi
je L51
L52:
movq $0, %rax # integerExpression
movq -32(%rbp), %rdx # spill load
cmp %rax, %rdx
je L48
L49:
movq -40(%rbp), %rsi # spill load
movq 0(%rsi), %rax # mem(boe)
movq -32(%rbp), %rdx # spill load
movq 0(%rdx), %rcx # mem(boe)
cmp %rcx, %rax
jl L45
L46:
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r12 # rax to temp 
movq $0, %rcx # bin(t, i)
movq %r12, %rax # add lexp -> r
add %rcx, %rax
movq $0, %rcx # bin(t, i)
movq -32(%rbp), %rdx # spill load
add %rcx, %rdx
movq (%rdx), %rcx # m2m mem to temp
movq %rcx, (%rax) # m2m temp to mem
movq $8, %rax # bin(t, i)
movq %r12, %r13 # add lexp -> r
add %rax, %r13
movq -8(%rbp), %rdi # mem(boe)
movq -32(%rbp), %rdx # spill load
movq 8(%rdx), %rdx # mem(boe)
movq -40(%rbp), %rsi # spill load
call L25
movq %rax, (%r13) # store
movq %r12, %rsi # move(t, t)
L47:
L50:
L53:
movq %rsi, %rax # move(t, t)
movq -24(%rbp), %r12 # spill load
movq -16(%rbp), %r13 # spill load
jmp L84
L51:
movq -32(%rbp), %rdx # spill load
movq %rdx, %rsi # move(t, t)
jmp L53
L48:
movq -40(%rbp), %rsi # spill load
jmp L50
L45:
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r12 # rax to temp 
movq $0, %rcx # bin(t, i)
movq %r12, %rax # add lexp -> r
add %rcx, %rax
movq $0, %rcx # bin(t, i)
movq -40(%rbp), %rsi # spill load
add %rcx, %rsi
movq (%rsi), %rcx # m2m mem to temp
movq %rcx, (%rax) # m2m temp to mem
movq $8, %rax # bin(t, i)
movq %r12, %r13 # add lexp -> r
add %rax, %r13
movq -8(%rbp), %rdi # mem(boe)
movq -40(%rbp), %rsi # spill load
movq 8(%rsi), %rsi # mem(boe)
movq -32(%rbp), %rdx # spill load
call L25
movq %rax, (%r13) # store
movq %r12, %rsi # move(t, t)
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
# start 
L87:
movq %rdi, -8(%rbp) # store to offset
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $8, %rdi # integerExpression
call initRecord
movq $0, 0(%rax) # store to offset
movq %rax, %r14 # move(t, t)
movq -8(%rbp), %rdi # mem(boe)
movq %r14, %rsi # move reg arg 1 to temp
call L0
movq %rax, %r15 # rax to temp 
movq 0(%r14), %rax # mem(boe)
movq $1, %rcx # integerExpression
cmp %rcx, %rax
je L30
L31:
movq $0, %rax # move(t, i)
L32:
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L86
L30:
movq $16, %rdi # integerExpression
call initRecord
movq %rax, %r14 # rax to temp 
movq %r15, 0(%r14) # store to offset
movq $8, %rax # bin(t, i)
movq %r14, %r15 # add lexp -> r
add %rax, %r15
movq -8(%rbp), %rdi # mem(boe)
call L24
movq %rax, (%r15) # store
movq %r14, %rax # move(t, t)
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
# start 
L89:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, -32(%rbp) # spill store
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %r14 # move(t, i)
movq %rbp, %rdi # move reg arg 0 to temp
call L2
movq $0, %rax # bin(t, i)
movq -32(%rbp), %rsi # spill load
add %rax, %rsi
movq %rsi, %r15 # move(t, t)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rsi # mem(boe)
movq %rbp, %rdi # move reg arg 0 to temp
call L1
movq %rax, (%r15) # store
L22:
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rsi # mem(boe)
movq %rbp, %rdi # move reg arg 0 to temp
call L1
movq $1, %rcx # integerExpression
cmp %rcx, %rax
je L23
L20:
movq %r14, %rax # move(t, t)
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L88
L23:
movq $10, %rcx # bin(t, i)
movq %r14, %rax # imul l -> rax
imul %rcx # imul rax * r 
movq %rax, %r14 # move(t, t)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call ord
add %rax, %r14
movq $L21, %rdi # default name
call ord
sub %rax, %r14
movq -8(%rbp), %r15 # mem(boe)
movq $-8, %rax # bin(t, i)
add %rax, %r15
call getchar
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
# start 
L91:
movq %rdi, -8(%rbp) # store to offset
movq %r15, -16(%rbp) # spill store
L16:
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
movq $L10, %rsi # default name
call stringEqual
movq $1, %rcx # integerExpression
cmp %rcx, %rax
je L12
L13:
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
movq $L11, %rsi # default name
call stringEqual
movq $1, %rcx # integerExpression
cmp %rcx, %rax
je L17
L15:
movq -16(%rbp), %r15 # spill load
jmp L90
L12:
L17:
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %r15 # mem(boe)
movq $-8, %rax # bin(t, i)
add %rax, %r15
call getchar
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
# start 
L93:
movq %rdi, -8(%rbp) # store to offset
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call ord
movq %rax, %r14 # move(t, t)
movq $L3, %rdi # default name
call ord
cmp %rax, %r14
jge L5
L6:
movq $0, %rax # move(t, i)
L7:
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L92
L5:
movq $1, %r14 # move(t, i)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %rax # mem(boe)
movq -8(%rax), %rdi # mem(boe)
call ord
movq %rax, %r15 # move(t, t)
movq $L4, %rdi # default name
call ord
cmp %rax, %r15
jle L8
L9:
movq $0, %r14 # move(t, i)
L8:
movq %r14, %rax # move(t, t)
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
