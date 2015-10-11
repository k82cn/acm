#include "ogl.h"

#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>

using namespace std;

namespace ogl
{
    Property::Property(const string& path, const string& env)
    {
        this->read(path, env);
    }

    Property::Property()
    {
    }

    Property::~Property()
    {
    }

    void Property::read(const string& path, const string& env)
    {
        string line;

        std::string pp;

        char* envHome = ::getenv(env.c_str());

        if (envHome)
        {
            pp.append(envHome).append("/").append(path);
        }
        else
        {
            pp = path;
        }

        ifstream propsFile(pp.c_str());

        while( getline(propsFile, line))
        {

            std::size_t comments = line.find('#');
            if (comments != std::string::npos)
            {
                line = line.substr(0, comments);
            }

            ogl::trim(line);

            if (line.empty())
            {
                continue;
            }

            // key/value splitor
            std::size_t kvs = line.find('=');

            if (kvs == std::string::npos)
            {
                continue;
            }

            string key = line.substr(0, kvs);
            ogl::trim(key);
            string val = line.substr(kvs + 1);
            ogl::trim(val);

            m_props[key] = val;
        }
    }

    const char* Property::getCString(const string& key)
    {
        if (m_props.find(key) == m_props.end())
        {
            return 0;
        }

        return m_props[key].c_str();
    }

    std::string Property::get(const string& key)
    {
        assert(m_props.find(key) != m_props.end());
        return m_props[key];
    }

    int Property::getInteger(const string& key)
    {
        assert(m_props.find(key) != m_props.end());
        return atoi(m_props[key].c_str());
    }

};
