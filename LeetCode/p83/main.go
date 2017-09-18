package main

import "fmt"

/**
 * Definition for singly-linked list.
 */
type ListNode struct {
	Val  int
	Next *ListNode
}

func deleteDuplicates(head *ListNode) *ListNode {
	if head == nil {
		return head
	}

	prev := head
	cur := head.Next

	for cur != nil {
		if prev.Val == cur.Val {
			cur = cur.Next
		}
	}

	return head
}

func main() {
}
