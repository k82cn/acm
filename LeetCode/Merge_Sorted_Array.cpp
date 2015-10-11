#include <iostream>
#include <iterator>
#include <cstring>

using namespace std;

class Solution
{
    public:
        void merge(int A[], int m, int B[], int n)
        {
            int i = m - 1, j = n - 1;
            int k = m + n - 1;

            while (i >= 0 && j >= 0)
            {
                if (A[i] > B[j])
                {
                    A[k] = A[i];
                    i--;
                }
                else if (A[i] == B[j])
                {
                    A[k] = A[i];
                    k--;
                    A[k] = B[j];
                    i--;
                    j--;
                }
                else
                {
                    A[k] = B[j];
                    j--;
                }
                k--;
            }

            if (j >= 0)
            {
                memcpy(A, B, (j + 1) * sizeof(int));
            }

        }
};

int main(int argc, char** argv)
{
    Solution sln;

    const int alen = 2;
    const int blen = 1;

    int A[9] = {2};
    int B[3] = {1};

    sln.merge(A, 1, B, 1);

    copy(A, A + alen, ostream_iterator<int>(cout, " "));

    return 0;
}