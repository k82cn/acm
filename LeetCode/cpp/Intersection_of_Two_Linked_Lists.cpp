//  Definition for singly-linked list.
struct ListNode
{
    int val;
    ListNode *next;
    ListNode(int x) : val(x), next(NULL) {}
};

class Solution
{
    public:
        ListNode *getIntersectionNode(ListNode *headA, ListNode *headB)
        {
            int aLen = getLengthOfList(headA);
            int bLen = getLengthOfList(headB);

            if (aLen > bLen)
            {
                for (int i = 0; i < aLen - bLen; i++)
                {
                    headA = headA->next;
                }
            }
            else if (aLen < bLen)
            {
                for (int i = 0; i < bLen - aLen; i++)
                {
                    headB = headB->next;
                }
            }

            for (int i = 0; i < min(aLen, bLen); i++)
            {
                if (headA == headB)
                {
                    return headA;
                }

                headA = headA->next;
                headB = headB->next;
            }

            return 0;
        }

    private:
        int getLengthOfList(ListNode* head)
        {
            int res = 0;

            while ( head != 0)
            {
                head = head->next;
                res++;
            }
            return res;
        }
};