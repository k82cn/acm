#include <iostream>
#include <iomanip>
#include <fstream>
#include <sstream>

using namespace std;

/* Create a value returning function to calculate total exemption amount.  Return the total exemption amount to the calling routine. */
double calExemption(char status, int numOfChild);

/*
 * Create a value returning function to calculate the taxable income.  Return the taxable income to the calling routine.
 */
double calTaxable(double gross, double pf, double exemption);

/*
 * Create a value returning function to calculate tax obligation.  Return the tax obligation to the calling routine.
 */
double calObligation(double taxables);

/*
 * Create a value returning function to open files.  Both the input and output files may be opened using one function.  The function must contain the prompt for the user to enter the input file name.  You may 'hardcode' the output file names (i.e., does not need to be user-entered).  The value returned to the calling routine must indicate a failure for any of the files or a success for ALL files.  If an open fails, the CALLING routine displays a message and exits the program.
 */

int openFiles(ifstream& in, ofstream& out, ofstream& err);

/* Create a function to close all files prior to program end. */

void closeFiles(ifstream& in, ofstream& out, ofstream& err);

/* Create a function to read the data.  Remember, functions may call other functions. */

void readAndProcessData(ifstream& in, ofstream& out, ofstream& err);


/* Create a function to display the output as indicated in Programming Assignment 2, both to the console and to an output file. */

void printResult(ofstream& out, string name, double gross, double exemption, 
        double pp, double taxables, double obligation);

/* Create a function to output the error messages and data to the error file. */
void printError(ofstream& err, string msg, string line);

int main (int argc, char** argv)
{
    ifstream in;
    ofstream out;
    ofstream err;

    if (openFiles(in, out, err) == 1)
    {
        return 1;
    }

    readAndProcessData(in, out, err);
    
    closeFiles(in, out, err);

    return 0;
}

double calExemption(char status, int numOfChild)
{
    switch (status) {
        case 'S':
            return 3000 + (numOfChild + 1) * 700;
            
        case 'M':
            return 6000 + (numOfChild + 2) * 700;
            
        default:
            return 0;
    }
}

double calTaxable(double gross, double pf, double exemption)
{
    double pp = gross * pf;
    return gross - exemption - pp;
}

double calObligation(double taxables)
{
    double obligation = 0;
    /* 15% - Taxable Income between $0 and $20,000
     * $2,250 plus 25% of taxable income OVER $20,000 – Taxable Income between $20,001 and $50,000
     * $8,460 plus 35% of taxable income OVER $50,000
     */
    
    if (taxables <= 20000 && taxables >= 0) obligation = 0.15 * taxables;
    else if (taxables <= 50000 && taxables > 20000) obligation = 2250 + (taxables - 20000) * 0.25;
    else if (taxables > 50000) obligation = 8460 + (taxables - 50000) * 0.35;

    return obligation;
}

int openFiles(ifstream& in, ofstream& out, ofstream& err)
{
    string line;
    
    cout << "Input file is : " ;
    
    do
    {
        getline(cin, line);
        in.open(line.c_str());
        if (!in.is_open())
        {
            cout << "Failed to open <" << line << ">." << endl;
            return 1;
        }
    } while (line.empty());
    
    out.open("out.txt");
    err.open("err.txt");
    
    if (!out.is_open() && !err.is_open())
    {
        cout << "Failed to open output and error files." << endl;
        return 1;
    }

    return 0;
}

void closeFiles(ifstream& in, ofstream& out, ofstream& err)
{
    in.close();
    out.close();
    err.close();
}

void readAndProcessData(ifstream& in, ofstream& out, ofstream& err)
{
    string line;
    string name;
    char status;
    int numOfChild;
    double gross, pf;
    
    while (getline(in, line))
    {
        stringstream ss(line);
        if (! (ss >> name >> status >> numOfChild >> gross >> pf))
        {
            continue;
        }
        
        if (pf > 0.5)
        {
            printError(err, "Invalid Pension Percentage", line); 
            continue;
        }
        
        if (status != 'S' && status != 'M')
        {
            printError(err, "Invalid Marital Status", line);
            continue;
        }

        double exemption = calExemption(status, numOfChild);
        double taxables = calTaxable(gross, pf, exemption);
        double obligation = calObligation(taxables);

        printResult(out, name, gross, exemption, gross * pf, taxables, obligation);

    }

}


void printResult(ofstream& out, string name, double gross, double exemption, 
        double pp, double taxables, double obligation)
{
    static int printHeader = 1;

    if (printHeader)
    {
        cout << left << setw(10) << "Name"
            << setw(16) << "Gross Income"
            << setw(20) << "Total Exemptions"
            << setw(21) << "Pension Deduction"
            << setw(18) << "Taxable Income"
            << setw(18) << "Tax Obligation" << endl;
        
        out << left << setw(10) << "Name"
            << setw(16) << "Gross Income"
            << setw(20) << "Total Exemptions"
            << setw(21) << "Pension Deduction"
            << setw(18) << "Taxable Income"
            << setw(18) << "Tax Obligation" << endl;

        printHeader = 0;
    }

    cout << left << setw(10) << name
        << fixed << setprecision(2)
        << "$" << setw(15) << gross
        << "$" << setw(19) << exemption
        << "$" << setw(20) << pp
        << "$" << setw(17) << taxables
        << "$" << setw(17) << obligation
        << endl;
    
    out << left << setw(10) << name
        << fixed << setprecision(2)
        << "$" << setw(15) << gross
        << "$" << setw(19) << exemption
        << "$" << setw(20) << pp
        << "$" << setw(17) << taxables
        << "$" << setw(17) << obligation
        << endl;

}


void printError(ofstream& err, string msg, string line)
{
    cout << msg << endl << line << endl;
    err << msg << endl << line << endl;
}
