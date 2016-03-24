package pl.edu.agh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {

    String str = null;

    public String read(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();
            while (line != null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            br.close();
        }

        return str;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
