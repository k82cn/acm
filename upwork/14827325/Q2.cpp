#include <iostream>
#include <string>

using namespace std;


/*
 * The definition of Name: firstName, middleName and lastName
 */
class Name
{
public:
    string firstName;
    string middleName;
    string lastName;
};

/*
 * The definition of People: social security number and Name 
 */
class People
{
public:
    string number;
    Name name;
};

/*
 * The function to print people's information; pass info by value
 */
void printPeople(People p)
{
    cout << p.name.firstName << ", " << p.name.lastName << " ";
    // if the middle name is emtpy, do not print it; otherwise, print initial letter
    if (!p.name.middleName.empty())
    {
        cout << p.name.middleName[0] << ". -- ";
    }
    cout << p.number << endl;
}

/*
 * The function to print all people's information; pass info by address
 */
void printPeopleArray(People* ps, int len)
{
    People* pe = ps + len;
    while(ps < pe)
    {
        printPeople(*ps);
        ps++;
    }
}

// main function
int main(int argc, char** argv)
{
    People ps[5] = 
    {
        {"302039823", {"Dribble", "Mick", "Flossie"}},
        {"456739823", {"Beyonce", "Giselle", "Knowles"}},
        {"982334567", {"Nick", "", "Xu"}},
        {"325687289", {"Alicia", "", "Chen"}},
        {"209856761", {"Steven", "Paul", "Jobs"}},
    };
    
    printPeopleArray(ps, 5);
    
    return 0;
}
