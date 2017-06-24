.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_prime_1 proc near
push bp
mov bp, sp
sub sp, 8
mov ax, n
mov dx, 0
cmp ax, dx
jl ?mov ax, n
mov dx, 2
cmp ax, dx
jl ?mov ax, 2
mov dx, 2
cmp ax, dx
jz ?mov ax, n
cwd
mov cx, 2
idiv cxmov size ptr [bp+offset], dx
mov ax, 0
mov dx, 0
cmp ax, dx
jz ?mov R, i
mov size ptr [bp+offset], R
mov ax, n
cwd
mov cx, 2
idiv cxmov size ptr [bp+offset], ax
mov ax, 2
mov dx, $t3
cmp ax, dx
jle ?mov ax, n
cwd
mov cx, i
idiv cxmov size ptr [bp+offset], dx
mov ax, 0
mov dx, 0
cmp ax, dx
jz ?jmp ?mov ax, i
mov dx, 2
add ax, dx
mov size ptr [bp+offset], ax
mov R, i
mov size ptr [bp+offset], R
jmp ?jmp ?jmp ?jmp ?_prime_29: mov sp, bp
pop bp
ret
_prime_29 endp
_main_30 proc near
push bp
mov bp, sp
sub sp, 8
mov R, limit
mov size ptr [bp+offset], R
mov R, counter
mov size ptr [bp+offset], R
mov ax, 2
mov dx, 2
cmp ax, dx
jge ?mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, counter
mov size ptr [bp+offset], R
jmp ?mov ax, 3
mov dx, 3
cmp ax, dx
jge ?mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, counter
mov size ptr [bp+offset], R
jmp ?mov R, number
mov size ptr [bp+offset], R
mov ax, limit
mov dx, limit
cmp ax, dx
jle ?mov ax, number
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
mov ax, null
mov dx, 1
cmp ax, dx
jz ?mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, counter
mov size ptr [bp+offset], R
mov ax, number
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
jmp ?mov ax, limit
mov dx, limit
cmp ax, dx
jnz ?mov ax, number
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov ax, null
mov dx, 1
cmp ax, dx
jz ?mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, counter
mov size ptr [bp+offset], R
mov ax, number
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
jmp ?mov ax, number
mov dx, 6
add ax, dx
mov size ptr [bp+offset], ax
mov R, number
mov size ptr [bp+offset], R
_main_99: mov sp, bp
pop bp
ret
_main_99 endp
