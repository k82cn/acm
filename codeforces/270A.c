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
	ulong n, i;

	while (scanf ("%lu", &n) == 1)
	{
		for (i = 4; i <= n/2; i++ )
		{
			if (!is_prime(i) && !is_prime(n - i))
			{
				printf("%lu %lu\n", i, n- i);
				break;
			}	
		}
		
	}
	
	return 0;
}
