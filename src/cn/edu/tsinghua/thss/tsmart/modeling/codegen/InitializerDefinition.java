package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class InitializerDefinition {
	/**
	 * modelVar:
	 * 		- 存在：即 modelVar != null 且在模型中可以找到
	 * 		- 不存在：即 modelVar == null 或者在模型中未找到
	 * 初始化方法：
	 * 		1. modelVar存在则初始化之
	 * 		2. modelVar不存在且defaultValue存在则初始化为defaultValue
	 * 		3. modelVar不存在且defaultValue==null，则初始化为XXX
	 * @author aleck
	 *
	 */
	public static class InitializeItem {
		public InitializeItem() {
		}
		
		public InitializeItem(String devProperty, String modelVar, String defaultValue) {
			this.devProperty = devProperty;
			this.modelVar = modelVar;
			this.defaultValue = defaultValue;
		}
		
		@Attribute
		public String devProperty;
		@Attribute(required = false)
		public String modelVar;
		@Attribute(required = false)
		public String defaultValue;
	}
	
	public static class DeviceInitializer {
		public DeviceInitializer() {
			this.items = new ArrayList<InitializeItem>();
		}
		
		public DeviceInitializer(String devType) {
			this();
			this.devType = devType;
		}
		
		@Attribute
		public String devType;
		@ElementList
		public List<InitializeItem> items;
		
		public void addItem(InitializeItem item) {
			items.add(item);
		}
	}

	public InitializerDefinition() {
		initializers = new ArrayList<DeviceInitializer>();
	}
	
	@ElementList
	public List<DeviceInitializer> initializers;
	
	private void addInitializer(DeviceInitializer devInitializer) {
		initializers.add(devInitializer);
	}

	public static void main(String[] args) {
		testWrite();
		// textRead();
	}

	private static void textRead() {
		InitializerDefinition id = new InitializerDefinition();
		Serializer serializer = new Persister();
		try {
			serializer.read(id, new File("./docs/CodeGen/device-initializer.xml"));
			System.out.println("Read OK!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testWrite() {
		InitializerDefinition id = new InitializerDefinition();
		
		DeviceInitializer buttonInitializer = new DeviceInitializer("PushButton");
		buttonInitializer.addItem(new InitializeItem("pushed", null, "0"));
		id.addInitializer(buttonInitializer);
		
		DeviceInitializer fixedSpeedMotorInitializer = new DeviceInitializer("FixedSpeedMotor");
		fixedSpeedMotorInitializer.addItem(new InitializeItem("ticktime", null, "100"));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("fixedSpeed", "SPEED_CONSTANT", null));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("direction", "Dir", null));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("position", "INIT_POS", null));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("hardDownLimit", "HARD_DOWN_LMT", null));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("softDownLimit", "SOFT_DOWN_LMT", null));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("softUpLimit", "SOFT_UP_LMT", null));
		fixedSpeedMotorInitializer.addItem(new InitializeItem("hardUpLimit", "HARD_UP_LMT", null));
		id.addInitializer(fixedSpeedMotorInitializer);
		
		DeviceInitializer variantSpeedMotorInitializer = new DeviceInitializer("VariantSpeedMotor");
		variantSpeedMotorInitializer.addItem(new InitializeItem("ticktime", null, "100"));
		variantSpeedMotorInitializer.addItem(new InitializeItem("accWork", "ACC_WORK", null));
		variantSpeedMotorInitializer.addItem(new InitializeItem("accStop", "ACC_STOP", null));
		variantSpeedMotorInitializer.addItem(new InitializeItem("position", "INIT_POS", null));
		variantSpeedMotorInitializer.addItem(new InitializeItem("targetSpeed", null, "0"));
		variantSpeedMotorInitializer.addItem(new InitializeItem("hardDownLimit", "HARD_DOWN_LMT", null));
		variantSpeedMotorInitializer.addItem(new InitializeItem("softDownLimit", "SOFT_DOWN_LMT", null));
		variantSpeedMotorInitializer.addItem(new InitializeItem("softUpLimit", "SOFT_UP_LMT", null));
		variantSpeedMotorInitializer.addItem(new InitializeItem("hardUpLimit", "HARD_UP_LMT", null));
		id.addInitializer(variantSpeedMotorInitializer);
		
		Serializer serializer = new Persister();
		try {
			serializer.write(id, new File("./docs/CodeGen/device-initializer.xml"));
			System.out.println("Write OK!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
