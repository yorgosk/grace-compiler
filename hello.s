.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
.global main
main:
# Prologue (set up the frame & stack pointer)
push ebp
mov ebp, esp
# Put the argument of printf() on the stack
mov eax, OFFSET FLAT:fmt
push eax
call printf # Calls printf()
add esp, 4
mov eax, 0 # Set the exit code (0) here
# Epilogue (Reset frame and stack pointer)
mov esp, ebp
pop ebp
ret
.data
fmt: .asciz  "Hello world!\n"
