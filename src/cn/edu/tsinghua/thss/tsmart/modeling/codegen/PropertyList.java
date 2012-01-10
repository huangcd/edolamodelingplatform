package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.simpleframework.xml.ElementList;

public class PropertyList {
	@ElementList
	public List<PropertyEntry> properties = new ArrayList<PropertyEntry>();

	private Properties props;
	
	public void addProperty(String key, String value) {
		properties.add(new PropertyEntry(key, value));
	}
	
	public Properties getProperties() {
		Properties props = new Properties();
		for (PropertyEntry pe : properties) {
			props.put(pe.property, pe.value);
		}
		return props;
	}
	
	public void ensureProps() {
		if (props == null) {
			props = getProperties();
		}
	}
	
	public boolean getBool(String key) {
		ensureProps();
		String value = props.getProperty(key);
		try {
			return Integer.parseInt(value) == 1;
		} catch (Exception e) {
			// in 'true' or 'false' format
			return Boolean.parseBoolean(value);
		}
	}

	public byte getByte(String key) {
		ensureProps();
		return Byte.parseByte(props.getProperty(key));
	}

	public int getInt(String key) {
		ensureProps();
		return Integer.parseInt(props.getProperty(key));
	}

	public float getReal(String key) {
		ensureProps();
		return Float.parseFloat(props.getProperty(key));
	}

	public boolean getBool(String key, boolean def) {
		if (containsKey(key)) {
			return getBool(key);
		} else {
			return def;
		}
	}

	public byte getByte(String key, byte def) {
		if (containsKey(key)) {
			return getByte(key);
		} else {
			return def;
		}
	}

	public int getInt(String key, int def) {
		if (containsKey(key)) {
			return getInt(key);
		} else {
			return def;
		}
	}

	public float getReal(String key, float def) {
		if (containsKey(key)) {
			return getInt(key);
		} else {
			return def;
		}
	}

	private boolean containsKey(String key) {
		ensureProps();
		return props.containsKey(key);
	}
}
