package gz.com.alibaba.fastjson.parser;

import gz.com.alibaba.fastjson.parser.ParserConfig;

@Deprecated
public class JavaBeanMapping extends ParserConfig {
	private final static JavaBeanMapping instance = new JavaBeanMapping();

	public static JavaBeanMapping getGlobalInstance() {
		return instance;
	}
}
