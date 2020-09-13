package com.wh.pjtr.ngs.batch.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptTest {

	public static void main(String[] args) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // 암호화 Key값 입력하여 commit 금지(임의의 복호화를 하지 못하도록 하기 위함)
        config.setPassword("whngs");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

		String enc = encryptor.encrypt(""); //암호화 할 내용
		System.out.println("ENC = "+ enc);	//암호화 한 내용을 출력

		//테스트용 복호화
		String des = encryptor.decrypt(enc);
		System.out.println("DES = "+ des);
   }
}
