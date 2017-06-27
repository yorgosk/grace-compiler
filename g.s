.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_reverse_1 proc near
push bp
mov bp, sp
sub sp, 8
push si
push si
sub sp, 2
call near ptr strlen
add sp, size+4
mov size ptr [bp+offset], R
mov size ptr [bp+offset], R
cmp ax, dx
jl ?
jmp ?
sub ax, dx
mov size ptr [bp+offset], ax
sub ax, dx
mov size ptr [bp+offset], ax
mov cx, 8
imul cx
add ax, cx
mov si, word ptr [bp+offset]
mov size ptr [si], ax
mov cx, 8
imul cx
add ax, cx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
jmp 17
mov cx, 8
imul cx
add ax, cx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
_reverse_19: mov sp, bp
pop bp
ret
_reverse_19 endp
_main_20 proc near
push bp
mov bp, sp
sub sp, 8
push si
sub sp, 2
call near ptr reverse
add sp, size+4
push si
sub sp, 2
call near ptr puts
add sp, size+4
_main_25: mov sp, bp
pop bp
ret
_main_25 endp
.data
