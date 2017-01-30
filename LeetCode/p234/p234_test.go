package p234test

import (
	"testing"
)

/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */

type ListNode struct {
	Val int
	Next *ListNode
}

func isPalindrome(head *ListNode) bool {

	
	
	return false;
}

func TestIsPalindrome(t *testing.T) {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}
	input.Next.Next = &ListNode{Val: 1, Next: nil}

	if !isPalindrome(input) {
		t.Errorf("121 is palindrome.")
	}
}
