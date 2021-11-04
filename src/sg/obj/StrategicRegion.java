package sg.obj;

import sg.HubMain;

import java.io.*;

public class StrategicRegion {

    public String content;
    public File file;
    public String provinces;

    public StrategicRegion(File file) {
        this.file = file;
        this.content = HubMain.readFile(file);
        this.provinces = HubMain.getProvinceList(content);

    }
}
