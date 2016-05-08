package main

import (
	"testing"
)

func Test_case1(t *testing.T) {
	res := watermelon(2)
	if res != "NO" {
		t.Errorf("expected NO, but %s\n", res)
	}
}

func Test_case2(t *testing.T) {
	res := watermelon(6)
	if res != "YES" {
		t.Errorf("expected YES, but %s\n", res)
	}
}

func Test_case3(t *testing.T) {
	res := watermelon(1)
	if res != "NO" {
		t.Errorf("expected NO, but %s\n", res)
	}
}
