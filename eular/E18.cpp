#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <iterator>
#include <algorithm>

using namespace std;

int main(int argc, char** argv)
{
  vector<vector<int>> datas;
  string line;
  while(getline(cin, line)) {
    stringstream ss(line);
    vector<int> v;
    copy(istream_iterator<int>(ss), istream_iterator<int>(),
      back_inserter(v));
    datas.push_back(v);
  }

  for (int i = datas.size() - 1; i>0; i--) {
    for (int j = 0; j < datas[i].size() - 1; j++) {
      datas[i - 1][j] += max (datas[i][j], datas[i][j+1]);
    }
  }

  cout << datas[0][0] << endl;

  return 0;
}
