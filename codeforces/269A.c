#include <stdio.h>

int main(int argc, char const *argv[])
{
	int i, j, st[10] = {0};

	int h = 0, b = 0, l = 0;

	for (i = 0; i < 6; i++)
	{
		scanf("%d", &j);
		st[j] ++;
	}

	for (i = 0; i < 10; i++)
	{
		switch(st[i])
		{
			case 1:
				if (!h) h = i;
				else if (!b) b = i;
				break;
			case 2:
				h = b = i;
				break;
			case 4:
				l = i;
				break;
			case 5:
				l = i;
				if (!h) h = i;
				else if (!b) b = i;
				break;
			case 6:
				l = h = b = i;
				break;
			default:
				break;
		}
	}

	if (l && h && b)
	{
		printf("%s\n", (h == b ? "Elephant" : "Bear"));
	}
	else
	{
		printf("Alien\n");
	}

	return 0;
}