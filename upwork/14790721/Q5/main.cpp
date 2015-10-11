/*
Q5: You’ve been asked to write a program to grade a multiple choice exam. The exam has 20 questions, each answered with a little in the range of ‘a’ through ‘f’. The data are stored on a file (exams.dat) where the first line is the key consisting of a string of 20 characters. The remaining lines on the files are exam answers, and consist of a student ID number, a space, and a string of 20 characters. The program should read the key, then read each exam and output the ID number and score to file scores.dat. Erroneous input should result in an error message. For example, given the data:

abcdefabcdefabcdefab
1234567 abcdefabcdefabcdefab
9876543 abddefbbbdefcbcdefac
5554446 abcdefabcdefabcdef
4445556 abcdefabcdefabcdefabcd
3332221 abcdefghijklmnopqrst

The program should output on scores.dat:

1234567 20
9876543 15
5554446 Too few answers
4445556 Too many answers
3332221 Invalid answers

Use functional decomposition to solve the problem and code the solution using functions as appropriate. Be sure to use proper formatting and appropriate comments in your code. The output should be neatly formatted, and the error messages should be informative.

Use at least two functions (properly documented with a header and pre/post conditions) in addition to main(): one function to get the id/answers of one student, and one to compute and output a student’s results. DO NOT use global variables to avoid passing parameters to the functions. When passing an ifstream or ofstream variable, pass it by reference. You do not need to check the answer key in any way (i.e, assume it is always correctly entered.)     
*/

#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>

using namespace std;

// Get the key from exam data files
int getKey(fstream& exam, string& key)
{
    exam >> key;
    return exam.good();
}


// Get the exam result of single student
int getExamResult(fstream& exam, string& id, string& ans)
{
    exam >> id >> ans;
    return exam.good();
}

/*
 * Get the score of the answer
 *  > 0: the score of the answer
 *  -1: too few answers
 *  -2: too many answers
 *  -3: invalid answers
 */
int getScore(const string& key, const string& ans)
{
    int res = 0;
    // too few answers
    if (ans.length() < key.length())
    {
        return -1;
    }

    // too many answers
    if (ans.length() > key.length())
    {
        return -2;
    }

    for (int i = 0; i < key.length(); i++)
    {
        if (key[i] == ans[i])
        {
            res ++;
        }
        else
        {
            // invalid answers
            if (ans[i] > 'f' || ans[i] < 'a')
            {
                return -3;
            }
        }
    }

    return res;
}


// Open the two data files; throw exception when I/O error
static void initDataFile(fstream& exam, fstream& score)
{
    exam.open("exam.dat", fstream::in);
    if (!exam.is_open())
    {
        throw runtime_error("Failed to open <exam.dat>, please check the file exist and has the read permission.");
    }
    
    score.open("score.dat", fstream::out | fstream::app);

    if (!score.is_open())
    {
        throw runtime_error("Failed to create <score.dat>.");
    }

}

int main(int argc, char** argv)
{
    string key, id, ans;

    fstream exam, score;

    // init the two data file: exam.dat & score.dat
    initDataFile(exam, score);

    // get the key from exam.dat
    getKey(exam, key);


    // get exam result of each student
    while (getExamResult(exam, id, ans))
    {
        score << id << " ";
        // get the score of this student
        int sc = getScore(key, ans);
        // print the score into score.dat
        switch(sc)
        {
            case -1:
                score << "Too few answers" << endl;
                break;
            case -2:
                score << "Too many answers" << endl;
                break;
            case -3:
                score << "Invalid answers" << endl;
                break;
            default:
                score << sc << endl;
        }
    }

    return 0;
}
