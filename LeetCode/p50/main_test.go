package main

import (
	"testing"
)

func Test_case_1(t *testing.T) {
	r := myPow(2.0, 3)
	if r != 8 {
		t.Errorf("Expected: 8, Actual: %f", r)
	}
}

func Test_case_2(t *testing.T) {
	r := myPow(2.0, 30)
	if r != 1073741824.0 {
		t.Errorf("Expected: 1073741824, Actual: %f", r)
	}
}

func Test_case_3(t *testing.T) {
	r := myPow(34.00515, -3)
	if r != 0.00003 {
		t.Errorf("Expected: 0.00003, Actual: %f", r)
	}
}
