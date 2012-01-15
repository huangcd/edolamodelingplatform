package cn.edu.tsinghua.thss.tsmart.platform.language;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "cn.edu.tsinghua.thss.tsmart.platform.language.messages"; //$NON-NLS-1$
	public static String SwitchChineseAction_0;
	public static String SwitchChineseAction_1;
	public static String SwitchEnglishAction_0;
	public static String SwitchEnglishAction_1;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
