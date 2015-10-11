#ifndef __OGL_HASH_H__
#define __OGL_HASH_H__

#include <map>
#include <string>

namespace ogl
{

    class ConsistentHashNet
    {
        public:
            void addNode(const std::string& nodeName);
            void removeNode(const std::string& nodeName);
            std::string getNode(const std::string& key);

        private:
            long hash(char* digest, int nTime);
            void computeMd5(const std::string& key, char* digest);

            std::map<long, std::string> m_nodes;
    };
}

#endif

