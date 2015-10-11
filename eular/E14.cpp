#include <iostream>

using namespace std;

int main(int argc, char** argv)
{
  unsigned long long len = 0;
  unsigned long long res = 0;
  for (unsigned long long i = 1; i < 1000000; i++) {
    unsigned long long n = i;
    unsigned long long m = 1;
    while (n != 1) {
      if ((n & 1) == 0) {
        n = n / 2;
      } else {
        n = 3 * n + 1;
      }
      m++;
    }

    if (m > len) {
      len = m;
      res = i;
    }
  }
  cout << res << endl;
  return 0;
}
