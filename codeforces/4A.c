#include <stdio.h>

int main(int argc, char const *argv[])
{
    int n;
    scanf("%d", &n);
    if (n > 2)
    {
        printf("%s\n", ((n & 1) == 0)?"YES":"NO");    
    }
    else
    {
        printf("NO\n");
    }
    
    return 0;
}
