int str_len(char* s)
{
    int len = 0;
    int i;

    for (i = 0; s[i] != 0; i++) {
        len++;
    }

    return len;
}
