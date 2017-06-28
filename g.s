.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
_hello_1 proc near
push bp
mov bp, sp
sub sp, 8
push si
sub sp, 2
call near ptr puts
add sp, size+4
#hello_1:
mov sp, bp
pop bp
ret
_hello_1 endp
.data
