#include <iostream>
#include <string>
#include <map>

using namespace std;

/*
 * Given a string, find the length of the longest substring without repeating characters.
 * For example, the longest substring without repeating letters for "abcabcbb" is "abc", which the length is 3.
 * For "bbbbb" the longest substring is "b", with the length of 1.
 */
class Solution
{
    public:
        int lengthOfLongestSubstring(string s)
        {
            map<char, int> idx ;
            int maxLen = 1;
            int curLen = 1;

            if (s.size() < 2)
            {
                return s.size();
            }

            int i = 0;
            int j = 1;

            idx[s[i]] = 1;

            while (j < s.size())
            {
                if (0 == idx[s[j]])
                {
                    idx[s[j]] = 1;
                    j++;
                    curLen++;
                }
                else
                {
                    maxLen = max (curLen, maxLen);
                    curLen--;
                    idx[s[i]]--;
                    i++;
                }
            }
            return max (curLen, maxLen);
        }
};

int main(int argc, char** argv)
{
    Solution sln;
    cout << sln.lengthOfLongestSubstring("abcabcbb") << endl;
    cout << sln.lengthOfLongestSubstring("au") << endl;
    cout << sln.lengthOfLongestSubstring("bbbbb") << endl;
    cout << sln.lengthOfLongestSubstring("hchzvfrkmlnozjk") << endl;

    return 0;
}
