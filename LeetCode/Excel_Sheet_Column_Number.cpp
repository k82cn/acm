#include <string>
#include <cmath>
#include <iostream>

using namespace std;

class Solution
{
    public:
        int titleToNumber(string s)
        {
            int res = 0;
            int ss = s.size() - 1;
            for (int i = ss; i >= 0; i--)
            {
                res += pow(26, ss - i) * (s[i] - 'A' + 1);
            }
            return res;
        }
};

int main(int argc, char const *argv[])
{
    Solution sln;

    cout << sln.titleToNumber("AA") << endl;
    cout << sln.titleToNumber("AB") << endl;
    cout << sln.titleToNumber("BC") << endl;
    return 0;
}