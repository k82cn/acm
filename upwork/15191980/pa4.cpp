#include <iostream>
#include <iomanip>
#include <sstream>
#include "pa4.h"

using namespace std;

int main(int argc, char** argv)
{
    ifstream salesInput, prodInput;
    ofstream err;
    Sales sales[30];
    int ss;
    Product prods[50];
    int ps;
    int rc = 0;

    rc = openFiles(salesInput, prodInput, err);

    if (rc != 3)
    {
        return 1;
    }

    ps = readProducts(prodInput, prods, 50, err);

    ss = readAndCalSales(salesInput, sales, 30, prods, ps, err);

    printSales(sales, ss);

    closeFiles(salesInput, prodInput, err);

    return 0;
}

int readAndCalSales(std::ifstream& in, Sales* sales, int ss, Product* prods, int ps, ofstream& err)
{
    string line;
    int res = 0;

    while(res < ss)
    {
        getline(in, line);
        if (in.eof()) { break; }
        stringstream s(line);
        if (!(s >> sales[res].firstName && 
                    s >> sales[res].lastName && 
                    s >> sales[res].companyID))
        {
            cerr << "Failed to parse salses information: <" << line << ">." << endl;
            err  << "Failed to parse salses information: <" << line << ">." << endl;
            continue;
        }

        sales[res].totalSales = 0;
        string pid;
        int num; 
        while (true)
        {
            if (s >> pid >> num)
            {
                double up = 0;
                for (int i = 0; i < ps; i++)
                {
                    if (prods[i].productNumber == pid)
                    {
                        up = prods[i].unitPrice;
                        break;
                    }
                }
                sales[res].totalSales += up * num;
            }
            else
            {
                break;
            }
        }

        if (sales[res].totalSales)
            sales[res].bonus = 1;

        res++;
    }

    return res;

}

int printSales(Sales* ss, int size)
{
    cout << left << setw(12) << "First Name" 
         << setw(12) << "Last Name" 
         << setw(10)  << "ID" 
         << setw(15) << "Total Sales" 
         << setw(10) << "Bonus"
         << endl;

    for (int i = 0; i < size; i++) {
        cout << left << setw(12) << ss[i].firstName 
             << setw(12) << ss[i].lastName
             << setw(10)  << ss[i].companyID
             << setw(15) << ss[i].totalSales
             << setw(10) << ss[i].bonus
             << endl;
    }
}

int readProducts(std::ifstream& in, Product* ps, int size, ofstream& err)
{
    string line;
    int res = 0;

    while(res < size) {
        getline(in, line);
        if (in.eof()) { break; }
        stringstream s(line);
        if (!(s >> ps[res].productNumber && s >> ps[res].unitPrice)) {
            cerr << "Failed to parse product information: <" << line << ">." << endl;
            err  << "Failed to parse product information: <" << line << ">." << endl;
            continue;
        }
        res++;
    }

    return res;

}

int openFiles(ifstream& salesInput, ifstream& prodInput, ofstream& err)
{
    salesInput.open("sales.txt");

    if (!salesInput.is_open()) {
        cout << "Failed to open sales.txt in current working directory." << endl;
        return 0;
    }

    prodInput.open("products.txt");
    if (!prodInput.is_open()) {
        cout << "Failed to open products.txt in current working directory." << endl;
        return 1;
    }
    err.open("err.txt");
    if (!err.is_open()) {
        cout << "Failed to open err.txt in current working directory." << endl;
        return 2;
    }

    return 3;
}

int closeFiles(ifstream& salesInput, ifstream& prodInput, ofstream& err)
{
    salesInput.close();
    prodInput.close();
    err.close();
}

