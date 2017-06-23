.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
@1:
_swap_1 proc near
push bp
mov bp, sp
sub sp, 8
@2:
mov R, t
mov size ptr [bp+offset], R
@3:
mov R, x
mov si, word ptr [bp+offset]
mov size ptr [si], R
@4:
mov R, y
mov si, word ptr [bp+offset]
mov size ptr [si], R
@5:
_swap_5: mov sp, bp
pop bp
ret
_swap_5 endp
@6:
_bsort_6 proc near
push bp
mov bp, sp
sub sp, 8
@7:
mov R, changed
mov size ptr [bp+offset], R
mov ax, 0
mov dx, 0
cmp ax, dx
jg ?@10:
mov R, changed
mov size ptr [bp+offset], R
@11:
mov R, i
mov size ptr [bp+offset], R
mov ax, n
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
mov ax, i
mov dx, $t1
cmp ax, dx
jl ?mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov ax, 1
mov dx, $t4
cmp ax, dx
jg ?mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@26:
mov R, changed
mov size ptr [bp+offset], R
jmp ?mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@29:
mov R, i
mov size ptr [bp+offset], R
@32:
_bsort_32: mov sp, bp
pop bp
ret
_bsort_32 endp
@33:
_putArray_33 proc near
push bp
mov bp, sp
sub sp, 8
@36:
mov R, i
mov size ptr [bp+offset], R
mov ax, i
mov dx, n
cmp ax, dx
jl ?mov ax, 0
mov dx, 0
cmp ax, dx
jg ?jmp ?mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@48:
mov R, i
mov size ptr [bp+offset], R
@52:
_putArray_52: mov sp, bp
pop bp
ret
_putArray_52 endp
@53:
_main_53 proc near
push bp
mov bp, sp
sub sp, 8
@54:
mov R, seed
mov size ptr [bp+offset], R
@55:
mov R, i
mov size ptr [bp+offset], R
mov ax, i
mov dx, 16
cmp ax, dx
jl ?mov ax, seed
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
@62:
mov R, seed
mov size ptr [bp+offset], R
@64:
mov R, [$t15]
mov size ptr [bp+offset], R
mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@66:
mov R, i
mov size ptr [bp+offset], R
@79:
_main_79: mov sp, bp
pop bp
ret
_main_79 endp
