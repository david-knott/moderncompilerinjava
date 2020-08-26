#include <vector>
#include <string>

struct ArithmeticTokenHolder
{
    enum ArithmeticTokenTypes
    {
        ADD = 3,
        INT = 5,
    };
};

class TestINode
{
    public:
    ArithmeticTokenHolder::ArithmeticTokenTypes opcode;
    std::vector<TestINode*> children;
    std::string text;

    public:
    int getArity()
    {
       return children.size();
    }

    TestINode* getNthChild(int idx)
    {
        return children[idx];
    }

    int getOperator()
    {
        return (int)this->opcode;
    }

    std::string getText()
    {
        return this->text;
    }

    std::string toString()
    {
        return "TestINode";
    }
};

#include "AllocatorTest.h"

int main()
{
    TestINode root;
    root.opcode = ArithmeticTokenHolder::ADD;

    TestINode firstChild;
    firstChild.opcode = ArithmeticTokenHolder::INT;
    firstChild.text = "1";

    TestINode secondChild;
    secondChild.opcode = ArithmeticTokenHolder::INT;
    secondChild.text = "2";

    root.children.push_back(&firstChild);
    root.children.push_back(&secondChild);

    AllocatorTest burm;
    burm.burm(&root);
    long result = burm.getlongResult();

    if ( 3 == result )
    {
        std::cerr << "Succeeded: explicit allocator.\n";
    }
    else
    {
        std::cerr << "C++ target FAILED: expected 3, actual " << result << std::endl;
        return 1;
    }
    return 0;
}
