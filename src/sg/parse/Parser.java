package sg.parse;

import sg.ParsingScripts;

import java.io.*;
import java.util.ArrayList;

public class Parser {

    public static String cleanFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            String output = "";

            while((line = reader.readLine()) != null) {
                line = ParsingScripts.beforeWord(line, "#");
                output += line;
                output += " ";
            }

            output = output.replace("\r", " ");
            output = output.replace("\t", " ");
            output = output.replace("\n", " ");

            while(output.contains("  ")) {
                output.replace("  ", " ");
            }

            return output;

        } catch(FileNotFoundException f) {
            System.out.println(file.getName() + " not found.");
        } catch(IOException e) {
            System.out.println(file.getName() + " yielded an IO exception.");
        }
        return "";
    }

    public Block parseFile(File file) {
        String content = cleanFile(file);
        return (Block) parseElement(content);
    }

    public Element parseElement(String content) {

       String current = "";
       for(char i : content.toCharArray()) {
           if(i == '{' || i == '}' || i == '=') {
                splitUp.add(current);
                current = "";
                splitUp.add(Character.toString(i));
           }
           if(i == ' ') {
               splitUp.add(current);
               current = "";
           }666
       }

       for(String thing : splitUp) {

       }

    }
}
