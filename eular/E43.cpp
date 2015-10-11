#include <algorithm>
#include <vector>
#include <iterator>
#include <iostream>

using namespace std;

unsigned long long to_int(vector<int>::iterator begin, vector<int>::iterator end)
{
  unsigned long long res = 0;
  while(begin < end) {
    res += *begin;
    res = res * 10;
    begin++;
  }
  return res / 10;
}

bool is_pand(vector<int>& data)
{
  unsigned long long t;
  auto it = data.begin();
  t = to_int(it + 1, it + 4);
  if (t % 2 != 0) return false;
  t = to_int(it + 2, it + 5);
  if (t % 3 != 0) return false;
  t = to_int(it + 3, it + 6);
  if (t % 5 != 0) return false;
  t = to_int(it + 4, it + 7);
  if (t % 7 != 0) return false;
  t = to_int(it + 5, it + 8);
  if (t % 11 != 0) return false;
  t = to_int(it + 6, it + 9);
  if (t % 13 != 0) return false;
  t = to_int(it + 7, data.end());
  if (t % 17 != 0) return false;
  return true;
}

int main(int argc, char** argv)
{
  vector<int> data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
  std::ostream_iterator<int> out_it (std::cout);
  unsigned long long res = 0;
  do {
    if (is_pand(data)) {
      unsigned long long t = to_int(data.begin(), data.end());
      res += t;
      copy(data.begin(), data.end(), out_it);
      cout << ", " << t << endl;
    }
  } while (std::next_permutation(data.begin(), data.end()));
  cout << "=====================================================" << endl;
  cout << res << endl;
  return 0;
}
