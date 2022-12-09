

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class OpenAI {

    public static void main(String[] args){
        executeOpenAIRequest("test");
    }
    // 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
    public static String executeOpenAIRequest(String request) {
        StringBuilder totalOutput = new StringBuilder();

       ProcessBuilder processBuilder = new ProcessBuilder("python3", System.getProperty("user.dir")+"/src/openAI.py",request);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String lines;
            while((lines = reader.readLine())!= null){
                totalOutput.append(lines+"\n");
            }
            while((lines = errorReader.readLine())!= null){
                System.out.println("error "+lines);
            }
            System.out.println(totalOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return totalOutput.toString();

    }
}
