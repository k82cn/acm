package leetcode

import (
	"testing"
	"reflect"
)

func TestP501(t *testing.T) {
	tests := []struct {
		input    *TreeNode
		expected []int
	}{
		{
			input: &TreeNode{
				Val:1,
				Right:&TreeNode{
					Val:2,
					Left:&TreeNode{
						Val:2,
					},
				},
			},
			expected: []int{2},
		},
	}

	for _, test := range tests {
		got := findMode(test.input)
		if !reflect.DeepEqual(got, test.expected) {
			t.Errorf("expected %v, got %v", test.expected, got)
		}
	}
}