#ifndef __OGL_PROPERTY_H__
#define __OGL_PROPERTY_H__

#include <map>
#include <string>

namespace ogl
{
    class Property
    {
        public:
            Property(const std::string& path, const std::string& env);

            Property();

            ~Property();

            void read(const std::string& path, const std::string& env);

            std::string get(const std::string& key);

            const char* getCString(const std::string& key);

            int getInteger(const std::string& key);

        private:
            std::map<std::string, std::string> m_props;
    };
}

#endif

