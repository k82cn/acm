package leetcode

import "strings"

func lengthOfLastWord(s string) int {
	ss := strings.Split(s, " ")

	if len(ss) == 0 {
		return 0
	}

	for i := len(ss) - 1; i >= 0; i-- {
		t := strings.Trim(ss[i], " ")
		if len(t) == 0 {
			continue
		}
		return len(t)
	}

	return 0
}
