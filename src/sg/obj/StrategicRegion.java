package sg.obj;

import sg.ContentScripts;
import sg.FileScripts;
import sg.HubMain;

import java.io.*;

public class StrategicRegion {

    public String content;
    public File file;
    public String provinces;

    public StrategicRegion(File file) {
        this.file = file;
        this.content = FileScripts.readFile(file);
        this.provinces = ContentScripts.getProvincesString(content);

    }
}
