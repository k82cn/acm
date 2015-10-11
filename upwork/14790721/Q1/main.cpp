
/*
 * (Spam Scanner) Spam (or junk email) costs U.S. organizations billions of dollars a year in spam-prevention software, equipment, network resources, bandwidth, and lost productivity. 
 * Research online some of the most common spam e-mail messages and words, and check your own junk e-mail folder. Create a list of 30 words and phrases commonly found in spam messages. Write a C++ application in which the user reads an e-mail message from a file. Then, scan the message for each of the 30 keywords or phrases. For each occurrence of one of these within the message, add a point to the message's "spam score." Next, rate the likelihood that the message is spam, based on the number of points it received. The likelihood is the number of points divided by the total number of words. 
 */

#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>

using namespace std;

char* spanWordList[] = {
    "free",
    "congratul",
    "won",
    "diet",
    "opport",
    "smoking",
    "viagra",
    "weight",
    "degree",
    "dvd",
    "gambling",
    "horoscope",
    "ink",
    "obligation",
    "cash",
    "cheap",
    "credit",
    "deal",
    "loan",
    "income",
    "debt",
    "promo",
    "rate",
    "shop",
    "stock",
    "wealth",
    "trading",
    "merchant",
    "mortgage",
    "insurance",
    0};

int main(int argc, char** argv)
{

    if (argc != 2)
    {
        cout << "Usage: main email" << endl;
        return 1;
    }

    fstream email(argv[1], fstream::in);
    if (!email.is_open())
    {
        throw runtime_error("Failed to open <" + string(argv[1]) + ">, please check the file exist and has read permission.");
    }

    string word;
    double wordCnt = 0;
    double spanCnt = 0;

    while (email >> word)
    {
        for (char** cur = spanWordList; *cur !=0; cur++)
        {
            if (word == *cur)
            {
                spanCnt++;
            }
        }

        wordCnt++;
    }

    cout << "The span rate of <" << argv[1] << "> is " << (spanCnt/(wordCnt?wordCnt:1)) << endl;

    return 0;
}
