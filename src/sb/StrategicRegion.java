package sb;

import java.io.*;

public class StrategicRegion {

    public String content;
    public File file;
    public String provinces;

    public StrategicRegion(File file) {
        this.file = file;
        this.content = Scripts.readFile(file);
        this.provinces = Scripts.getProvinceList(content);

    }
}
