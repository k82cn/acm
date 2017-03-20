package main

import (
	"testing"
)

func TestMyPower(t *testing.T) {
	tests := []struct {
		x float64
		n int
		expected float64
	}{
		{
			x: 2.0,
			n: 3,
			expected: 8,
		},
		{
			x: 2.0,
			n: 30,
			expected: 1073741824.0,
		},
		//{
		//	x:	34.00515,
		//	n: -3,
		//	expected:0.000025,
		//},
		{
			x: 0.00001,
			n: 2147483647,
			expected: 0,
		},
	}

	for i, test := range tests {
		r := myPow(test.x, test.n)
		if r != test.expected {
			t.Errorf("Case %d: Expected: %f, Actual: %f", i, test.expected, r)
		}
	}
}
