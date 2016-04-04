package pl.edu.agh;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class ProblemParser {

    private final String path;
    private final String out;
    private JSONObject obj;
    private JSONArray arr;
    private final JSONReader reader;


    public ProblemParser(String path, String out)
    {
        this.path = path;
        this.out = out;
        reader = new JSONReader();
        try {
            obj = new JSONObject(reader.read(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        arr = obj.getJSONArray("cond");
    }

    public void generateJSONDualProblem()
    {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(out);
            generateCount(pw);
            generateCond(pw);
            generateDest(pw);
            generateSign(pw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (pw != null) {
            pw.close();
        }
    }

    private void generateCount(PrintWriter pw)
    {
        int count = obj.getJSONObject("dest").getInt("x1");
        pw.println("{");
        pw.println("    \"count\": " + arr.length() + ",");
    }

    private void generateCond(PrintWriter pw)
    {
        pw.println("    \"cond\":[");
        for(int i=1;i<=obj.getInt("count");i++)
        {
            pw.print("      { ");
            for(int j=1;j<=arr.length();j++)
            {
                String val = arr.getJSONObject(j-1).getString("x"+i);
                pw.print("\"x" + j + "\": \"" + val + "\", ");

            }
            if(obj.getJSONObject("dest").get("val").equals("min"))
            {
                String strToWrite = null;
                if(obj.getJSONObject("sign").getString("x"+i).equals(">="))
                    strToWrite = "<=";
                else if(obj.getJSONObject("sign").getString("x"+i).equals("<="))
                    strToWrite = ">=";
                else
                    strToWrite = "=";
                pw.print("\"s\": \"" + strToWrite + "\", ");
            }
            else{
                pw.print("\"s\": \"" + obj.getJSONObject("sign").getString("x"+i) + "\", ");
            }
            pw.print("\"v\": \"" + obj.getJSONObject("dest").getString("x"+i) + "\"}");
            if(i < obj.getInt("count"))
                pw.println(",");
            else pw.println();
        }
        pw.println("    ],");
    }

    private void generateDest(PrintWriter pw)
    {
        pw.print("      \"dest\":{");
        for(int i=1;i<=arr.length();i++)
        {
            pw.print("\"x"+i +"\": \"" + arr.getJSONObject(i-1).getString("v") + "\", ");
        }
        if(obj.getJSONObject("dest").getString("val").equals("max"))
            pw.print("\"val\": \"min\"");
        else
            pw.print("\"val\": \"max\"");
        pw.println("},");
    }

    private void generateSign(PrintWriter pw)
    {
        pw.print("      \"sign\":{");
        for(int i=1;i<=arr.length();i++)
        {
            if(obj.getJSONObject("dest").get("val").equals("max")) {
                String strToWrite = null;
                if (arr.getJSONObject(i - 1).getString("s").equals("<=")) {
                    strToWrite = ">=";
                } else if (arr.getJSONObject(i - 1).getString("s").equals(">=")) {
                    strToWrite = "<=";
                } else {
                    strToWrite = "=";
                }
                pw.print("\"x" + i + "\": \"" + strToWrite + "\"");
            }
            else
                pw.print("\"x"+i +"\": \"" + arr.getJSONObject(i-1).getString("s") + "\"");
            if(i<arr.length())
                pw.print(",");
        }
        pw.println("}");
        pw.print("}");
    }
}
