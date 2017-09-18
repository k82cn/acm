#include <stdio.h>

typedef unsigned int uint32_t;

int hammingWeight(uint32_t n) {
	int count = 0;
	while (n) {
		n = n&(n-1);
		count ++;
	}
	return count;
}

int main(int argc, char** argv) {
	printf("%d\n", hammingWeight(11));
	return 0;
}
