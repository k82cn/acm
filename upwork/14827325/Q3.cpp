#include <iostream>
#include <fstream>
#include <sstream>

#include <iomanip>

using namespace std;

const int MAX_LEN = 19;

/*
 * The definition of Player
 */
class Player
{
public:
    int id;
    int cnt;

    int hits;
    int walks;
    int rbis;

    string firstName;
    string lastName;

    Player()
    {
        id = 0;
        cnt = 0;
        hits = 0;
        walks = 0;
        rbis = 0;
    }

};

int main(int argc, char **argv)
{
    string line;
    Player ps[MAX_LEN];
    
    fstream data("../Team.txt", ios::in);
    
    if (!data)
    {
        throw runtime_error("Failed to open team.txt");
    }
    
    // Read the data from Team.txt
    while (getline(data, line))
    {
        int id, cnt, hits, walks, rbis;
        string firstName, lastName;
        
        stringstream ss (line);
        // calculate the summary of count, hits, walks and RBIs
        if (ss >> id >> firstName >> lastName >> cnt >> hits >> walks >> rbis)
        {
            ps[id].id = id;
            ps[id].cnt += cnt;
            ps[id].hits += hits;
            ps[id].walks += walks;
            ps[id].rbis += rbis;
            
            ps[id].firstName = firstName;
            ps[id].lastName = lastName;
        }
        else
        {
            cerr << "'" << line << "' is ignored because of invalid format." << endl;
        }
    }
    
    /*
     * After got all datas, print the avg of hits, walks and RBIs of each player.
     */
    for (int i = 0; i < MAX_LEN; i++)
    {
        if (ps[i].cnt == 0)
            continue;
        int cnt = ps[i].cnt;
        cout << ps[i].id << " ";
        cout << ps[i].firstName << " " << ps[i].lastName << " ";
        cout << cnt << " ";
        cout << setprecision(2) << static_cast<double>(ps[i].hits) / cnt << " " << static_cast<double>(ps[i].walks) / cnt << " " << static_cast<double>(ps[i].rbis) / cnt;
        cout << endl;
    }
    
    return 0;
}
