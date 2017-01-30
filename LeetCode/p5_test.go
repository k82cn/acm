package leetcode

import (
	"testing"
)

func TestP5(t *testing.T) {

	tests := []struct {
		expected, input string
	}{
		{
			expected:"bb",
			input:"cbbd",
		},
		{
			expected:"bab",
			input:"babad",
		},
		{
			expected:"a",
			input:"a",
		},
	}

	for _, test := range tests {
		got := longestPalindrome(test.input)
		if got != test.expected {
			t.Errorf("expected %v, got %v", test.expected, got)
		}
	}
}
