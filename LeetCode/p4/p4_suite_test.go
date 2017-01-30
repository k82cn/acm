package main

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"

	"testing"
    "fmt"
)

func TestP4(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "P4 Suite")
}

func ExampleFindMedianSortedArraysCase1() {
	nums1 := []int{1, 3}
	nums2 := []int{2, 4}

	fmt.Print(findMedianSortedArrays(nums1, nums2))
	// Output: 2.5
}

func ExampleFindMedianSortedArraysCase2() {
	nums1 := []int{1, 3}
	nums2 := []int{2}

	fmt.Print(findMedianSortedArrays(nums1, nums2))
	// Output: 2
}

func ExampleFindMedianSortedArraysCase3() {
	nums1 := []int{2, 2}
	nums2 := []int{2, 2}

	fmt.Print(findMedianSortedArrays(nums1, nums2))
	// Output: 2
}

func ExampleFindMedianSortedArraysCase4() {
	nums1 := []int{2, 2}
	nums2 := []int{2}

	fmt.Print(findMedianSortedArrays(nums1, nums2))
	// Output: 2
}

