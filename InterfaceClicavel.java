import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InterfaceClicavel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Clickable GUI Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JButton button = new JButton("Click Me!");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Button Clicked!");
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(button);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}