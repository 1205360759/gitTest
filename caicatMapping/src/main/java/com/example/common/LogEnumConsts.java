package com.example.common;

public class LogEnumConsts {

	/**
	 * @since 日志操作说明的枚举
	 */
	public enum logType {
		add("添加", "1", "add"), 
		edit("修改", "2", "edit"),
		del("删除", "3", "del");
		private String name;
		private String value;
		private String code;

		private logType(String name, String value, String code) {
			this.name = name;
			this.value = value;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String getCode() {
			return code;
		}
	}

}
