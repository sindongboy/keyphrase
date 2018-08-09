package com.skplanet.nlp.keyphrase.driver;

import java.util.Arrays;

/**
 * Find Longest Repetitive Sequence
 * @author Donghun Shin / donghun.shin@sk.com
 * @since 9/6/16
 */
public class LRS {
	// return the longest common prefix of s and t
	public static String lcp(String s, String t) {
		int n = Math.min(s.length(), t.length());
		for (int i = 0; i < n; i++) {
			if (s.charAt(i) != t.charAt(i))
				return s.substring(0, i);
		}
		return s.substring(0, n);
	}


	// return the longest repeated string in s
	public static String find(String s) {

		// form the N suffixes
		int n  = s.length();
		String[] suffixes = new String[n];
		for (int i = 0; i < n; i++) {
			suffixes[i] = s.substring(i, n);
		}

		// sort them
		Arrays.sort(suffixes);

		// find longest repeated substring by comparing adjacent sorted suffixes
		String lrs = "";
		for (int i = 0; i < n-1; i++) {
			String x = lcp(suffixes[i], suffixes[i+1]);
			if (x.length() > lrs.length())
				lrs = x;
		}
		return lrs;
	}

	public static void main(String[] args) {
		String s = "히아루론 히아루론 히아루론 히아루론 히아루론";
		System.out.println(LRS.find(s));
	}
}
