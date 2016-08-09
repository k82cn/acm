package p206

import (
	"fmt"
)

func printList(head *ListNode) {
	for cur := head; cur != nil; cur = cur.Next {
		fmt.Printf("%d ", cur.Val)
	}
}

func ExampleReverseList_Case1() {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}
	input.Next.Next = &ListNode{Val: 3, Next: nil}

	printList(ReverseList(input))
	// Output: 3 2 1
}

func ExampleReverseList_Case2() {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}
	input.Next.Next = &ListNode{Val: 3, Next: nil}
	input.Next.Next.Next = &ListNode{Val: 4, Next: nil}

	printList(ReverseList(input))
	// Output: 4 3 2 1
}

func ExampleReverseList_Case3() {
	input := &ListNode{Val: 1, Next: nil}
	input.Next = &ListNode{Val: 2, Next: nil}

	printList(ReverseList(input))
	// Output: 2 1
}

func ExampleReverseList_Case4() {
	input := &ListNode{Val: 1, Next: nil}

	printList(ReverseList(input))
	// Output: 1
}
