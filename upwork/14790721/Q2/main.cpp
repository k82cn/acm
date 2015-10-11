#include <iostream>
#include <fstream>
#include <sstream>
#include <stdexcept>
#include <map>
#include <vector>
#include <cstdlib>

using namespace std;

char* unitList[] = 
{
    "cup",
    "teaspoon",
    0,
};

// conver 1/2 to double
// conver 1 and 1/2 to 1.5
static double string2double(const string& str);

// conver double (0.25) to string 1/4
// conver 1.5 to 1 and 1/2
static string double2string(double d);


// the class for item in recipe
struct Item
{
    double num;
    string unit;
    string ingr;

    // key: type, value: subs
    map< int, vector<Item> > subs;

    Item(): num(0) {}

    void reset() 
    {
        num = 0;
        unit.clear();
        ingr.clear();
        subs.clear();
    }

    friend istream& operator>> (istream& in, Item& it);
    friend ostream& operator<< (ostream& out, Item& it);
};

// init dababase of ingredient and substitution
void initDataBase(vector<Item>& db);

// Get the subsitution by type
bool getItem(const vector<Item>& db, Item& it, int type);

int getType()
{
    int type;
    // get user's concerns: high cholesterol, high blood pressure, weight loss, gluten allergy
    cout << "Please input your health concern: "
            "1. high cholesterol; 2. high blood pressure; 2. weight loss; 3. gluten allergy" << endl <<
            "[1-3]:";

    cin >> type;

    return type;

}

int main(int argc, char** argv)
{
    int type;
    string line;
    fstream recipe;

    vector<Item> db;

    if (argc != 2)
    {
        cout << "Usage: " << argv[0] << " recipe_file" << endl;
        return 1;
    }

    // open recipe files
    recipe.open(argv[1], fstream::in);

    if (!recipe.is_open())
    {
        throw runtime_error("Failed to open <" + string(argv[1]) + ">; please check the file exist and has read permission.");
    }

    // print warning
    cerr << "WARN: Always consult your physician before making significant changes to your diet." << endl;

    // init database of Ingredient and Substitution
    initDataBase(db);

    type = getType();

    while (getline(recipe, line))
    {
        Item it;
        stringstream ss(line);

        // de-serialize ingredient into item
        ss >> it;

        // get item by ingredient
        bool found = getItem(db, it, type);

        if (!found)
        {
            cout << line << endl;
        }
        else
        {
            cout << it << endl;
        }
    }

    return 0;
}

// convert 1/2 to double
static double s2d(const string& str)
{
    double f = 0, d = 1;
    stringstream ss(str);
    ss >> f;
    // ignore /
    ss.get();
    ss >> d;
    return f/d;
}

// conver 1/2 to double
// conver 1 and 1/2 to 1.5
static double string2double(const string& str)
{
    if (str.find("and") == string::npos)
    {
        if (str.find("/") == string::npos)
        {
            return atof(str.c_str());
        }
        else
        {
            return s2d(str);
        }
    }
    else
    {
        double n;
        string a; // and
        string d; // double
        stringstream ss(str);
        ss >> n;
        ss >> a;
        ss >> d;
        return n + s2d(d);
    }

    return 0;
}

// conver double (0.25) to string 1/4
// conver 1.5 to 1 and 1/2
static string double2string(double d)
{
    stringstream ss;
    int f = (int) d;

    if (d - f < 0.0001)
    {
        ss << f;
    }
    else
    {
        for ( int i = 1; i < 8; i++)
        {
            if (d - f - (double(i))/8.0 < 0.0001)
            {
                if (f)
                {
                    ss << f << " and ";
                }
                switch(i)
                {
                    case 2:ss << "1/4"; break;
                    case 4:ss << "1/2"; break;
                    case 6:ss << "3/4"; break;
                    default :ss << i << "/8";
                }
                break;
            }
        }
    }

    return ss.str();
}

ostream& operator<< (ostream& out, Item& it)
{
    if (it.subs.empty())
    {
        out << double2string(it.num) << " " << it.unit << " " << it.ingr;
    }
    else
    {
        map<int, vector<Item> >::iterator subIt = it.subs.begin();
        vector<Item>::iterator typeIt = subIt->second.begin();

        out << double2string(typeIt->num) << " " << typeIt->unit << " " << typeIt->ingr;
        typeIt ++;

        for(; typeIt != subIt->second.end(); typeIt++)
        {
            out << " and " << double2string(typeIt->num) << " " << typeIt->unit << " " << typeIt->ingr;
        }
    }

    return out;
}

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


istream& operator>> (istream& in, Item& it)
{
    string num, unit, ingr;
    in >> num;

    in >> unit;
    if (unit == "and")
    {
        // if number is 1 and 1/2, read them all
        num = num + " " + unit;
        in >> unit;
        num = num + " " + unit;
    }
    else
    {
        // if unit is in candidate list, assign it
        for(char** cur = unitList; *cur != 0; cur++)
        {
            if (unit == *cur)
            {
                it.unit = unit;
            }
        }
    }

    // convert 1/2 to double
    it.num = string2double(num);

    getline(in, it.ingr);

    if (it.unit.empty())
    {
        // if unit is empty, it should be part of ingre, e.g. eggs
        it.ingr = unit + it.ingr;
    }

    trim(it.ingr);

    return in;
}

