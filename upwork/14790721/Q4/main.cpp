/*
 * Description: Write a simple telephone directory program in C++ that looks up phone numbers in a file containing a list of names and phone numbers. The user should be prompted to enter a first name and last name, and the program then outputs the corresponding number, or indicates that the name isn't in the directory. After each lookup, the program should ask the user whether they want to look up another number, and then either repeat the process or exit the program. The data on the file should be organized so that each line contains a first name, a last name, and a phone number, separated by blanks. You can return to the beginning of the file by closing it an opening it again. 
 * Use functional decomposition to solve the problem and code the solution using functions as appropriate. Be sure to use proper formatting and appropriate comments in your code. The output should be clearly labeled and neatly formatted, and the error messages should be informative.
 */

#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <stdexcept>

using namespace std;

class PhoneDirectory
{
    public: 
        /*
         * The constructor of PhoneDiretory: open the telephone directory file.
         */
        PhoneDirectory(const string& pdFile)
        {
            m_phoneDir.open(pdFile.c_str(), fstream::in);
            // if failed to open the phone directory, throw exception
            if (!m_phoneDir.is_open())
            {
                throw runtime_error("Can not open <" + pdFile + 
                        "> as phone directory; please check the file exist and has read permission.");
            }
        }

        /*
         * The de-constructor of PhoneDiretory; close the telephone directory file.
         */
        ~PhoneDirectory()
        {
            m_phoneDir.close();
        }

        void findAndPrintPhone()
        {

            /*
             * Prompt to input first name and last name
             */
            cout << "Please input first name and last name to find a phone number: ";
            do
            {
                getline(cin, m_name);
                trim(m_name);
            } while(m_name.empty());

            /*
             * Get the phone number by user name
             */
            string phoneNum;

            if (getPhoneNum(phoneNum))
            {
                cout << "The phone number of \"" << m_name << "\" is "  << phoneNum << endl;
            }
            else
            {
                cout << "Can not find the phone number of \"" << m_name  << "\""<< endl;
            }
        }

        /*
         * Ask the user whether they want to look up another number: Y/y/<Enter> to continue to find phone number; others to exit the program
         */
        bool findMorePhone()
        {
            string ans;
            cout << "Do you want to find another phone number [Y/n]?:";
            getline(cin, ans);
            return ans.empty() || ans == "Y" || ans == "y";
        }

    private:
        /*
         * Get the phone number by user name
         */
        bool getPhoneNum(string& pn)
        {
            m_phoneDir.seekg(0);
            string line;
            string firstName;
            string lastName;

            while (getline(m_phoneDir, line))
            {
                // Assumption: The data on the file is organized so that each line contains a first name, a last name, and a phone number, separated by blanks
                stringstream ss(line);
                ss >> firstName >> lastName >> pn;
                
                if (m_name == (firstName + " " + lastName))
                {
                    return true;
                }
            }
            return false;
        }

        // trim string
        string& trim(std::string &s)
        {
            if (s.empty())
            {
                return s;
            }

            s.erase(0,s.find_first_not_of(" "));
            s.erase(s.find_last_not_of(" ") + 1);
            return s;  
        }

    private:
        fstream m_phoneDir;
        string m_name;
};

int main(int argc, char** argv)
{

    /*
     * Open "phone.txt" as phone directory
     */
    PhoneDirectory pd("phone.txt");

    do
    {
        pd.findAndPrintPhone();
    } while(pd.findMorePhone());

    return 0;
}
