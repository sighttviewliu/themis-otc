package com.oxchains.themis.common.geetest;

import javax.swing.text.StyledEditorKit.BoldAction;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	/**
	 * 填入自己的captcha_id和private_key
	 */
	private static final String GEETEST_ID = "472def0349b037596b02cd1067c76624";
	private static final String GEETEST_KEY = "e642041a75a50adc638c684c8b2134c7";
	private static final boolean NEWFAILBACK = true;

	public static final String getGeetestId() {
		return GEETEST_ID;
	}

	public static final String getGeetestKey() {
		return GEETEST_KEY;
	}
	
	public static final boolean isnewfailback() {
		return NEWFAILBACK;
	}

}
