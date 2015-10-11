#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <iterator>
#include <algorithm>
#include <map>
#include <set>

using namespace std;

map<char, uint64_t> cache;

uint64_t no_repeating_num(uint64_t i) {
  set<uint64_t> l;

  // cout << ">>>>>>>>>>>" << i << "<<<<<<<<<<<<<" << endl;

  do {
    l.insert(i);
    stringstream ss;
    ss << i;
    i = 0;
    for (char c : ss.str()) {
      i += cache[c];
    }
    // cout << i << endl; 
  } while (l.find(i) == l.end());

  return l.size();
}

int main(int argc, char** argv)
{
  int res = 0;

  for (uint64_t i = 1; i < 10; i++) {
    uint64_t t = 1;
    for (uint64_t j = 1; j <= i; j++) {
      t = t*j;
    }
    cache[i + '0'] = t;
  }
  cache['0'] = 1;

  for (uint64_t i = 1; i <= 1000000; i++) {
    if (no_repeating_num(i) == 60)
      res++;
  }

  cout << res << endl;
  return 0;
}
