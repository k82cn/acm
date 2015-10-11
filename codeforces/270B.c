#include <stdio.h>
#include <stdlib.h>

static int _cmp_int(const void* _a, const void* _b)
{
	int a = *(int*) _a;
	int b = *(int*) _b;
	if (a == b) return 0;
	if (a < b) return -1;
	if (a > b) return 1;
}

int main(int argc, char const *argv[])
{
	int n, k, i, *p, *c;
	int t;

	while (scanf("%d %d", &n, &k) == 2)
	{
		p = (int*) malloc(sizeof(int) * n);
		if (p == 0)
		{
			return -1;
		}
		
		for (i = 0; i < n; ++i)
		{
			if (scanf("%d", p + i) != 1)
			{
				return -1;
			}
		}

		qsort(p, n, sizeof(int), _cmp_int);

		t = 0;

		for (c = p + n - 1; c >= p; c -= k)
		{
			t += (*c - 1) * 2;
		}

		printf("%d\n", t);

		free(p);
	}

	return 0;
}