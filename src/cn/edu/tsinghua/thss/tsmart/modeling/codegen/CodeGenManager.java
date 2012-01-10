package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.io.File;
import java.io.IOException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class CodeGenManager {

    public DeviceDefinition dd;
    public Mapping          m;
    private String          ddFileName;
    private String          mFileName;

    public CodeGenManager(String dd, String m) {
        loadDeviceDefinition(dd);
        loadMapping(m);
        ddFileName = dd;
        mFileName = m;
    }

    private void loadDeviceDefinition(String fileName) {
        if (!checkExistenceDeviceDefinition(fileName)) createDeviceDefinition(fileName);

        Serializer serializer = new Persister();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            dd = serializer.read(DeviceDefinition.class, new File(fileName));
            Thread.currentThread().setContextClassLoader(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMapping(String fileName) {
        if (!checkExistenceMapping(fileName)) createMapping(fileName);

        Serializer serializer = new Persister();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            m = serializer.read(Mapping.class, new File(fileName));
            Thread.currentThread().setContextClassLoader(cl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDeviceDefinition(String fileName) {
        if (!checkExistenceDeviceDefinition(fileName)) createDeviceDefinition(fileName);

        Serializer serializer = new Persister();
        try {
            serializer.write(dd, new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveMapping(String fileName) {
        if (!checkExistenceMapping(fileName)) createMapping(fileName);

        Serializer serializer = new Persister();
        try {
            serializer.write(m, new File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        saveDeviceDefinition(ddFileName);
        saveMapping(mFileName);
    }

    private boolean checkExistenceDeviceDefinition(String dd) {
        File file = new File(dd);
        return file.exists();
    }

    private boolean checkExistenceMapping(String m) {
        File file = new File(m);
        return file.exists();
    }

    private boolean createDeviceDefinition(String ddFileName) {
        File file = new File(ddFileName);
        try {
            if(!file.createNewFile())
                return false;
            
            dd = new DeviceDefinition();
            
            saveDeviceDefinition(ddFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    private boolean createMapping(String mFileName) {
        File file = new File(mFileName);
        try {
            if(!file.createNewFile())
                return false;
            
            m = new Mapping();
            
            saveMapping(mFileName);
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public boolean checkValid() {
        // TODO ≤ª”√ºÏ≤È

        return true;
    }

}
