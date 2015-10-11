#include <stdio.h>

#include <math.h>

typedef unsigned long ulong;

int is_prime(ulong n)
{
	ulong i;
	ulong h = (ulong) sqrt(n);
	for (i = 2; i <= h; i++)
	{
		if (n%i == 0)
		{
			return 0;
		}
	}
	return 1;
}

int main(int argc, char const *argv[])
{
	int p = 1, i;
	for (i = 0; i < 10001; ++i)
	{
		while(1)
		{
			p++;
			if (is_prime(p))
			{
				break;
			}
		}
	}

	printf("%d\n", p);

	return 0;
}