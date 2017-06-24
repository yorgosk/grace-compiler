.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_swap_1 proc near
push bp
mov bp, sp
sub sp, 8
mov R, t
mov size ptr [bp+offset], R
mov R, x
mov si, word ptr [bp+offset]
mov size ptr [si], R
mov R, y
mov si, word ptr [bp+offset]
mov size ptr [si], R
_swap_5: mov sp, bp
pop bp
ret
_swap_5 endp
_bsort_6 proc near
push bp
mov bp, sp
sub sp, 8
mov R, changed
mov size ptr [bp+offset], R
mov ax, 0
mov dx, 0
cmp ax, dx
jg ?
jmp ?
mov R, changed
mov size ptr [bp+offset], R
mov R, i
mov size ptr [bp+offset], R
mov ax, n
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
mov ax, i
mov dx, $t1
cmp ax, dx
jl ?
jmp ?
mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov ax, 1
mov dx, $t4
cmp ax, dx
jg ?
jmp ?
mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, changed
mov size ptr [bp+offset], R
jmp ?mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, i
mov size ptr [bp+offset], R
_bsort_32: mov sp, bp
pop bp
ret
_bsort_32 endp
_putArray_33 proc near
push bp
mov bp, sp
sub sp, 8
mov R, i
mov size ptr [bp+offset], R
mov ax, i
mov dx, n
cmp ax, dx
jl ?
jmp ?
mov ax, 0
mov dx, 0
cmp ax, dx
jg ?
jmp ?
jmp ?mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, i
mov size ptr [bp+offset], R
_putArray_52: mov sp, bp
pop bp
ret
_putArray_52 endp
_main_53 proc near
push bp
mov bp, sp
sub sp, 8
mov R, seed
mov size ptr [bp+offset], R
mov R, i
mov size ptr [bp+offset], R
mov ax, i
mov dx, 16
cmp ax, dx
jl ?
jmp ?
mov ax, seed
mov cx, 137
imul cx
mov size ptr [bp+offset], ax
mov ax, $t11
mov dx, 220
add ax, dx
mov size ptr [bp+offset], ax
mov ax, $t12
mov dx, i
add ax, dx
mov size ptr [bp+offset], ax
mov ax, $t13
cwd
mov cx, 101
idiv cxmov size ptr [bp+offset], dx
mov R, seed
mov size ptr [bp+offset], R
mov R, [$t15]
mov size ptr [bp+offset], R
mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, i
mov size ptr [bp+offset], R
_main_79: mov sp, bp
pop bp
ret
_main_79 endp
