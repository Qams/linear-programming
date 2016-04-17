package pl.edu.agh;
import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,300);
        frame.add(new FChooser());

        frame.setVisible(true);
    }
}
