/*
 * 1. Create a function to INPUT the information for the movie.  Pass the empty structure in
 *    as a parameter.
 *    
 * 2. Create a function to OUTPUT the information in the structure.  Pass the filled structure
 *    in as a parameter.
 *    
 * 3. Place the ENUM definition, STRUCTURE definition, and FUNCTION prototypes in a HEADER FILE (*.h).
 *    Remove those definitions from the main program and include your header file in the main program
 *    file.  DECLARE the STRUCTURE VARIABLE as a LOCAL (not global) variable in the main program.
 *    
 * 4. Create a new console project using Dev-C++ or Codeblocks.  Add your code file (*.cpp) and header file (*.h)
 *    to the project.  When you compile the project, it is best to have the main code file as the
 *    active tab.
*/

#include <iostream>

#include "movie.h"

using namespace std;

int main(int argc, char** argv)
{
    struct Movie m;
    readMovie(m);
    printMovie(m);
    return 0;
}

int readMovie(struct Movie& m)
{
    cout << "input the information of movie:" << endl;
    cout << "Movie Name: ";
    cin >> m.name;

    while(true)
    {
        int i;
        cout << "Rating: ";
        cin >> i ;
        if (i >= G && i <=NC_17)
        {
            m.rating = (Rating)(i);
            break;
        }
        else
        {
            cout << "The rating is invalid." << endl;
        }
    } ;

    cout << "Year produced: ";
    cin >> m.year;
    cout << "Main star: ";
    cin >> m.star;

    do
    {
        cout << "Did you see the movie? (Y/N): ";
        cin >> m.saw;
    } while (m.saw != 'Y' && m.saw != 'N');

    do
    {
        cout << "Do you like the movie? (Y/N): ";
        cin >> m.like;
    } while (m.like != 'Y' && m.like != 'N');

    return 0;
}


int printMovie(struct Movie& m)
{
    cout << m.name << ", " << m.year << ", " << m.star << endl;

    cout << "Rating: ";
    switch(m.rating)
    {
        case G:
            cout << "General Audiences" << endl;
            break;
        case PG:
            cout << "Parental Guidance Suggested" << endl;
            break;
        case PG_13:
            cout << "Parent's Strongly Cautioned" << endl;
            break;
        case R:
            cout << "Under 17 requires accompanying adult or adult guardian" << endl;
            break;
        case NC_17:
            cout << "No one under 17 and under admitted" << endl;
            break;
    }

    if (m.saw == 'Y')
        cout << "You saw the movie" << endl;
    else
        cout << "You did not see the movie" << endl;

    if (m.like == 'Y')
        cout << "You Liked the Move" << endl;
    else
        cout << "You did not like the movie" << endl;

    return 0;
}

