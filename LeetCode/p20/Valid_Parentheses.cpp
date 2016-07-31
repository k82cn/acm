#include <string>
#include <vector>
#include <iostream>

using namespace std;

class Solution
{
    public:
        bool isValid(string s)
        {
            std::vector<char> v;
            for (int i = 0; i < s.size(); i++)
            {
                switch (s[i])
                {
                case '{':
                case '[':
                case '(':
                    v.push_back(s[i]);
                    break;
                case '}':
                    if (v.size() == 0)
                    {
                        return false;
                    }
                    if (v.back() == '{')
                    {
                        v.pop_back();
                    }
                    else
                    {
                        return false;
                    }
                    break;
                case ']':
                    if (v.size() == 0)
                    {
                        return false;
                    }
                    if (v.back() == '[')
                    {
                        v.pop_back();
                    }
                    else
                    {
                        return false;
                    }
                    break;
                case ')':
                    if (v.size() == 0)
                    {
                        return false;
                    }
                    if (v.back() == '(')
                    {
                        v.pop_back();
                    }
                    else
                    {
                        return false;
                    }
                    break;
                }
            }
            return v.size() == 0;
        }
};

int main(int argc, char const *argv[])
{
    Solution sln;

    cout << sln.isValid("()[]{}") << endl;
    cout << sln.isValid("()") << endl;
    cout << sln.isValid("(]") << endl;
    cout << sln.isValid("([)]") << endl;
    return 0;
}
