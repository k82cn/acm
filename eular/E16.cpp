#include <iostream>
#include <vector>

using namespace std;

int main(int argc, char** argv)
{
  vector<int> v = {1};
  int res = 0;

  for (int i = 0; i < 1000; i++) {
    int m = 0;
    for_each(v.begin(), v.end(), [&](int& j) {
      j = j * 2 + m;
      m = j / 10;
      j = j % 10;
    });

    if (m != 0) {
      v.push_back(m);
    }

    for_each(v.begin(), v.end(), [=](int i) {
        cout << i ;
        });
    cout << endl;
  }

  for_each(v.begin(), v.end(), [&](int i) {
    res += i;
  });

  cout << endl << res << endl;

  return 0;
}
