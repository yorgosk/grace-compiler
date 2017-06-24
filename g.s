.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_swap_1 proc near
push bp
mov bp, sp
sub sp, 8
mov size ptr [bp+offset], R
mov si, word ptr [bp+offset]
mov size ptr [si], R
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
mov size ptr [bp+offset], R
cmp ax, dx
jg ?
jmp ?
mov size ptr [bp+offset], R
mov size ptr [bp+offset], R
sub ax, dx
mov size ptr [bp+offset], ax
cmp ax, dx
jl ?
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
cmp ax, dx
jg ?
jmp ?
push si
add ax, dx
mov size ptr [bp+offset], ax
sub sp, 2
call near ptr swap
add sp, size+4
mov size ptr [bp+offset], R
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
jmp 31
jmp 32
_bsort_32: mov sp, bp
pop bp
ret
_bsort_32 endp
_putArray_33 proc near
push bp
mov bp, sp
sub sp, 8
sub sp, 2
call near ptr puts
add sp, size+4
mov size ptr [bp+offset], R
cmp ax, dx
jl ?
jmp ?
cmp ax, dx
jg ?
jmp ?
sub sp, 2
call near ptr puts
add sp, size+4
jmp ?
sub sp, 2
call near ptr puti
add sp, size+4
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
jmp 50
sub sp, 2
call near ptr puts
add sp, size+4
_putArray_52: mov sp, bp
pop bp
ret
_putArray_52 endp
_main_53 proc near
push bp
mov bp, sp
sub sp, 8
mov size ptr [bp+offset], R
mov size ptr [bp+offset], R
cmp ax, dx
jl ?
jmp ?
imul cx
mov size ptr [bp+offset], ax
add ax, dx
mov size ptr [bp+offset], ax
add ax, dx
mov size ptr [bp+offset], ax
cwd
idiv cxmov size ptr [bp+offset], dx
mov size ptr [bp+offset], R
mov size ptr [bp+offset], R
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
jmp 68
push ax
push si
sub sp, 2
call near ptr putArray
add sp, size+4
push si
sub sp, 2
call near ptr bsort
add sp, size+4
push ax
push si
sub sp, 2
call near ptr putArray
add sp, size+4
_main_79: mov sp, bp
pop bp
ret
_main_79 endp
