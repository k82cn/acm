#include <iostream>

using namespace std;

bool is_prime(unsigned long long n)
{
  for (unsigned long long i = 2; i < n; i++) {
    if (n % i == 0)
      return false;
  }
  return true;
}

int main(int argc, char** argv)
{
  unsigned long long sum = 2;

  for (unsigned long long i = 3; i < 2000000; i++) {
    if (is_prime(i))
      sum += i;
  }
  cout << sum << endl;
  return 0;
}
