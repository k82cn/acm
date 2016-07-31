/**
 * Definition for singly-linked list.
 */

package p21test

import (
	"fmt"
)

type ListNode struct {
	Val  int
	Next *ListNode
}

func mergeTwoLists(l1 *ListNode, l2 *ListNode) *ListNode {
	if l1 == nil {
		return l2
	}

	if l2 == nil {
		return l1
	}

	var resultHead *ListNode
	var resultCur *ListNode

	c1 := l1
	c2 := l2

	if c1.Val < c2.Val {
		resultHead = c1
		resultCur = c1
		c1 = c1.Next
	} else if c1.Val == c2.Val {
		resultHead = c1
		resultCur = c1
		c1 = c1.Next

		resultCur.Next = c2
		resultCur = resultCur.Next
		c2 = c2.Next
	} else {
		resultHead = c2
		resultCur = c2
		c2 = c2.Next
	}

	for {
		// If one of item is empty, break the loop.
		if c1 == nil || c2 == nil {
			break
		}

		if c1.Val < c2.Val {
			resultCur.Next = c1
			resultCur = resultCur.Next
			c1 = c1.Next
		} else if c1.Val == c2.Val {
			resultCur.Next = c1
			resultCur = resultCur.Next
			c1 = c1.Next

			resultCur.Next = c2
			resultCur = resultCur.Next
			c2 = c2.Next
		} else {
			resultCur.Next = c2
			resultCur = resultCur.Next
			c2 = c2.Next
		}
	}

	if c1 == nil {
		resultCur.Next = c2
	} else if c2 == nil {
		resultCur.Next = c1
	}

	return resultHead
}

func ExampleMergeTwoLists() {
	l1 := &ListNode{}

	c1 := l1
	c1.Val = 1

	c1.Next = &ListNode{}
	c1 = c1.Next
	c1.Val = 3

	c1.Next = &ListNode{}
	c1 = c1.Next
	c1.Val = 5
	c1.Next = nil

	l2 := &ListNode{}

	c2 := l2
	c2.Val = 2

	c2.Next = &ListNode{}
	c2 = c2.Next
	c2.Val = 4

	c2.Next = &ListNode{}
	c2 = c2.Next
	c2.Val = 6
	c2.Next = nil

	// Check the result.
	result := mergeTwoLists(l1, l2)
	for cur := result; cur != nil; cur = cur.Next {
		fmt.Printf("%d ", cur.Val);
	}
	// Output: 1 2 3 4 5 6
}
