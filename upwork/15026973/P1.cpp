#include <iostream>
#include <sstream>

using namespace std;

class Fraction
{
    private:
        int numerator;
        int denominator;
    public:
        Fraction() {};
        Fraction(int n, int d) 
        {
            numerator = n;
            denominator = d;
        };
        Fraction(const Fraction& f) 
        {
            numerator = f.numerator;
            denominator = f.denominator;
        };
        int get_numerator() {return this->numerator;} ;
        int get_denominator() {return this->denominator; } ;

        void set_numerator(int n) {this->numerator = n;} ;
        int set_denominator(int d) {this->denominator = d; } ;

        Fraction multiply(const Fraction& f)
        {
            Fraction result;
            result.set_numerator(f.numerator * this->numerator);
            result.set_denominator(f.denominator * this->denominator);
            return result;
        }

        string str()
        {
            stringstream ss;
            ss << get_numerator() << "/" << get_denominator();
            return ss.str();
        }
};

int main(int argc, char** argv)
{
    int n, d;

    cout << "Enter 1st fraction:";
    cin >> n >> d; 
    Fraction f1(n, d);

    cout << "Enter 2nd fraction:";
    cin >> n >> d;

    Fraction f2(n, d);

    Fraction f3 = f1.multiply(f2);

    cout << f1.str() << " * " << f2.str() << " = " << f3.str() << endl;

    return 0;
}
