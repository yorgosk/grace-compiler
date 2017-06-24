void strcat(char *trg, char *src) {
    while (*trg) trg++;
    while (*trg++ = *src++);
}
