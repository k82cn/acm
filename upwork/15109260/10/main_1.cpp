#include <iostream>
#include <string>

using namespace std;

/*1. Create an enumerated type (enum) that represents movie ratings:
   
   G, PG, PG_13, R, NC_17
   
(Remember enum names must following C++ variable name rules, so no dashes (-) allowed)

2.  Create a STRUCTURE with the following information:

    move name
    movie rating   DEFINE this as the TYPE of the enum created in step 1.
    year produced
    main star of the movie, i.e., person
    value indicating whether the user saw the movie.  Can be a char, int, etc.
      storing a Y or N type value
    value indicating whether the user liked the movie.  Can be a char, int, etc.
      storing a Y or N type value
      
3.  Request the above information from the user and store in the structure.  The
    rating may be requested as follows:
    
     1 - G 
     2 - PG
     3 - PG-13
     4 - R
     5 - NC-17

NOTE:  The following may come in handy: structureVariable.move_ratingVariable = enumType(rating value entered)

Where structureVariable    is the variable name of the enum type
      move_ratingVariable  is the variable name in the structure for move rating
      enumType             is the name of the defined enum
      rating value entered is the variable containing the rating value the user entered
      
Try the above before asking!!!
      
4.  Output the information the user entered from the data stored in the STRUCTURE.

    The move name, the year, the main star
    
    Output the rating using the enum as follows:
    
     if  G - This movie is rated G:  General Audiences
     if  PG - This movie is rated PG : Parental Guidance Suggested
     if  PG_13 - This movie is rated PG-13:  Parent's Strongly Cautioned
     if  R - This movie is rated R:  Under 17 requires accompanying adult or adult guardian
     if  NC_17 - This movie is rated NC-17: No one under 17 and under admitted

    
    If the user saw the movie output You saw the movie; else output You did not see the movie.
    
    If the user liked the movie, output You Liked the Move; else output You did not like the movie.
    
*/ 

enum Rating{
   G = 1, PG, PG_13, R, NC_17
};

struct Movie
{
    string name; // move name
    Rating rating; // movie rating   DEFINE this as the TYPE of the enum created in step 1.
    int year; // year produced
    string star; // main star of the movie, i.e., person
    char saw; // value indicating whether the user saw the movie.  Can be a char, int, etc.  storing a Y or N type value
    char like; //value indicating whether the user liked the movie.  Can be a char, int, etc.  storing a Y or N type value
};

int main(int argc, char** argv)
{
    struct Movie m;
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
