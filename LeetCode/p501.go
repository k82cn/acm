package leetcode

//Definition for a binary tree node.
type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func findMode(root *TreeNode) []int {
	visited := make(map[int]int)
	dup := []int{}

	if root == nil {
		return nil
	}

	cache := []*TreeNode{root}

	for {
		if len(cache) == 0 {
			break
		}

		cur := cache[0]
		cache = cache[1:]

		visited[cur.Val] += 1

		if cur.Left != nil {
			cache = append(cache, cur.Left)
		}
		if cur.Right != nil {
			cache = append(cache, cur.Right)
		}
	}

	for k, v := range visited {
		if v > 1 {
			dup = append(dup, k)
		}
	}

	return dup
}
