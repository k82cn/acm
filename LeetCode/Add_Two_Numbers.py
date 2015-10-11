# Definition for singly-linked list.
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # @return a ListNode
    def addTwoNumbers(self, l1, l2):
        lp1 = l1.next;
        lp2 = l2.next;

        e = int(l1.val + l2.val) / 10;
        n = int(l1.val + l2.val) % 10;

        res = ListNode(n);
        rp = res;

        while (lp1 != None and lp2 != None):
            n = (e + int(lp1.val + lp2.val)) % 10;
            e = (e + int(lp1.val + lp2.val)) / 10;
            rp.next = ListNode(n);
            rp = rp.next;
            lp1 = lp1.next;
            lp2 = lp2.next;

        while (lp1 != None):
            n = (e + int(lp1.val)) % 10;
            e = (e + int(lp1.val)) / 10;
            rp.next = ListNode(n);
            rp = rp.next;
            lp1 = lp1.next;

        while (lp2 != None):
            n = (e + int(lp2.val)) % 10;
            e = (e + int(lp2.val)) / 10;
            rp.next = ListNode(n);
            rp = rp.next;
            lp2 = lp2.next;

        if (e != 0):
            rp.next = ListNode(e);

        return res;

l1 = ListNode(2);
l1.next = ListNode(4);
l1.next.next = ListNode(3);

l2 = ListNode(5);
l2.next = ListNode(6);
l2.next.next = ListNode(4);

l11 = ListNode(1);
l11.next = ListNode(8);

l12 = ListNode(0);


sln = Solution();

res = sln.addTwoNumbers(l1, l2);

rp = res;

while (rp != None):
    print rp.val;
    rp = rp.next;


res = sln.addTwoNumbers(l11, l12);

rp = res;

while (rp != None):
    print rp.val;
    rp = rp.next;
