.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L166:
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
call L33 # default call
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
call L33 # default call
movq %rax, %rcx # default exp
movq %rbp, %rcx # default move
movq %rcx, -24(%rbp) # spill store
movq %rax, %rdx # move arg 2 to temp
movq -16(%rbp), %rax # spill load
movq %rax, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L85 # default call
movq %rax, %rcx # default exp
movq %rax, %rsi # move arg 1 to temp
movq -24(%rbp), %rax # spill load
movq %rax, %rcx # spill load
movq %rcx, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L155 # default call
jmp L165
L165:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
L155:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L168:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
je L162
L163:
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
call L131 # default call
movq $L159, %rax # default name
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
call L155 # default call
L164:
jmp L167
L162:
movq $L156, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
jmp L164
L167:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L159:
	.long  0x1
	.ascii " "
.data
L156:
	.long  0x2
	.ascii "\n"
.text
L131:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L170:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jl L143
L144:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jg L140
L141:
movq $L139, %rax # default name
movq %rax, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call print # default call
L142:
L145:
jmp L169
L143:
movq $L138, %rax # default name
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
call L133 # default call
jmp L145
L140:
movq -16(%rbp), %rax # spill load
movq %rax, %rsi # move arg 1 to temp
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L133 # default call
jmp L142
L169:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L149:
	.long  0x1
	.ascii " "
.data
L146:
	.long  0x2
	.ascii "\n"
.data
L139:
	.long  0x1
	.ascii "0"
.data
L138:
	.long  0x1
	.ascii "-"
.text
L133:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L172:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jg L135
L136:
L137:
jmp L171
L135:
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
call L133 # default call
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
movq $L134, %rax # default name
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
jmp L137
L171:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L134:
	.long  0x1
	.ascii "0"
.text
L85:
pushq %rbp
movq %rsp, %rbp
subq $56, %rsp
# start main
# sink 
L174:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq %rdx, %rax # default move
movq %rax, -24(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
je L106
L107:
movq $0, %rcx # const
movq -24(%rbp), %rax # spill load
cmp %rcx, %rax
je L103
L104:
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
add %rcx, %rax
movq (%rax), %rcx # default load
movq $0, %rdx # const
movq -24(%rbp), %rax # spill load
add %rdx, %rax
movq (%rax), %rax # default load
cmp %rax, %rcx
jl L100
L101:
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
call L85 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -56(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq -40(%rbp), %rax # spill load
L102:
L105:
L108:
jmp L173
L106:
movq -24(%rbp), %rax # spill load
jmp L108
L103:
movq -16(%rbp), %rax # spill load
jmp L105
L100:
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
call L85 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -48(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq -32(%rbp), %rax # spill load
jmp L102
L173:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L125:
	.long  0x1
	.ascii " "
.data
L122:
	.long  0x2
	.ascii "\n"
.data
L115:
	.long  0x1
	.ascii "0"
.data
L114:
	.long  0x1
	.ascii "-"
.text
L109:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L176:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jg L111
L112:
L113:
jmp L175
L111:
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
call L109 # default call
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
movq $L110, %rax # default name
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
jmp L113
L175:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L110:
	.long  0x1
	.ascii "0"
.text
L33:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# sink 
L178:
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
je L39
L40:
movq $0, %rax # const
L41:
jmp L177
L39:
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
call L33 # default call
movq %rax, %rcx # default exp
movq %rax, %rcx # default move
movq -40(%rbp), %rax # spill load
movq %rcx, (%rax) # store
movq -24(%rbp), %rax # spill load
jmp L41
L177:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L79:
	.long  0x1
	.ascii " "
.data
L76:
	.long  0x2
	.ascii "\n"
.data
L69:
	.long  0x1
	.ascii "0"
.data
L68:
	.long  0x1
	.ascii "-"
.text
L63:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# sink 
L180:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -16(%rbp) # spill store
movq $0, %rcx # const
movq -16(%rbp), %rax # spill load
cmp %rcx, %rax
jg L65
L66:
L67:
jmp L179
L65:
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
call L63 # default call
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
movq $L64, %rax # default name
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
jmp L67
L179:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L64:
	.long  0x1
	.ascii "0"
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $56, %rsp
# start main
# sink 
L182:
movq %rdi, -8(%rbp) # store to offset
movq %rsi, %rax # default move
movq %rax, -24(%rbp) # spill store
movq $0, %rax # const
movq %rax, -16(%rbp) # spill store
movq %rbp, %rdi # move arg 0 to temp
movq $0, %rax # zero rax
call L18 # default call
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
L31:
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
je L32
L29:
movq -16(%rbp), %rax # spill load
jmp L181
L32:
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
movq $L30, %rax # default name
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
jmp L31
L181:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L30:
	.long  0x1
	.ascii "0"
.text
L18:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# sink 
L184:
movq %rdi, -8(%rbp) # store to offset
L25:
movq $L19, %rax # default name
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
je L21
L22:
movq $L20, %rax # default name
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
je L26
L24:
jmp L183
L21:
L26:
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
jmp L25
L183:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L20:
	.long  0x2
	.ascii "\n"
.data
L19:
	.long  0x1
	.ascii " "
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
# start main
# sink 
L186:
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
jmp L185
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
jle L16
L17:
movq $0, %rax # const
movq %rax, -16(%rbp) # spill store
L16:
movq -16(%rbp), %rax # spill load
jmp L7
L185:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L9:
	.long  0x2
	.ascii "\n"
.data
L8:
	.long  0x1
	.ascii " "
.data
L4:
	.long  0x1
	.ascii "9"
.data
L3:
	.long  0x1
	.ascii "0"
