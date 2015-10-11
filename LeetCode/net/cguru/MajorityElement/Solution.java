package net.cguru.MajorityElement;

import java.util.HashMap;
import java.util.Map.Entry;

public class Solution {

	public int majorityElement(int[] num) {
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		
		if (num == null)
		{
			throw new IllegalArgumentException();
		}
		
		for (int i : num)
		{
			if (m.containsKey(i))
			{
				m.put(i, m.get(i) + 1);
			}
			else
			{
				m.put(i, 1);
			}
		}
		
		for (Entry<Integer, Integer> e : m.entrySet())
		{
			if (e.getValue() > num.length/2)
			{
				return e.getKey();
			}
		}
		
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Solution s = new Solution();
		System.out.println(s.majorityElement(new int[]{1}));
	}

}
