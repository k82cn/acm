package leetcode

func longestPalindrome(s string) string {
	var maxStr string
	var maxLen int

	isPalindromic := func(s string) bool {
		for i := 0; i < len(s) / 2 ; i++ {
			if s[i] != s[len(s) - i - 1] {
				return false
			}
		}

		return true
	}

	for i := 0; i < len(s); i++ {
		for j := i; j <= len(s); j++ {
			if isPalindromic(s[i:j]) {
				if j - i > maxLen {
					maxStr = s[i:j]
					maxLen = j - i
				}
			}
		}
	}

	return maxStr
}
