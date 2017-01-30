package leetcode

import "testing"

func TestP7(t *testing.T) {
	tests := []struct{
		input, expected int
	} {
		{
			input:10,
			expected: 1,
		},
		{
			input:1,
			expected:1,
		},
		{
			input:-123,
			expected:-321,
		},
		{
			input:1534236469,
			expected:0,
		},
		{
			input: 1463847412,
			expected:2147483641,
		},
	}

	for _, test := range tests {
		got := reverse(test.input)
		if got != test.expected {
			t.Errorf("expected %d, got %d", test.expected, got)
		}
	}
}
