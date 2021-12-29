package sg;

import java.io.*;
import java.util.Scanner;

public class FileScripts {

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
                        line = ParsingScripts.beforeWord(line, "#");
                        line = line.replace("\r", "");
                    }
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
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

    /**
     * Copy contents from one existing file to another.
     */
    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * Create a copy of a file in a new directory.
     */
    public static File clone(File orig, String newDirectory) {
        File newFile = new File(newDirectory + "/" + orig.getName());
        try {
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
            copyFileUsingStream(orig, newFile);
        } catch (IOException e) {
            System.out.println("Clone failed");
        }
        return newFile;
    }

    /**
     * Replace in a file one string with another.
     */
    public static void replace(File file, String oldLine, String newLine) throws IOException {
        Scanner sc = new Scanner(file);
        StringBuffer buffer = new StringBuffer();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()+System.lineSeparator());
        }
        String fileContents = buffer.toString();
        sc.close();
        fileContents = fileContents.replaceAll(oldLine, newLine);
        FileWriter writer = new FileWriter(file);
        writer.append(fileContents);
        writer.flush();
    }



}
