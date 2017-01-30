package leetcode

import "testing"

func TestP495(t *testing.T) {
	tests := []struct {
		name       string
		timeSeries []int
		duration   int
		expected   int
	}{
		{
			name:"1",
			timeSeries: []int{1, 2},
			duration: 2,
			expected: 3,
		},
		{
			name:"2",
			timeSeries: []int{1, 4},
			duration:2,
			expected:4,
		},
	}

	for _, test := range tests {
		got := findPosisonedDuration(test.timeSeries, test.duration)

		if got != test.expected {
			t.Errorf("%v: expected %v, got %v", test.name, test.expected, got)
		}
	}
}
