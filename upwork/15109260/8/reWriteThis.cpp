#include <iostream>
#include <string>
#include <fstream>
#include<cstdlib> // for system("pause")

using namespace std;

// global variables; are used anywhere within the program without re-definition

string ifileName, ofileName;
ifstream inData;
ofstream outData;
int numValues, total, i, num;
float avg;

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

int openFiles(const string& inputFile, const string& outputFile)
{
    inData.open(inputFile.c_str());
    if (!inData) {
        return -1;
    }

    outData.open(outputFile.c_str());
    if (!outData) {
        return -2;
    }

    return 0;
}

 /*                                 3.  Create a void function to read the data file.  This function MUST have the
 *                                     following parameters, in any order:
 *                                           a.  input file stream
 *                                                 b.  OUT parameter to return the total of all values
 *                                                       c.  OUT parameter to return the number of values read
 *                                                             
 *                                                             4.  Create a value returning function (I'm leaving it up to YOU to choose
 *                                                                 the correct type) to return the avg of the numbers read.  This function
 *                                                                     MUST have the following parameters, in any order:
 *                                                                           a.  IN parameter to pass in the total of the numbers read
 *                                                                                 b.  IN parameter to pass in the number of values read
 *                                                                                       
 *                                                                                       5.  Create void function(s) for closing the input and output files.  These
 *                                                                                           closes may be in the same function.  This function MUST have the following
 *                                                                                               parameters, in any order.
 *                                                                                                     a.  input file stream
 *                                                                                                           b.  output file stream
 *
 *                                                                                                           6.  Move the global variables (defined before main in reWriteThis.cpp)
 *                                                                                                               and place them within the appropriate functions.  Some will be 
 *                                                                                                                   needed with the related function, some will still be needed in main.
 *                                                                                                                       
 *                                                                                                                       7.  FOR PRACTICE, do NOT use the same variable names as ARGUMENTS in the
 *                                                                                                                           function call and PARAMETERS in the function heading definition.
 *                                                                                                                               
 *                                                                                                                               8.  You MUST use function prototypes (before main) and you MUST define
 *                                                                                                                                   your functions AFTER main.
 *                                                                                                                                       
 *                                                                                                                                       9.  The console (cout) and file writes remain in main.
 *
 *                                                                                                                                       10. Either create a file named inFile.txt that contains:
 *                                                                                                                                             a.  The number of values to add
 *                                                                                                                                                   b.  A number of integer values matching the number read in a.
 *                                                                                                                                                      
 *                                                                                                                                                          OR 
 *                                                                                                                                                              
 *                                                                                                                                                                    c.  Download and use attached inFile.txt
 */


int main () {

  ifileName = "inFile.txt";
  inData.open(ifileName.c_str());
  if (!inData) {
    cout << "Error opening file: " << ifileName << ".  Exiting program..." << endl;
    return 0;
  }

  inData >> numValues;

  for (i = 0; i < numValues; i++) {
    inData >> num;
    total += num;
  }

  avg = float(total) / float(numValues);

  ofileName = "outFile.txt";
  outData.open(ofileName.c_str());
  if (!outData) {
    cout << "Error opening file: " << ofileName << ".  Exiting program..." << endl;
    return 0;
  }

  cout << "You entered " << numValues << " numbers totaling "  <<  total << " with a average of " << avg << endl;
  outData <<  "You entered " << numValues << " numbers totaling "  <<  total << " with a average of " << avg << endl;

  inData.close();
  outData.close();

  system("pause");



}
