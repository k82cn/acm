package leetcode

import "testing"

func TestP9 (t *testing.T) {
	tests :=[]struct{
		name string
		input int
		expected bool
	} {
		{
			name:"1",
			input:1,
			expected:true,
		},
	}

	for _, test := range tests {
		got := isPalindrome(test.input)
		if got != test.expected {
			t.Errorf("%s: expected %t, got %t", test.name, test.expected, got)
		}
	}

}
