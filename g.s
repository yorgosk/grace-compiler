.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:
push ebp
mov ebp, esp
mov eax, OFFSET FLAT:fmt
push eax
call puts
add esp, 4
mov eax, 0
pop ebp
ret
.data
fmt: .asciz "Hello world!\n"
