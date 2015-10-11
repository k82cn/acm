#include <map>
#include <vector>
#include <iostream>

using namespace std;

static int gcd(int a, int b)
{
    if (a == 1 || b == 1)
    {
	return 1;
    }
	
    while (b != 0)
    {
        int r = b;
        b = a % b;
        a = r;
    }
    return a;
}

int main(int argc, char const *argv[])
{
	
	int n, t;

	while (cin>>n)
	{
		vector<int> v(n);
		map<int, int> db;

		for (int i = 0; i < n; ++i)
		{
			cin >> v[i];
			db[v[i]]++;
		}

		for (int i = n-1; i >=0; --i)
		{
			for (int j = 0; j < i; ++j)
			{
				v[j] = gcd (v[j], v[j+1]);
				db[v[j]]++;
			}
		}

		cin>> n;

		for (int i = 0; i < n; ++i)
		{
			cin >> t;
			cout << db[t] << endl;
		}
	}

	

	return 0;
}
