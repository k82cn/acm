package main

import (
	"testing"
)

func Test_flagstone_case1(t *testing.T) {
	res := flagstone(6, 6, 4)
	if res != 4 {
		t.Errorf("expected 4, but %d\n", res)
	}
}

func Test_flagstone_case2(t *testing.T) {
	res := flagstone(1000000000, 1000000000, 1)
	if res != 1000000000000000000 {
		t.Errorf("expected 1000000000000000000, but %d\n", res)
	}
}
