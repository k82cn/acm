#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_NAME_LEN 51

typedef struct 
{
	char first_name[MAX_NAME_LEN], last_name[MAX_NAME_LEN];
} people_t;

static void _swap_name(people_t* p)
{
	char tmp[MAX_NAME_LEN];

	memcpy(tmp, p->first_name, MAX_NAME_LEN);
	memcpy(p->first_name, p->last_name, MAX_NAME_LEN);
	memcpy(p->last_name, tmp, MAX_NAME_LEN);
}

int main(int argc, char const *argv[])
{
	int n, i, j;

	if (scanf("%d", &n) == 1)
	{
		people_t p[100000] = {{0}};

		char last [MAX_NAME_LEN] = {0};

		for (i = 0; i < n; i++)
		{
			if (scanf("%s %s", p[i].first_name, p[i].last_name) == 2)
			{
				if (strcmp(p[i].first_name, p[i].last_name) > 0)
				{
					_swap_name(p + i);
				}
			}
		}

		for (i = 0; i < n; ++i)
		{
			if (scanf("%d", &j) == 1)
			{
				j--;
				if (last[0] == 0)
				{
					memcpy (last, p[j].first_name, MAX_NAME_LEN);
					continue;
				}

				if (strcmp(last, p[j].first_name) < 0)
				{
					memcpy(last, p[j].first_name, MAX_NAME_LEN);
				}
				else if (strcmp(last, p[j].last_name) < 0)
				{
					memcpy(last, p[j].last_name, MAX_NAME_LEN);
				}
				else
				{
					printf("NO\n");
					break;
				}

			}
		}

		if (i >= n)
		{
			printf("YES\n");
		}
	}

	return 0;
}