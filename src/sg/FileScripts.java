package sg;

import java.io.*;
import java.nio.file.Files;
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

    /**
     * If a file already exists at loc, returns it.
     * Otherwise, creates an empty file there and returns it.
     */
    public static File createFile(String loc) {
        File file = new File(loc);
        if(!file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }

        if(file.isFile()) {
            return file;
        } else {
            try {
                file.createNewFile();
            } catch (IOException io) {
                System.out.println("Can't create file at " + loc);
            }
        }
        return file;

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
            while((line = reader.readLine()) != null) {
                if(line.contains("#")) {
                    line = ParsingScripts.beforeWord(line, "#");
                }
                line = line.replace("\r", "");
                if(line != "") {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
            }
            return stringBuilder.toString();

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
        try (InputStream is = Files.newInputStream(source.toPath()); OutputStream os = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
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
    public static void replace(File file, String oldLine, String newLine) {
        try {
            Scanner sc = new Scanner(file);
            StringBuilder buffer = new StringBuilder();
            while (sc.hasNextLine()) {
                buffer.append(sc.nextLine()).append(System.lineSeparator());
            }
            String fileContents = buffer.toString();
            sc.close();
            fileContents = fileContents.replaceAll(oldLine, newLine);
            FileWriter writer = new FileWriter(file);
            writer.append(fileContents);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error replacing in file.");
        }
    }

}
