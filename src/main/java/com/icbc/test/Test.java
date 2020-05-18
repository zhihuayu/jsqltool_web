package com.icbc.test;

import com.alibaba.druid.filter.config.ConfigTools;

public class Test {

	private static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJmV1g+CtAHAK0FuaEd21TsaNz4/mI8Qiz0pDQoOrOlRMHtUDzEFO272EMbxpWQ3/Wsqe//iHJNHYjFe59301yMCAwEAAQ==";
	private static final String PRIVATE_KEY = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAmZXWD4K0AcArQW5oR3bVOxo3Pj+YjxCLPSkNCg6s6VEwe1QPMQU7bvYQxvGlZDf9ayp7/+Ick0diMV7n3fTXIwIDAQABAkAMgAt6G7M26UMcAjZuipd0Rf/1CwwJqNkMU0kCZotgOeElSfQHmnFxOYJmrAG7BjBHxhRgSQ5iEITLeLpQ7tkhAiEAyrCiMNTtc5BTN0Kt9DNXDL8+hFAqX7zW7nxXM/s+2rsCIQDB+uyMyo1pbvTXZW7FfaxGrbpuNszSq0wglP41xgzyuQIhAIc3HnwFEAMwHNOYoANw3Hmce1A5Sotjpt6iBGqhCWVRAiEAnVcQpCpvhYOQaV5G+T20jSfBPkCI5qA3AX6Yq7lXK1ECIGK8d1yW9vDdtOVCa/c2W59uPtydJHzktLtQigw8dzzc";

	public static void main(String[] args) throws Exception {
		// 公钥加密
		String encord = ConfigTools.encrypt(PRIVATE_KEY, "1234dd5sdfsd6");
		System.out.println(encord);
		System.out.println(encord.length());
		// 私钥解密
		String decrypt2 = ConfigTools.decrypt(PUBLIC_KEY, encord);
		System.out.println(decrypt2);
		String encrypt = ConfigTools.encrypt("1234sdf56");
		System.out.println(encrypt);
	}

}
