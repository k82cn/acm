package p9test

import (
	"testing"
)

func isPalindrome(x int) bool {
	
	return false;
}

func TestIsPalindrome_Case1(t *testing.T) {
	if !isPalindrome(11) {
		t.Errorf("11 is palindrome.");
	}
}

func TestIsPalindrome_Case2(t *testing.T) {
	if isPalindrome(12) {
		t.Errorf("12 is not palindrome.");
	}
}
