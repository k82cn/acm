#include <stdio.h>

typedef unsigned long long ulong;

#define F(m,a) (((m)%(a) == 0)? (m)/(a) : ((m)/(a)) + 1)

int main(int argc, char const *argv[])
{
    ulong m, n, a;

    if (scanf("%llu %llu %llu", &m, &n, &a) != 3)
    {
        return -1;
    }

    printf("%llu\n", F(m,a) * F(n, a));

    return 0;
}
