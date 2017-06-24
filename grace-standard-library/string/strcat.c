void str_cat(char *trg, char *src) {
    while (*trg) trg++;
    while (*trg++ = *src++);
}
