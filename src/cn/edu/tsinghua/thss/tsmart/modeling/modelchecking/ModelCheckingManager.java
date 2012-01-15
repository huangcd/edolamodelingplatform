package cn.edu.tsinghua.thss.tsmart.modeling.modelchecking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.Messages;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.utils.ProcessCaller;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class ModelCheckingManager {
    public ModelCheckingProperties mcps;
    CodeGenProjectModel            cgpm;
    String                         fileName;

    public ModelCheckingManager() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();

        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong"); //$NON-NLS-1$
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        cgpm = (CodeGenProjectModel) topModel;

        fileName = cgpm.getMCPropertiesFilePath();

        loadProperties();
    }

    private void loadProperties() {
        if (!checkExistenceProperties()) createProperties();

        Serializer serializer = new Persister();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            mcps = serializer.read(ModelCheckingProperties.class, new File(fileName));
            Thread.currentThread().setContextClassLoader(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveProperties() {
        if (!checkExistenceProperties()) createProperties();

        Serializer serializer = new Persister();
        try {
            serializer.write(mcps, new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        saveProperties();
    }

    private boolean checkExistenceProperties() {
        File file = new File(fileName);
        return file.exists();
    }

    private boolean createProperties() {
        File file = new File(fileName);
        try {
            if (!file.createNewFile()) return false;

            mcps = new ModelCheckingProperties();

            saveProperties();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean checkValid() {
        // TODO 检查



        return true;
    }

    /**
     * 生成test.bip与test.ctl文件到temp目录下
     * 
     * @param isTypeChecking 为真时生成空ctl文件，为假时把properties都输出
     * @return 验证结果
     */
    public void doChecking(boolean isTypeChecking, String modelCheckingParamsString) {
        // 输出对应的BIP文件
        String bipContent = cgpm.getStartupModel().exportAllToBIP();

        // TODO 判断是否能导出，需要改进这个处理
        if (bipContent.equals("")) //$NON-NLS-1$
            return;

        String location = cgpm.getLocation();
        File tempDir = new File(location + "/temp/"); //$NON-NLS-1$
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        try {
            FileWriter fos = new FileWriter(tempDir.getAbsolutePath() + "/test.bip"); //$NON-NLS-1$
            fos.write(bipContent);
            fos.flush();
            fos.close();

            fos = new FileWriter(tempDir.getAbsolutePath() + "/test.ctl"); //$NON-NLS-1$
            if (isTypeChecking) {
                fos.write(""); //$NON-NLS-1$
            } else {
                for (Iterator<ModelCheckingProperty> iterator = mcps.properties.iterator(); iterator
                                .hasNext();) {
                    ModelCheckingProperty property = (ModelCheckingProperty) iterator.next();
                    fos.write("INVARSPEC " + property.property + "\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 调用模型检测工具
            String cmd = System.getProperty("user.dir") //$NON-NLS-1$
                            + "/tool/ModelChecking/BIP.exe"; //$NON-NLS-1$
            if (isTypeChecking) {
                cmd += " TypeCheck "; //$NON-NLS-1$
            } else {
                cmd += " ModelCheck "; //$NON-NLS-1$
            }
            cmd +=
                            "\""            + tempDir.getAbsolutePath() + "/test\"" + " result.txt" + modelCheckingParamsString; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ProcessCaller pc = new ProcessCaller();
            int exitValue = pc.exec(cmd);
            boolean isCheckOK = dumpMessage(pc.getOutputAsString().split("[\\r\\n]+"));//$NON-NLS-1$
            if (exitValue != 0) {
                MessageUtil.addProblemErrorMessage("Error while invoking the model checker: exitValue = "
                                + exitValue);//$NON-NLS-1$
            }
            
            if (isTypeChecking) {
                if (isCheckOK) {
                    MessageUtil.showMessageDialog(Messages.ValidateModelCheckingAction_3, Messages.ValidateModelCheckingAction_4);
                } else {
                    MessageUtil.ShowErrorDialog(Messages.ValidateModelCheckingAction_5, Messages.ValidateModelCheckingAction_6);
                }
            }
            
            return;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

    private boolean dumpMessage(String[] lines) {
        // 准备控制台输出
        MessageConsole console = new MessageConsole("", null);
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] {console});
        console.activate();
        IOConsoleOutputStream normalOut = console.newOutputStream();
        normalOut.setColor(ColorConstants.black);

        IOConsoleOutputStream errorOut = console.newOutputStream();
        errorOut.setColor(ColorConstants.red);

        boolean isCheckOK = false;
        try {
            for (String content : lines) {
                if (content.toLowerCase().contains("error")) { //$NON-NLS-1$
                    errorOut.write(content + "\r\n");//$NON-NLS-1$
                } else {
                    normalOut.write(content + "\r\n"); //$NON-NLS-1$
                }

                if (content.contains("YES")) { //$NON-NLS-1$
                    isCheckOK = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isCheckOK;
    }

}
