package p206

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

func ReverseList(head *ListNode) *ListNode {
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
