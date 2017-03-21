package main

import "fmt"

func hammingDistance(x int, y int) int {
	dist := 0
	val := x ^ y
	for {
		if val == 0 {
			break
		}
		dist++
		val &= val - 1
	}
	return dist
}

func main() {
	fmt.Printf("%d\n", hammingDistance(1, 4))
}
