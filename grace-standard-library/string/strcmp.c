int str_cmp(char* s1, char* s2) {
	while (*s1 == *s2) {
		if (*s1 == '\0') return 0;
		s1++;
		s2++;
	}

	return *s1 - *s2;
}
