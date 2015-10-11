#include <algorithm>
#include <vector>
#include <iterator>
#include <iostream>

using namespace std;

unsigned long long to_int(vector<int>& data)
{
  unsigned long long res = 0;
  for(auto i : data) {
    res += i;
    res = res * 10;
  }
  return res / 10;
}

bool is_prime(unsigned long long n)
{
  for (int i = 2; i <= n/2 + 1; i++) {
    if (n % i == 0) return false;
  }
  return true;
}

int max_prime(vector<int>& data)
{
  std::reverse(data.begin(), data.end());
  unsigned long long res = 0;
  do {
    res = to_int(data);
    if (is_prime(res)) {
      break;
    } else {
      res = 0;
    }

  } while (std::prev_permutation(data.begin(), data.end()));
  cout << "=====================================================" << endl;
  cout << res << endl;
}

int main(int argc, char** argv)
{
  vector<int> data0 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
  max_prime(data0);

  vector<int> data1 = {1, 2, 3, 4, 5, 6, 7, 8};
  max_prime(data1);

  vector<int> data2 = {1, 2, 3, 4, 5, 6, 7};
  max_prime(data2);

  vector<int> data3 = {1, 2, 3, 4, 5, 6};
  max_prime(data3);

  vector<int> data4 = {1, 2, 3, 4, 5};
  max_prime(data4);

  vector<int> data5 = {1, 2, 3, 4};
  max_prime(data5);

  vector<int> data6 = {1, 2, 3};
  max_prime(data6);

  vector<int> data7 = {1, 2};
  max_prime(data7);

  return 0;
}
