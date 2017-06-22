.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
name(foo) proc near
push bp
mov bp, sp
sub sp, size
endof(foo): mov sp, bp
pop bp
ret
name(foo) endp
name(bar) proc near
push bp
mov bp, sp
sub sp, size
endof(bar): mov sp, bp
pop bp
ret
name(bar) endp
name(main) proc near
push bp
mov bp, sp
sub sp, size
endof(main): mov sp, bp
pop bp
ret
name(main) endp
.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
