#include <iostream>
#include <vector>
#include <iterator>
#include <algorithm>

using namespace std;

struct pile_t
{
	int start, end;
};

static bool is_worm_in_pile(const pile_t& p, int w)
{
	if (p.end >= w)
	{
		return false;
	}

	return true;
}

int main(int argc, char** argv)
 {
     int n;
     while (cin >> n)
     {
     	vector<pile_t> ps(n);
     	int t, m, c = 0;

     	for (int i = 0; i < n; ++i)
     	 {     	 	
     	 	cin >> t;
     	 	ps[i].start = c + 1;
     	 	ps[i].end = c + t;
     	 	c = ps[i].end;
     	 } 

     	 cin >> m;
     	 for (int i = 0; i < m; ++i)
     	 {
     	 	cin>>t;
 			vector<pile_t>::iterator it = lower_bound (ps.begin(), ps.end(), t, is_worm_in_pile);
     	 	cout << it - ps.begin() + 1 << endl;
     	 }
     }
     return 0;
 }