package pl.edu.agh;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        /*String str = null;
        BufferedReader br = new BufferedReader(new FileReader("output.json"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }

        System.out.println(str);


        JSONObject obj = new JSONObject(str);

        int count = obj.getJSONObject("dest").getInt("x1");
        JSONArray arr = obj.getJSONArray("cond");
        for (int i = 0; i < arr.length(); i++)
        {
            String post_id = arr.getJSONObject(i).getString("x1");
        }
        int c = obj.getInt("count");
        System.out.println(c);
        PrintWriter pw = new PrintWriter("third.json");
        pw.println("{");
        pw.println("    \"count\": " + arr.length() + ",");
        pw.println("    \"cond\":[");

        for(int i=1;i<=c;i++)
        {
            pw.print("      { ");
            for(int j=1;j<=arr.length();j++)
            {
                String val = arr.getJSONObject(j-1).getString("x"+i);
                pw.print("\"x" + j + "\": \"" + val + "\", ");

            }
            pw.print("\"s\": \"" + obj.getJSONObject("sign").getString("x"+i) + "\", ");
            pw.print("\"v\": \"" + obj.getJSONObject("dest").getString("x"+i) + "\"");
            pw.println("},");

        }
        pw.println("    ],");
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

        pw.print("      \"sign\":{");
        for(int i=1;i<=arr.length();i++)
        {
            pw.print("\"x"+i +"\": \"" + arr.getJSONObject(i-1).getString("s") + "\", ");
        }
        pw.println("},");
        pw.print("}");
        pw.close();*/

        ProblemParser problemParser = new ProblemParser("pp.json", "output.json");
        problemParser.generateJSONDualProblem();



    }
}
