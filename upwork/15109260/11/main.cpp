
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

/*
 *
 * 1.  Write a program to do the following:
 * 2.  Create an array to hold up to 20 integers.
 * 3.  Create a data file or download attached text file (twenty_numbers.txt) that contains UP TO 20 integers.
 * 4.  Request the input and output file names from the user.  Open the files
 *     being sure to check the file state.
 * 5.  Request from the user HOW MANY numbers to read from the data file,
 *     up to twenty.  Request the number until the user enters 20 or less, but not less
 *     than 0.  The user enters the number of integers to read.  The integers are stored
 *     in the file and are to be read from the file.
 *     
 * 6.  Write an integer function that reads from the opened data file: the number
 *     of integers the user wants to read, and store the numbers in 
 *     in the array. For example, if the user wants to read 13 numbers, 
 *     read 13 of the 20 that may be in the file.
 *     
 * 7.  Write an integer function that writes to the opened output file AND THE
 *     CONSOLE, the numbers stored in the array.
 *     
 * 8.  NO GLOBAL variables are to be used.  The input and output file variables
 *     must be passed into the functions as parameters, the number of integers
 *     to read and output must be passed in to each function, the array must be passed
 *     in to each function.
 */

int readDatas(ifstream& inData, int* arr, int n)
{
    int d;
    for (int i = 0; i < n; i++)
    {
        if (!(inData >> d))
        {
            cout << "Failed to read data from input file, exiting..." << endl;
            return -1;
        }

        arr[i] = d;
    }
    return n;
}

int writeDatas(ofstream& outData, int* arr, int n)
{
    for (int i = 0; i < n; i++)
    {
        outData << arr[i] << " ";
        cout << arr[i] << " ";
    }
    cout << endl;
    return n;
}


int main (int argc, char** argv)
{
    int arr[20] = {0};
    int n;
    ifstream inData;
    ofstream outData;
    string ifileName, ofileName;

    cout << "input file's name: " ;
    cin >> ifileName;

    inData.open(ifileName.c_str());
    if (!inData)
    {
        cout << "Failed to open file " << ifileName << ", exit..." << endl;
        return 0;
    }

    cout << "output files' name: ";
    cin >> ofileName;

    outData.open(ofileName.c_str());
    if (!outData)
    {
        cout << "Failed to open file " << ifileName << ", exit..." << endl;
        return 0;
    }

    do
    {
        cout << "how many integers do you want to read?" << endl;
        cin >> n;
    } while ( n > 20 || n < 0);

    if (readDatas(inData, arr, n) < 0)
        return 0;

    writeDatas(outData, arr, n);

    return 0;
}
