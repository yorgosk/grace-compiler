.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:
push ebp
mov ebp, esp
mov eax, OFFSET FLAT:fmt
push eax
push si
sub esp, 2
call puts
add esp, 4
#hello_1:
mov esp, ebp
pop ebp
ret
.data
fmt: .asciz "Hello world!\n"
