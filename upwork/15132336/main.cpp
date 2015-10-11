/**
 *
 * Write a program that can be used to calculate the federal tax obligation (amount of taxes owed) using the following information:
 *
 * For a single person, the standard exemption is $3,000
 * For married people, the standard exemption is $6,000
 * Personal exemption is $700 per person
 *
 * A person may put UP TO 5% of his/her gross (total income before taxes) in a pension plan.  This can be 0, but not over 5%
 *
 * Tax rates/amounts are:
 * 15% - Taxable Income between $0 and $20,000
 * $2,250 plus 25% of taxable income OVER $20,000 – Taxable Income between $20,001 and $50,000
 * $8,460 plus 35% of taxable income OVER $50,000
 *
 * Calculate the taxable income as follows:
 * Subtract the sum of (1) the standard exemption, (2) the amount contributed to a pension fund, and (3) the personal exemption from the Gross income.  For example, if a married couple has two (2) children, personal exemption is 4 x $700 = $2,800.  If a single person has 1 child, personal exemption is 2 * $700 = $1,400.00.
 * Do NOT include the pension as an exemption (in the exemption total), include it as a deduction.
 *
 * Tax obligation (taxes owed) will be calculated against the taxable income using the Tax Rates indicated above.
 *
 *
 *
 *
 * Evaluate the marital status using a SWITCH statement.  
 *
 * Use the provided data file with the following information (inputFile.txt).  
 * Each ROW of data contains:
 * Surname of family / single individual (last name only)
 * Marital status – represented by a character (M, S)
 * Number of children under the age of 14 – may be 0
 * Gross salary (before taxes)
 * Percentage of gross salary contributed to pension fund.  This may be 0.  This will be a percentage, not an amount, i.e., .05 will mean 5%.
 *
 * For each row of input in the data file, output the following to the CONSOLE (screen) in a neat, readable format.  Output each input line on one output line.  Use manipulators to output values in readable columns.  Use dollar signs before monetary values.  All numerical values must have 2 decimal points.  Use headings to indicate values displayed (you DO NOT need to use the following as headings, you may use your own):
 * Surname of the family (last name only)
 * Gross Income
 * Total exemption amount (does not include pension deduction)
 * Pension deduction amount in dollars (not the percentage)
 * Taxable Income
 * Tax obligation (taxes owed)
 *
 * For each row of input in the data file, output the following to an OUTPUT FILE, name of your choice (in addition to the console) in a neat, readable format.  Output each input line on one output line. Use manipulators to output values in readable columns.  Use dollar signs before monetary values.  All numerical values must have 2 decimal points.  Use headings to indicate values displayed (you DO NOT need to use the following as headings; you may user your own).  
 * You MAY hardcode the name of the output file, however, check the state of the open of the output file.  If the open fails, output an appropriate message and exit the program:
 * Surname of the family (last name only)
 * Gross Income
 * Total exemption amount (does not include pension deduction)
 * Pension deduction amount in dollars (not the percentage)
 * Taxable Income
 * Tax obligation (taxes owed)
 *
 * For 9 and 10, output must be in the following form, i.e., one heading for all lines for output:
 * Name    Gross Income   Total Exemptions    Pension Deduction   Taxable Income   Tax Obligation
 *
 * You MAY use abbreviations for headers to fit across the screen.
 *
 *   There will be ERRORS in the data file (see below for possible errors).  If an error in the data is encountered, write the error and the line containing the error to a THIRD OUTPUT FILE, name of your choice (not the console).  DO NOT PROCESS THE DATA; continue with the next input line.  Only log the first error found in each line/row of data.
 *   You MAY hardcode the name of the output error file, however, check the state of the open of the output file.  If the open fails, output an appropriate message and exit the program:
 *
 *   Amount contributed to a pension fund MAY NOT be OVER .05.  If the value in the input file is OVER .05, output the message “Invalid Pension Percentage” and the line of data containing the error on a separate line.  No headings.
 *
 *   Marital Status must be ‘S’ or ‘M’.  If any other value, output the message “Invalid Marital Status” and the line of data containing the error on a separate line.  No headings.
 *
 *
 *   CLOSE ALL files before exiting the program.
 */

#include <iostream>
#include <iomanip>
#include <fstream>
#include <sstream>

using namespace std;

int main (int argc, char** argv)
{
    ifstream in;
    ofstream out("out.txt");
    ofstream err("err.txt");
    string name;
    char status;
    int numOfChild;
    double gross, pf;
    string line;


    /* Request the name of the input file from the user.  Do not “hard-code” the file name in the program. */
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

    /* Check the state of all file opens.  If any file does not open properly, display an appropriate message and exit the program. */
    if (!out.is_open() && !err.is_open())
    {
        cout << "Failed to open output and error files." << endl;
        return 1;
    }

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


    /* Read the file using a looping construct that checks for the end of file (not the number of rows in the data file). */
    while (getline(in, line))
    {
        stringstream ss(line);
        if (! (ss >> name >> status >> numOfChild >> gross >> pf))
        {
            continue;
        }

        if (pf > 0.5)
        {
            cout << "Invalid Pension Percentage" << endl;
            cout << line << endl;
            err << "Invalid Pension Percentage" << endl;
            err << line << endl;
            continue;
        }

        switch(status)
        {
            case 'S':
                {
                    double pp = gross * pf;
                    double exemption = 3000 + (numOfChild + 1) * 700;
                    double taxables = gross - exemption - pp;
                    double obligation = 0;
                    /* 15% - Taxable Income between $0 and $20,000
                     * $2,250 plus 25% of taxable income OVER $20,000 – Taxable Income between $20,001 and $50,000
                     * $8,460 plus 35% of taxable income OVER $50,000
                     */

                    if (taxables <= 20000 && taxables >= 0) obligation = 0.15 * taxables;
                    else if (taxables <= 50000 && taxables > 20000) obligation = 2250 + (taxables - 20000) * 0.25;
                    else if (taxables > 50000) obligation = 8460 + (taxables - 50000) * 0.35;

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

                break;
            case 'M':
                {
                    double pp = gross * pf;
                    double exemption = 6000 + (numOfChild + 2) * 700;
                    double taxables = gross - exemption - pp;
                    double obligation = 0;
                    /* 15% - Taxable Income between $0 and $20,000
                     * $2,250 plus 25% of taxable income OVER $20,000 – Taxable Income between $20,001 and $50,000
                     * $8,460 plus 35% of taxable income OVER $50,000
                     */

                    if (taxables <= 20000 && taxables >= 0) obligation = 0.15 * taxables;
                    else if (taxables <= 50000 && taxables > 20000) obligation = 2250 + (taxables - 20000) * 0.25;
                    else if (taxables > 50000) obligation = 8460 + (taxables - 50000) * 0.35;

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

                break;
            default:
                cout << "Invalid Marital Status" << endl;
                cout << line << endl;

                err << "Invalid Marital Status" << endl;
                err << line << endl;

                break;
        }
    }

    

    return 0;
}
