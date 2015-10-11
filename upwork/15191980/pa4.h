#ifndef __PA_4_H__
#define __PA_4_H__

#include <string>
#include <fstream>

struct Sales
{
    std::string firstName;
    std::string lastName;
    std::string companyID;
    double totalSales;
    double bonus;
};

struct Product
{
    std::string productNumber;
    double unitPrice;
};

int readProducts(std::ifstream& in, Product* prods, int ps, std::ofstream& err);

int readAndCalSales(std::ifstream& in, Sales* sales, int ss, Product* prods, int ps, std::ofstream& err);

int printSales(Sales* ss, int size);

int openFiles(std::ifstream& salesInput, std::ifstream& prodInput, std::ofstream& err);

int closeFiles(std::ifstream& salesInput, std::ifstream& prodInput, std::ofstream& err);

#endif
