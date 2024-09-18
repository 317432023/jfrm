package com.soaringloong.jfrm.framework.redis.tool;

/**
 * redis 存储模式
 *
 * @since 2021/9/4 17:52
 */
public enum ModeDict {

	NONE(0, "不包含任何应用前缀"), APP(1, "使用自身应用前缀"), APP_GROUP(2, "使用应用组前缀");

	public final int code;

	public final String name;

	ModeDict(int code, String name) {
		this.code = code;
		this.name = name;
	}

}
