#include <iostream>

using namespace std;

template<class T>
T max_2(T a, T b)
{
    if (a > b) return a;
    else return b;
}

int main(int argc, char** argv)
{
    cout << "The max value of 'a' and 'b' is: " << max_2('a', 'b') << endl;
    cout << "The max value of 1.0 and 3.0 is: " << max_2(1.0, 3.0) << endl;
    cout << "The max value of 1 and 2 is: " << max_2((int) 1, (int) 2) << endl;
    cout << "The max value of abcd and efgh is:" << max_2("abcd", "efgh") << endl;
    return 0;
}
