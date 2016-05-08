package main

import (
	"fmt"
	"testing"
)

func Test_flagstone_case1(t *testing.T) {
	res := flagstone(6, 6, 4)
	if res != 4 {
		t.Error("expected 4, but %d\n", res)
	}
}

func Test_flagstone_case2(t *testing.T) {
	res := flagstone(1000000000, 1000000000, 1)
	if res != 1000000000000000000 {
		t.Error(fmt.Printf("expected 1000000000000000000, but %d\n", res))
	}
}
