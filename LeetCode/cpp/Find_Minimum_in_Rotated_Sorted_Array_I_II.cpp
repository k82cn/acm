#include <iostream>
#include <vector>

using namespace std;

class Solution
{
    public:
        int findMin(vector<int> &num)
        {
            for (int i = 0; i < num.size() - 1; i++)
            {
                if (num[i] > num[i + 1])
                {
                    return num[i + 1];
                }
            }
            return num[0];
        }
};

int main(int argc, char** argv)
{
    vector<int> v;
    v.push_back(4);
    v.push_back(5);
    v.push_back(6);
    v.push_back(7);
    v.push_back(0);
    v.push_back(1);
    v.push_back(2);

    Solution s;
    cout << s.findMin(v) << endl;

    return 0;
}
