package sg;

import java.io.*;

public class Files {

    public static File[] getDirectoryFiles(String folder) {
        File dir = new File(folder);
        File[] foundFiles = dir.listFiles();
        return foundFiles;
    }

    public static File findFileFromStart(String folder, String start) {
        File dir = new File(folder);
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(start);
            }
        });
        return foundFiles[0];
    }

    public static File findFileFromStart(String folder, int start) {
        return findFileFromStart(folder, Integer.toString(start));
    }

    public static File getFile(String loc) {
        return new File(loc);
    }

    public static String readFile(String loc) {
        return readFile(getFile(loc));
    }

    public static String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            try {
                while((line = reader.readLine()) != null) {
                    if(line.contains("#")) {
                        line = Parsing.beforeWord(line, "#");
                    }
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch(FileNotFoundException f) {
            System.out.println(file.getName() + " not found.");
        } catch(IOException e) {
            System.out.println(file.getName() + " yielded an IO exception.");
        }
        return "";
    }

}
