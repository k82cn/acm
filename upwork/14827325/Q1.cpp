#include <string>
#include <iostream>
#include <sstream>
#include <iterator>
#include <algorithm>

using namespace std;

const int MAXBKS=40;

/*
 * The definition of Book
 */
class Book
{
public:
    string title;
    string author;
    double value;
    friend ostream& operator<< (ostream& out, const Book& bk);
};

/*
 * The operator to print Books info
 */
ostream& operator<< (ostream& out, const Book& bk)
{
    out << "Title:" << bk.title << ", Author: "<< bk.author << ", Value: " << bk.value;
    return out;
}

typedef Book* BookPtr;

bool __cmp_bks_by_title(BookPtr a, BookPtr b)
{
    return a->title < b->title;
}

bool __cmp_bks_by_value(BookPtr a, BookPtr b)
{
    return a->value > b->value;
}

void printBookIndex(BookPtr* bks)
{
    for (BookPtr* cur = bks; *cur != 0; cur++)
    {
        cout << **cur << endl;
    }
}

int main(int argc, char **argv)
{
    Book bks[MAXBKS];
    BookPtr bksIdx_v[MAXBKS] = {0};
    BookPtr bksIdx_t[MAXBKS] = {0};
    
    int len = 0;
    
    cout << "Please input the info of books, empty title to terminate the input."<< endl;

    while (len < MAXBKS)
    {
        string val;
        cout << "Title:" ;
        getline(cin, bks[len].title);
        if (bks[len].title.empty())
        {
            break;
        }
        cout << "Author:";
        getline(cin, bks[len].author);
        cout << "Value:";
        getline(cin, val);
        stringstream ss(val);
        ss >> bks[len].value;
        bksIdx_v[len] = bks+len;
        bksIdx_t[len] = bks+len;
        len ++;
        cout << endl;
    }
    
    cout << endl << "There are " << len << " books in total."<< endl;
    
    sort(bksIdx_t, bksIdx_t+len, __cmp_bks_by_title);
    
    cout << endl << "Sort books by title:"<< endl;
    
    printBookIndex(bksIdx_t);
    
    sort(bksIdx_v, bksIdx_v+len, __cmp_bks_by_value);
    
    cout << endl << "Sort books by value:"<< endl;

    printBookIndex(bksIdx_v);
    
    return 0;
}
