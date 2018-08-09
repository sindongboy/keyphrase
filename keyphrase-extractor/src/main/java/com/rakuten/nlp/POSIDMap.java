package com.rakuten.nlp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Donghun Shin | donghun.shin@rakuten.com | RIT | Rakuten Inc.
 * @since 2017/03/29
 */
final public class POSIDMap {
	private static final Map<String, Integer> POSID_MAP = new HashMap<String, Integer>();
	static {
		POSID_MAP.put("その他,間投,*,*", 0);
		POSID_MAP.put("フィラー,*,*,*", 1);
		POSID_MAP.put("感動詞,*,*,*", 2);
		POSID_MAP.put("記号,アルファベット,*,*", 3);
		POSID_MAP.put("記号,一般,*,*", 4);
		POSID_MAP.put("記号,括弧開,*,*", 5);
		POSID_MAP.put("記号,括弧閉,*,*", 6);
		POSID_MAP.put("記号,句点,*,*", 7);
		POSID_MAP.put("記号,空白,*,*", 8);
		POSID_MAP.put("記号,読点,*,*", 9);
		POSID_MAP.put("形容詞,自立,*,*", 10);
		POSID_MAP.put("形容詞,接尾,*,*", 11);
		POSID_MAP.put("形容詞,非自立,*,*", 12);
		POSID_MAP.put("助詞,格助詞,一般,*", 13);
		POSID_MAP.put("助詞,格助詞,引用,*", 14);
		POSID_MAP.put("助詞,格助詞,連語,*", 15);
		POSID_MAP.put("助詞,係助詞,*,*", 16);
		POSID_MAP.put("助詞,終助詞,*,*", 17);
		POSID_MAP.put("助詞,接続助詞,*,*", 18);
		POSID_MAP.put("助詞,特殊,*,*", 19);
		POSID_MAP.put("助詞,副詞化,*,*", 20);
		POSID_MAP.put("助詞,副助詞,*,*", 21);
		POSID_MAP.put("助詞,副助詞／並立助詞／終助詞,*,*", 22);
		POSID_MAP.put("助詞,並立助詞,*,*", 23);
		POSID_MAP.put("助詞,連体化,*,*", 24);
		POSID_MAP.put("助動詞,*,*,*", 25);
		POSID_MAP.put("接続詞,*,*,*", 26);
		POSID_MAP.put("接頭詞,形容詞接続,*,*", 27);
		POSID_MAP.put("接頭詞,数接続,*,*", 28);
		POSID_MAP.put("接頭詞,動詞接続,*,*", 29);
		POSID_MAP.put("接頭詞,名詞接続,*,*", 30);
		POSID_MAP.put("動詞,自立,*,*", 31);
		POSID_MAP.put("動詞,接尾,*,*", 32);
		POSID_MAP.put("動詞,非自立,*,*", 33);
		POSID_MAP.put("副詞,一般,*,*", 34);
		POSID_MAP.put("副詞,助詞類接続,*,*", 35);
		POSID_MAP.put("名詞,サ変接続,*,*", 36);
		POSID_MAP.put("名詞,ナイ形容詞語幹,*,*", 37);
		POSID_MAP.put("名詞,一般,*,*", 38);
		POSID_MAP.put("名詞,引用文字列,*,*", 39);
		POSID_MAP.put("名詞,形容動詞語幹,*,*", 40);
		POSID_MAP.put("名詞,固有名詞,一般,*", 41);
		POSID_MAP.put("名詞,固有名詞,人名,一般", 42);
		POSID_MAP.put("名詞,固有名詞,人名,姓", 43);
		POSID_MAP.put("名詞,固有名詞,人名,名", 44);
		POSID_MAP.put("名詞,固有名詞,組織,*", 45);
		POSID_MAP.put("名詞,固有名詞,地域,一般", 46);
		POSID_MAP.put("名詞,固有名詞,地域,国", 47);
		POSID_MAP.put("名詞,数,*,*", 48);
		POSID_MAP.put("名詞,接続詞的,*,*", 49);
		POSID_MAP.put("名詞,接尾,サ変接続,*", 50);
		POSID_MAP.put("名詞,接尾,一般,*", 51);
		POSID_MAP.put("名詞,接尾,形容動詞語幹,*", 52);
		POSID_MAP.put("名詞,接尾,助数詞,*", 53);
		POSID_MAP.put("名詞,接尾,助動詞語幹,*", 54);
		POSID_MAP.put("名詞,接尾,人名,*", 55);
		POSID_MAP.put("名詞,接尾,地域,*", 56);
		POSID_MAP.put("名詞,接尾,特殊,*", 57);
		POSID_MAP.put("名詞,接尾,副詞可能,*", 58);
		POSID_MAP.put("名詞,代名詞,一般,*", 59);
		POSID_MAP.put("名詞,代名詞,縮約,*", 60);
		POSID_MAP.put("名詞,動詞非自立的,*,*", 61);
		POSID_MAP.put("名詞,特殊,助動詞語幹,*", 62);
		POSID_MAP.put("名詞,非自立,一般,*", 63);
		POSID_MAP.put("名詞,非自立,形容動詞語幹,*", 64);
		POSID_MAP.put("名詞,非自立,助動詞語幹,*", 65);
		POSID_MAP.put("名詞,非自立,副詞可能,*", 66);
		POSID_MAP.put("名詞,副詞可能,*,*", 67);
		POSID_MAP.put("連体詞,*,*,*", 68);
	}

	public static int getPOSID(String feature) {
		if (POSID_MAP.containsKey(feature)) {
			return POSID_MAP.get(feature);
		} else {
			return 0;
		}
	}

}
