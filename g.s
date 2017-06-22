.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
@1:
name(_prime_1) proc near
push bp
mov bp, sp
sub sp, 8
mov ax, n
cwd
mov cx, 2
idiv cxmov size ptr [bp+offset], dx
@18:
mov R, i
mov size ptr [bp+offset], R
mov ax, n
cwd
mov cx, 2
idiv cxmov size ptr [bp+offset], ax
mov ax, n
cwd
mov cx, i
idiv cxmov size ptr [bp+offset], dx
mov ax, i
mov dx, 2
add ax, dx
mov size ptr [bp+offset], ax
@27:
mov R, i
mov size ptr [bp+offset], R
@30:
name(_main_30) proc near
push bp
mov bp, sp
sub sp, 8
@35:
mov R, limit
mov size ptr [bp+offset], R
@38:
mov R, counter
mov size ptr [bp+offset], R
mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@42:
mov R, counter
mov size ptr [bp+offset], R
mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@51:
mov R, counter
mov size ptr [bp+offset], R
@57:
mov R, number
mov size ptr [bp+offset], R
mov ax, number
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@67:
mov R, counter
mov size ptr [bp+offset], R
mov ax, number
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
mov ax, number
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov ax, counter
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
@83:
mov R, counter
mov size ptr [bp+offset], R
mov ax, number
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov ax, number
mov dx, 6
add ax, dx
mov size ptr [bp+offset], ax
@91:
mov R, number
mov size ptr [bp+offset], R
