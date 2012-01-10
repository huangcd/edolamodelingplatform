package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;

public class CreateWizardData {
    private String                    name;
    private String                    location;
    private String                    baseline;
    private List<String>              libs;
    private Map<String, LibraryModel> libraryMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public List<String> getLibs() {
        return libs;
    }

    public void setLibs(List<String> libs) {
        this.libs = libs;
    }

    public void setLibraryMap(Map<String, LibraryModel> libraryMap) {
        this.libraryMap = libraryMap;
    }

    public List<LibraryModel> getLibraries() {
        List<LibraryModel> list = new ArrayList<LibraryModel>();
        for (String libName : libs) {
            LibraryModel model = libraryMap.get(libName);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }
}
