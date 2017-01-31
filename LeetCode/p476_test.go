package leetcode

import "testing"

func TestP476(t *testing.T) {
	tests:=[]struct{input, expected int} {
		{
			input:5,
			expected:2,
		},
		{
			input:1,
			expected:0,
		},
	}

	for _, test := range tests {
		got := findComplement(test.input)
		if got != test.expected {
			t.Errorf("expected %d, got %d", test.expected, got)
		}
	}
}
