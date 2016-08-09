package p206_test

import (
	"fmt"
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

func reverseList(head *ListNode) *ListNode {
	if head == nil || head.Next == nil {
		return head
	}

	pre := head
	cur := head.Next

	pre.Next = nil
	next := cur.Next
	cur.Next = pre

	for next != nil {
		pre = cur
		cur = next
		next = next.Next
		cur.Next = pre
	}

	return cur
}

func printList(head *ListNode) {
	for cur := head; cur != nil; cur = cur.Next {
		fmt.Printf("%d ", cur.Val)
	}
}

func ExampleReverseList_Case1() {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}
	input.Next.Next = &ListNode{Val: 3, Next: nil}

	printList(reverseList(input))
	// Output: 3 2 1
}

func ExampleReverseList_Case2() {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}
	input.Next.Next = &ListNode{Val: 3, Next: nil}
	input.Next.Next.Next = &ListNode{Val: 4, Next: nil}

	printList(reverseList(input))
	// Output: 4 3 2 1
}

func ExampleReverseList_Case3() {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}

	printList(reverseList(input))
	// Output: 2 1
}

func ExampleReverseList_Case4() {
	input := &ListNode{Val: 1, Next: nil}

	printList(reverseList(input))
	// Output: 1
}

