#include <stdio.h>

int main(int argc, char** argv)
{
    double sum = .0L;
    double data[100];
    int i;

    for (i = 0; i < 100; i++)
    {
        scanf("%lf", data+i);
    }

    for (i = 0; i < 100; i++)
    {
        sum+=data[i];
    }

    printf ("%lf", sum);

    return 0;
}
