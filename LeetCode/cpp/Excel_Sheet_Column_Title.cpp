#include <iostream>
#include <string>

using namespace std;

class Solution
{
    public:
        string convertToTitle(int n)
        {
            string res;
            char c;
            while (n > 0)
            {
                c = (n - 1) % 26 + 'A';
                n = (n - 1) / 26;
                res.push_back(c);
            }

            reverse(res.begin(), res.end());

            return res;
        }
};

int main(int argc, char** argv)
{
    Solution sln;
    cout << sln.convertToTitle(1) << endl;
    cout << sln.convertToTitle(26) << endl;
    cout << sln.convertToTitle(17) << endl;
    return 0;
}
