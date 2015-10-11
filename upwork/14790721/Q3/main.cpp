/*
    Give the function definition for the function with the following function declaration.
    Embed your definition in a suitable test program.

    void getDouble(double& inputNumber);
    //Postcondition:inputNumber is given a value that the user approves of.

    You can assume that the user types in the input in normal everyday notation, such as 23.789, and does not use e-notation to type in the number. Model your definition of the function so that your function calls another function called readAndClean with the following prototype:

    void readAndClean(double& n);

    Function readAndClean reads a line of input, discards all symbols except the digits then converts the C string to double and sets n equal to the value of this double. 

    Sample Dialogue

    Enter input number: $57.53
    You entered 57.53 Is that correct? (yes/no): no
    Enter input number: $77*5xa
    You entered 775 Is that correct? (yes/no): no
    Enter input number: 77.9
    You entered 77.9 Is that correct? (yes/no): no
    Enter input number: $75.9987
    You entered 75.9987 Is that correct? (yes/no): yes
    Final value read in = 75.9987

*/

#include <iostream>
#include <sstream>
#include <string>
#include <cctype>

using namespace std;

void readAndClean(double& n)
{
    string rawNum;
    string num;
    int isDouble = 0;

    cout << "Enter input number: ";
    do {
        getline(cin, rawNum);
    } while (rawNum.empty());
    
    for (int i = 0; i < rawNum.length(); i++)
    {
        if (isdigit(rawNum[i])
            || ('-' == rawNum[i] && num.empty())
            || ('.' == rawNum[i] && !isDouble))
        {
            if ('.' == rawNum[i]) isDouble = 1;
            num.push_back(rawNum[i]);
        }
    }

    stringstream ss(num);
    ss >> n;
}

void getDouble(double& inputNumber)
{
    double n;
    string ans;

    while (1)
    {
        readAndClean(n);
        cout << "You entered " << n << " Is that correct? (yes/no):";
        
        getline(cin, ans);
        if (ans == "yes")
        {
            inputNumber = n;
            return;
        }
    }
}

int main(int argc, char** argv)
{

    double inputNumber;

    getDouble(inputNumber);

    cout << "Final value read in = " << inputNumber << endl;    
}