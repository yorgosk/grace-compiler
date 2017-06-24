.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_hello_1 proc near
push bp
mov bp, sp
sub sp, 8
sub sp, 2
call near ptr puts
add sp, size+4
_hello_4: mov sp, bp
pop bp
ret
_hello_4 endp
