package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.utils.ProcessCaller;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class CodeGenManager {

    public DeviceDefinition     dd;
    public Mapping              m;

    private String              ddFileName;
    private String              mFileName;
    private CodeGenProjectModel cgpm;
    private String              codeGenPath;
    private String              platForm                             = "plc";
    private int                 tickTime                             = 100;
    private int                 optimization                         = 1;

    private static final String CODE_GEN_BIP_FILE_NAME               = "codegen.edola";
    private static final String CODE_GEN_CONFIG_FILE_NAME            = "config.ini";
    private static final String CODE_GEN_SIMULATION_CONFIG_FILE_NAME = "config_sim.ini";

    public CodeGenManager() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();

        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        cgpm = (CodeGenProjectModel) topModel;

        ddFileName = cgpm.getDeviceDefinitionFilePath();
        mFileName = cgpm.getMappingFilePath();
        codeGenPath = this.cgpm.getLocation() + "/codegen/";

        loadDeviceDefinition();
        loadMapping();
    }

    public void setPlatForm(String p) {
        this.platForm = p;
    }

    public void setTickTime(int t) {
        this.tickTime = t;
    }

    public void setOptimization(int o) {
        this.optimization = o;
    }

    private void loadDeviceDefinition() {
        if (!checkExistenceDeviceDefinition()) createDeviceDefinition();

        Serializer serializer = new Persister();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            dd = serializer.read(DeviceDefinition.class, new File(ddFileName));
            Thread.currentThread().setContextClassLoader(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMapping() {
        if (!checkExistenceMapping()) createMapping();

        Serializer serializer = new Persister();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            m = serializer.read(Mapping.class, new File(mFileName));
            Thread.currentThread().setContextClassLoader(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDeviceDefinition() {
        if (!checkExistenceDeviceDefinition()) createDeviceDefinition();

        Serializer serializer = new Persister();
        try {
            serializer.write(dd, new File(ddFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveMapping() {
        if (!checkExistenceMapping()) createMapping();

        Serializer serializer = new Persister();
        try {
            serializer.write(m, new File(mFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        saveDeviceDefinition();
        saveMapping();
    }

    private boolean checkExistenceDeviceDefinition() {
        File file = new File(ddFileName);
        return file.exists();
    }

    private boolean checkExistenceMapping() {
        File file = new File(mFileName);
        return file.exists();
    }

    private boolean createDeviceDefinition() {
        File file = new File(ddFileName);
        try {
            if (!file.createNewFile()) return false;

            dd = new DeviceDefinition();

            saveDeviceDefinition();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean createMapping() {
        File file = new File(mFileName);
        try {
            if (!file.createNewFile()) return false;

            m = new Mapping();

            saveMapping();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean generateDirAndFiles() {
        File codeGenPathMD = new File(codeGenPath);
        if (!codeGenPathMD.exists()) {
            codeGenPathMD.mkdirs();
        }

        if (!cgpm.getStartupModel().checkCodeGenValid() || !cgpm.getStartupModel().validateFull())
            return false;

        String bipContext = cgpm.getStartupModel().exportAllToBIPforCodeGen();
        if (bipContext.equals(""))
            return false;

        FileWriter writer;
        try {
            writer = new FileWriter(codeGenPath + CODE_GEN_BIP_FILE_NAME);
            writer.write(bipContext);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Properties properties = new Properties();
        properties.setProperty("model", codeGenPath + CODE_GEN_BIP_FILE_NAME);
        properties.setProperty("mapping", mFileName);
        properties.setProperty("output", codeGenPath);
        properties.setProperty("platform", platForm);
        properties.setProperty("ticktime", String.format("%dms", tickTime));
        properties.setProperty("tick_conn_name", "_"
                        + cgpm.getStartupModel().getAllTickConnectorName());
        properties.setProperty("optimize", String.format("%d", optimization));

        try {
            properties.store(new FileOutputStream(codeGenPath + CODE_GEN_CONFIG_FILE_NAME), "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        properties = new Properties();
        properties.setProperty("input_size", "100");
        properties.setProperty("output_size", "100");
        // NOTE: use /plc-program.exe, TEMPORARILY!
        // Logically, the TSMART has to decide and tell codegen the name.
        // then the codegen can compile the program to this required name.
        properties.setProperty("program", codeGenPath + "/plc-program.exe");
        properties.setProperty("devices", ddFileName);
        properties.setProperty("mapping", mFileName);

        try {
            properties.store(new FileOutputStream(codeGenPath
                            + CODE_GEN_SIMULATION_CONFIG_FILE_NAME), "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean invokeCodeGenerator(String mode) {
        MessageUtil.clearProblemMessage();
        ProcessCaller pc = new ProcessCaller();
        String codegenJarFilePath =
                        System.getProperty("user.dir") + "/tool/CodeGeneration/codegen.jar";
        File home = new File(codeGenPath);
        String[] command =
                        new String[] {"java", "-jar", codegenJarFilePath, "--mode", mode,
                                        "--config", getCodegenConfigFileFullPath(),};
        try {
            int exitValue = pc.exec(command, null, home);
            // output the messages
            dumpMessage(pc.getOutputAsString().split("[\\r\\n]+"));
            if (exitValue != 0) {
                MessageUtil.addProblemErrorMessage("Error while invoking the code generator: exitValue = "
                                + exitValue);
            }
        } catch (IOException e) {
            MessageUtil.addProblemErrorMessage("Error while invoking the code generator: "
                            + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    public boolean checkValid() {
        if (!generateDirAndFiles()) return false;
        return invokeCodeGenerator("check");
    }

    public boolean runCodeGen() {
        if (!generateDirAndFiles()) return false;
        return invokeCodeGenerator("generate");
    }

    /**
     * 将信息输出到控制台和问题列表
     * 
     * @param split
     */
    private void dumpMessage(String[] lines) {
        // >>(INFO|WARNING|ERROR)#(\d+)
        Pattern header = Pattern.compile(">>(INFO|WARNING|ERROR)#(\\d+)");
        MessageConsole console = new MessageConsole("代码生成工具输出", null);
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {console});
        // the last WARNING or ERROR
        StringBuffer sb = new StringBuffer();
        String lastMessageType = null;
        try {
            console.activate();
            IOConsoleOutputStream out = console.newOutputStream(); // 获得Console的输出流
            out.setFontStyle(SWT.BOLD); // 设置字体
            out.setColor(ColorConstants.black);
            for (String line : lines) {
                Matcher m = header.matcher(line);
                if (m.matches()) {
                    // output the message
                    if (sb.length() > 0) {
                        String msg = sb.toString();
                        if (lastMessageType.equals("WARNING")) {
                            MessageUtil.addProblemWarningMessage(msg);
                        } else if (lastMessageType.equals("ERROR")) {
                            MessageUtil.addProblemErrorMessage(msg);
                        }
                    }
                    String type = m.group(1);
                    if (type.equals("INFO")) {
                        lastMessageType = type;
                    } else if (type.equals("WARNING")) {
                        lastMessageType = type;
                        sb.setLength(0);
                    } else if (type.equals("ERROR")) {
                        lastMessageType = type;
                        sb.setLength(0);
                    }
                } else {
                    sb.append(line);
                    sb.append(" | ");
                }
                out.write(line);
                out.write('\n');
            }
            // the last message
            if (sb.length() > 0) {
                String msg = sb.toString();
                if (lastMessageType.equals("WARNING")) {
                    MessageUtil.addProblemWarningMessage(msg);
                } else if (lastMessageType.equals("ERROR")) {
                    MessageUtil.addProblemErrorMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean runCodeSimulation() {
        new Thread() {
            public void run() {
                ProcessCaller pc = new ProcessCaller(true, true);
                String virtualEnvJarFilePath =
                                System.getProperty("user.dir")
                                                + "/tool/CodeGeneration/plc-virtual-env.jar";
                File home = new File(codeGenPath);
                String[] command =
                                new String[] {"java", "-jar", virtualEnvJarFilePath,
                                                getSimulationConfigFileFullPath(),};
                try {
                    pc.exec(command, null, home);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }

    private String getCodegenConfigFileFullPath() {
        return codeGenPath + CODE_GEN_CONFIG_FILE_NAME;
    }

    private String getSimulationConfigFileFullPath() {
        return codeGenPath + CODE_GEN_SIMULATION_CONFIG_FILE_NAME;
    }

}
