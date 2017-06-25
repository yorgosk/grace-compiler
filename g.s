.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_wrong_size_1 proc near
push bp
mov bp, sp
sub sp, 8
add ax, dx
mov size ptr [bp+offset], ax
mov si, word ptr [bp+offset]
mov size ptr [si], R
ret
_wrong_size_11: mov sp, bp
pop bp
ret
_wrong_size_11 endp
_main_12 proc near
push bp
mov bp, sp
sub sp, 8
push si
push si
sub sp, 2
call near ptr wrong_size
add sp, size+4
_main_16: mov sp, bp
pop bp
ret
_main_16 endp
.data
