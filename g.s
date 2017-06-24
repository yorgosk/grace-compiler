.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_prime_1 proc near
push bp
mov bp, sp
sub sp, 8
cmp ax, dx
jl ?
jmp ?
push si
push si
sub sp, 2
call near ptr prime
add sp, size+4
ret
jmp ?
cmp ax, dx
jl ?
jmp ?
ret
jmp ?
cmp ax, dx
jz ?
jmp ?
ret
jmp ?
cwd
idiv cxmov size ptr [bp+offset], dx
cmp ax, dx
jz ?
jmp ?
ret
jmp ?
mov size ptr [bp+offset], R
cwd
idiv cxmov size ptr [bp+offset], ax
cmp ax, dx
jle ?
jmp ?
cwd
idiv cxmov size ptr [bp+offset], dx
cmp ax, dx
jz ?
jmp ?
ret
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
jmp 34
ret
_prime_35: mov sp, bp
pop bp
ret
_prime_35 endp
_main_36 proc near
push bp
mov bp, sp
sub sp, 8
push si
sub sp, 2
call near ptr puts
add sp, size+4
push si
sub sp, 2
call near ptr geti
add sp, size+4
mov size ptr [bp+offset], R
push si
sub sp, 2
call near ptr puts
add sp, size+4
mov size ptr [bp+offset], R
cmp ax, dx
jge ?
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
push si
sub sp, 2
call near ptr geti
add sp, size+4
push si
sub sp, 2
call near ptr puts
add sp, size+4
jmp ?
cmp ax, dx
jge ?
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
push si
sub sp, 2
call near ptr geti
add sp, size+4
push si
sub sp, 2
call near ptr puts
add sp, size+4
jmp ?
mov size ptr [bp+offset], R
cmp ax, dx
jle ?
jmp ?
sub ax, dx
mov size ptr [bp+offset], ax
push si
push si
sub sp, 2
call near ptr prime
add sp, size+4
cmp ax, dx
jz ?
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
sub ax, dx
mov size ptr [bp+offset], ax
push si
sub sp, 2
call near ptr puti
add sp, size+4
push si
sub sp, 2
call near ptr puts
add sp, size+4
jmp ?
cmp ax, dx
jnz ?
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
push si
push si
sub sp, 2
call near ptr prime
add sp, size+4
cmp ax, dx
jz ?
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
add ax, dx
mov size ptr [bp+offset], ax
push si
sub sp, 2
call near ptr puti
add sp, size+4
push si
sub sp, 2
call near ptr puts
add sp, size+4
jmp ?
add ax, dx
mov size ptr [bp+offset], ax
mov size ptr [bp+offset], R
jmp 99
push si
sub sp, 2
call near ptr puts
add sp, size+4
push si
sub sp, 2
call near ptr puti
add sp, size+4
push si
sub sp, 2
call near ptr puts
add sp, size+4
_main_105: mov sp, bp
pop bp
ret
_main_105 endp
.data
