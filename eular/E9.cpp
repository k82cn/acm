#include <iostream>
#include <string>

using namespace std;

int main(int argc, char** argv)
{
    for (int i = 1; i < 1000; i++)
    {
        for (int j = 1; j < 1000; j++)
        {
            if (i*i + j*j == (1000 - i - j) * (1000 - i - j))
            {
                cout << i << " " << j << " " << i*j*(1000-i-j) << endl;
            }
        }
    }
}
