import unittest;
import sys;

class Solution:
    # @return an integer
    def reverse(self, x):
        neg = False;
        if (x < 0):
            x = -x;
            neg = True;
        x_tmp = int(str(x)[::-1]);
        if (x_tmp > 2147483647) :
            return 0;
        return -x_tmp if neg else x_tmp;


# Testing
class Test_Solution(unittest.TestCase):
    def setUp(self):
        self.sln = Solution();
        
    def test_reverse(self):
        self.assertEqual(self.sln.reverse(123), 321);
        self.assertEqual(self.sln.reverse(-123), -321);
        
if __name__ == "__main__":
    unittest.main();
    