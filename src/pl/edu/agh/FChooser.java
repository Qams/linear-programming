package pl.edu.agh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FChooser extends JPanel implements ActionListener{

    static private final String newline = "\n";
    JButton openButton;
    JTextArea log;
    JFileChooser fc;
    Solver solver;

    public FChooser() {
        super(new BorderLayout());

        log = new JTextArea(5, 20);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();

        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FChooser.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getPath() + "." + newline);
                solver = new Solver(new ProblemData(file.getPath()));
                if(solver.solve()) {
                    log.append("SOLUTION: [");
                    for (int i = 0; i < solver.getSolution().length; i++) {
                        log.append(solver.getSolution()[i] + " ");
                    }
                    log.append("]\n");
                    log.append("OPTIMAL COST: " + solver.getDestValue() + "\n");
                }
                else{
                    log.append("Cannot solve this problem\n");
                }

            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

        }
    }
}
