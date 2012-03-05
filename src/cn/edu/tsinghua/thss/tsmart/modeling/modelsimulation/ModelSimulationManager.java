package cn.edu.tsinghua.thss.tsmart.modeling.modelsimulation;

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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingProperties;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingProperty;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class ModelSimulationManager {
	public ModelCheckingProperties mcps;
    CodeGenProjectModel            cgpm;
    String                         fileName;

    @SuppressWarnings("rawtypes")
	public ModelSimulationManager() {
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
        // TODO ���
        return true;
    }
    
    /**
     * ����test.bip��test.ctl�ļ���tempĿ¼��
     * @param isTypeChecking Ϊ��ʱ���ɿ�ctl�ļ���Ϊ��ʱ��properties�����
     * @return ��֤���
     */
    public boolean doSimulation(boolean isTypeChecking, String modelCheckingParamsString) {
        // �����Ӧ��BIP�ļ�
        String bipContent = cgpm.getStartupModel().exportAllToBIP();
        
        // TODO �ж��Ƿ��ܵ�������Ҫ�Ľ��������
        if (bipContent.equals("")) //$NON-NLS-1$
            return false;
        
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
            }
            else {
                for (Iterator<ModelCheckingProperty> iterator = mcps.properties.iterator(); iterator.hasNext();) {
                    ModelCheckingProperty property = (ModelCheckingProperty) iterator.next();
                    fos.write("INVARSPEC " + property.property + "\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // ׼������̨���
        MessageConsole console = new MessageConsole(Messages.ModelSimulationManager_8, null);  
        ConsolePlugin.getDefault().getConsoleManager()
                        .addConsoles(new IConsole[] {console});
        try {
            console.activate();
            IOConsoleOutputStream normalOut = console.newOutputStream();
            normalOut.setColor(ColorConstants.black);
            
            IOConsoleOutputStream errorOut = console.newOutputStream();
            errorOut.setColor(ColorConstants.red);
            
            // ����ģ�ͼ�⹤��
            String cmd = "java -jar " + "\"" + System.getProperty("user.dir") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + "/tool/ModelChecking/bipSimulator.jar" + "\" "; //$NON-NLS-1$ //$NON-NLS-2$
//            cmd += "\"" + System.getProperty("user.dir")
//                    + "/file/dpu.bip" + "\"";
            cmd += "\"" + tempDir.getAbsolutePath() + "/test.bip\""; //$NON-NLS-1$ //$NON-NLS-2$
            File dir = new File(System.getProperty("user.dir") + "/tool/ModelChecking"); //$NON-NLS-1$ //$NON-NLS-2$
            Runtime.getRuntime().exec(cmd, null, dir);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return false;
    }

}
