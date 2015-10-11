#include <iostream>
#include <string>
#include <fstream>
#include<cstdlib> // for system("pause")

using namespace std;

// global variables; are used anywhere within the program without re-definition

 /*
  * 7.  FOR PRACTICE, do NOT use the same variable names as ARGUMENTS in the
 *      function call and PARAMETERS in the function heading definition.
 */      
 /*
  * 8.  You MUST use function prototypes (before main) and you MUST define
 *      your functions AFTER main.
 */

 /*
  * 9.  The console (cout) and file writes remain in main.
 */

int openFiles(ifstream& inStream, ofstream& outStream);

void readDatas(ifstream& inputFile, int& val, int& n);

float calAvg(int tt, int numVals);

void closeFiles(ifstream& input, ofstream& output);

int main () {
    ifstream inData;
    ofstream outData;
    int numValues, total;
    float avg;

  int hr = openFiles(inData, outData);

  if (hr < 0)
      return 0;

  readDatas(inData, total, numValues);

  avg = calAvg(total, numValues);

  cout << "You entered " << numValues << " numbers totaling "  <<  total << " with a average of " << avg << endl;
  outData <<  "You entered " << numValues << " numbers totaling "  <<  total << " with a average of " << avg << endl;

  closeFiles(inData, outData);

  system("pause");



}


/*
 *
 * 2.  Create int function(s) for opening the input and output files.  These
 *     opens may be in the same function.  This function MUST RETURN an int value 
 *     to indicate the success or failure of the open(s).  Test this value and 
 *     take appropriate action after the function call.  This function MUST have
 *     the following parameters, in any order:
 *      a.  input file stream
 *      b.  output file stream
 */                                 

int openFiles(ifstream& inStream, ofstream& outStream)
{
    string ifileName = "inFile.txt";
    string ofileName = "outFile.txt";

    inStream.open(ifileName.c_str());
    if (!inStream) {
        cout << "Error opening file: " << ifileName << ".  Exiting program..." << endl;
        return -1;
    }

    outStream.open(ofileName.c_str());
    if (!outStream) {
        cout << "Error opening file: " << ofileName << ".  Exiting program..." << endl;
        return -1;
    }

    return 0;
}

 /*
  * 3.  Create a void function to read the data file.  This function MUST have the
 *      following parameters, in any order:
 *        a.  input file stream
 *        b.  OUT parameter to return the total of all values
 *        c.  OUT parameter to return the number of values read
 */        

void readDatas(ifstream& inputFile, int& val, int& n)
{
    int i;
    int tmp;

    inputFile >> n;

    val = 0;
    for (i = 0; i < n; i++) {
        inputFile >> tmp;
        val += tmp;
    }

}

 /*
  * 4.  Create a value returning function (I'm leaving it up to YOU to choose
 *      the correct type) to return the avg of the numbers read.  This function
 *      MUST have the following parameters, in any order:
 *          a.  IN parameter to pass in the total of the numbers read
 *          b.  IN parameter to pass in the number of values read
 */                                                                                       

float calAvg(int total, int numVals)
{
    return float(total) / float(numVals);
}

 /*
  * 5.  Create void function(s) for closing the input and output files.  These
 *      closes may be in the same function.  This function MUST have the following
 *      parameters, in any order.
 *          a.  input file stream
 *          b.  output file stream
 */
void closeFiles(ifstream& input, ofstream& output)
{
    input.close();
    output.close();
}


