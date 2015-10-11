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

#include <string>

enum Rating{
   G = 1, PG, PG_13, R, NC_17
};

struct Movie
{
    std::string name; // move name
    Rating rating; // movie rating   DEFINE this as the TYPE of the enum created in step 1.
    int year; // year produced
    std::string star; // main star of the movie, i.e., person
    char saw; // value indicating whether the user saw the movie.  Can be a char, int, etc.  storing a Y or N type value
    char like; //value indicating whether the user liked the movie.  Can be a char, int, etc.  storing a Y or N type value
};

int readMovie(struct Movie& m);

int printMovie(struct Movie& m);