void initDataBase(vector<Item>& db)
{

    Item it, sub;
    vector<Item> subs;

// 1 cup sour cream     1 cup yogurt

    it.num = 1;
    it.unit = "cup";
    it.ingr = "sour cream";
        sub.num = 1;
        sub.unit = "cup";
        sub.ingr = "yogurt";
        subs.push_back(sub);
    it.subs[1] = subs;
    db.push_back(it);

    it.reset();
    sub.reset();
    subs.clear();

// 1 cup milk           2 cup evaporated milk and 2 cup water
    it.num = 1;
    it.unit = "cup";
    it.ingr = "milk";
        sub.num = 2;
        sub.unit = "cup";
        sub.ingr = "evaporated milk";
        subs.push_back(sub);

        sub.num = 2;
        sub.unit = "cup";
        sub.ingr = "water";
        subs.push_back(sub);
    it.subs[1] = subs;
    db.push_back(it);

    it.reset();
    sub.reset();
    subs.clear();


// 1 teaspoon lemon juice   1/2 teaspoon vinegar
    it.num = 1;
    it.unit = "teaspoon";
    it.ingr = "lemon juice";
        sub.num = 0.5;
        sub.unit = "teaspoon";
        sub.ingr = "vinegar";
        subs.push_back(sub);

    it.subs[1] = subs;
    db.push_back(it);

    it.reset();
    sub.reset();
    subs.clear();


// 1 cup sugar          ½ cup honey, 1 cup molasses
//                      Or 1/4 cup agave nectar
    it.num = 1;
    it.unit = "cup";
    it.ingr = "sugar";
        sub.num = 0.5;
        sub.unit = "cup";
        sub.ingr = "honey";
        subs.push_back(sub);

        sub.num = 1;
        sub.unit = "cup";
        sub.ingr = "molasses";
        subs.push_back(sub);
    it.subs[1] = subs;
    subs.clear();

        sub.num = 0.25;
        sub.unit = "cup";
        sub.ingr = "agave nectar";
        subs.push_back(sub);
    it.subs[2] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();

// 1 cup butter         1 cup margarine or yogurt
    it.num = 1;
    it.unit = "cup";
    it.ingr = "butter";
        sub.num = 1;
        sub.unit = "cup";
        sub.ingr = "margarine or yogurt";
        subs.push_back(sub);
    it.subs[1] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();

// 1 cup flour          1 cup rye or rice flour
    it.num = 1;
    it.unit = "cup";
    it.ingr = "flour";
        sub.num = 1;
        sub.unit = "cup";
        sub.ingr = "rye or rice flour";
        subs.push_back(sub);
    it.subs[1] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();


// 1 cup mayonnaise     1 cup cottage cheese
//                      Or 1/8 cup mayonnaise and 7/8 cup yogurt
    it.num = 1;
    it.unit = "cup";
    it.ingr = "mayonnaise";
        sub.num = 1;
        sub.unit = "cup";
        sub.ingr = "cottage cheese";
        subs.push_back(sub);

    it.subs[1] = subs;
    subs.clear();

        sub.num = 0.125;
        sub.unit = "cup";
        sub.ingr = "mayonnaise";
        subs.push_back(sub);

        sub.num = 0.875;
        sub.unit = "cup";
        sub.ingr = "yogurt";
        subs.push_back(sub);

    it.subs[2] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();


// 1 egg                2 tablespoons cornstarch, arrowroot flour
//                      Or potato starch or 2 egg whites
//                      Or ½ of a large banana (mashed)
    it.num = 1;
    it.unit = "";
    it.ingr = "egg";
        sub.num = 2;
        sub.unit = "tablespoon";
        sub.ingr = "cornstarch, arrowroot flour";
        subs.push_back(sub);

    it.subs[1] = subs;
    subs.clear();

        sub.num = 2;
        sub.unit = "";
        sub.ingr = "egg whites";
        subs.push_back(sub);

    it.subs[2] = subs;
    subs.clear();

        sub.num = 0.5;
        sub.unit = "";
        sub.ingr = "large banana";
        subs.push_back(sub);

    it.subs[3] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();

// 1 cup milk           1 cup soy milk
    it.num = 1;
    it.unit = "cup";
    it.ingr = "milk";
        sub.num = 1;
        sub.unit = "cup";
        sub.ingr = "soy milk";
        subs.push_back(sub);
    it.subs[2] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();

// 1/4 cup oil            ¼ cup apple sauce
    it.num = 0.25;
    it.unit = "cup";
    it.ingr = "oil";
        sub.num = 0.25;
        sub.unit = "cup";
        sub.ingr = "apple sauce";
        subs.push_back(sub);
    it.subs[1] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();

// White bread          whole-grain bread
    it.num = 1;
    it.unit = "";
    it.ingr = "white bread";
        sub.num = 1;
        sub.unit = "";
        sub.ingr = "whole-grain bread";
        subs.push_back(sub);
    it.subs[1] = subs;
    subs.clear();

    db.push_back(it);
    it.reset();
    sub.reset();
}

bool getItem(const vector<Item>& db, Item& it, int type)
{
    for (vector<Item>::const_iterator cit = db.begin(); cit != db.end(); cit++)
    {
        if (cit->ingr == it.ingr)
        {
            if (cit->subs.empty())
            {
                return false;
            }
            else
            {
                it.subs.clear();
                vector<Item> sub;
                double cnt =  it.num / cit->num;
                map<int, vector<Item> >::const_iterator i = cit->subs.find(type);
                if (i == cit->subs.end())
                {
                    return false;
                }
                else
                {
                    for (vector<Item>::const_iterator j = i->second.begin(); j != i->second.end(); j++)
                    {
                        Item t;
                        t.num = j->num * cnt;
                        t.unit = j->unit;
                        t.ingr = j->ingr;
                        sub.push_back(t);
                    }
                    it.subs[type] = sub;
                }
                return true;
            }
        }
    }
    return false;
}



