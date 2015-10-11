class Solution:
    # @param version1, a string
    # @param version2, a string
    # @return an integer
	def compareVersion(self, version1, version2):
		va = version1.split(".");
		vb = version2.split(".");

		v1l = len(va);
		v2l = len(vb);

		l = min (v1l, v2l);

		for i in range(l):
			if (int(va[i]) > int(vb[i])):
				return 1;

			if (int (va[i]) < int(vb[i])):
				return -1;

		for i in vb[l:]:
			if (int (i) != 0):
				return -1;
		for i in va[l:]:
			if (int (i) != 0):
				return 1;
		return 0;


sln = Solution();

print sln.compareVersion("1.0", "1");
print sln.compareVersion("1.1","1");
print sln.compareVersion("1","1.1");
print sln.compareVersion("0.1", "0.0.1");


