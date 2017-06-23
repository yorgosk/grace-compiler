.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_reverse_1 proc near
push bp
mov bp, sp
sub sp, 8
mov R, l
mov size ptr [bp+offset], R
mov R, i
mov size ptr [bp+offset], R
mov ax, i
mov dx, l
cmp ax, dx
jl ?mov ax, l
mov dx, i
sub ax, dx
mov size ptr [bp+offset], ax
mov ax, $t2
mov dx, 1
sub ax, dx
mov size ptr [bp+offset], ax
mov R, ASCII([$t5])
mov size ptr [bp+offset], R
mov ax, i
mov dx, 1
add ax, dx
mov size ptr [bp+offset], ax
mov R, i
mov size ptr [bp+offset], R
mov R, ASCII([$t7])
mov size ptr [bp+offset], R
_reverse_19: mov sp, bp
pop bp
ret
_reverse_19 endp
_main_20 proc near
push bp
mov bp, sp
sub sp, 8
_main_25: mov sp, bp
pop bp
ret
_main_25 endp
