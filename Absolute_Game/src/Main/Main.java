package Main;
import javax.swing.*;

/* SuperRunner
 * A Game made by Mahmoud Osama Mahmoud Solyman Mahmoud Elshafiey as the Term Project for
 * Object Oriented Programming Course
 * */
public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Absolute Game");

        GameFrame frame = new GameFrame();
        window.add(frame);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        frame.setupGame();
        frame.startThread();
    }
}