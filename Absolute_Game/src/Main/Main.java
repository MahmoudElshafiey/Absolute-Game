package Main;
import javax.swing.*;

/* SuperRunner
* A Game made by Mahmoud Osama Mahmoud Solyman Mahmoud Elshafiey as the Term Project for
* Object Oriented Programming Course
* */
public class Main {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome To Absolute Game\n \n Absolute Game'e " +
                "hoşgeldiniz");
        JOptionPane.showMessageDialog(null, "The game's objective is to get as many " +
                "points as possible\n \n Oyunun amacı olabildiğince puan almaktır");
        JOptionPane.showMessageDialog(null, "As you get more points your speed will start to increase\n \n " +
                "Puan aldıkça hızınız artar");
        JOptionPane.showMessageDialog(null, "But that just means you will have to be more careful as the game goes on \n \n" +
                "Bu yüzden oyun ilerledikçe daha dikkatli oynamanız gerekcek");
        JOptionPane.showMessageDialog(null, "Have fun!\n \n İyi eğlenceler! ");
        JFrame window = new  JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Absolute Game");

        GameFrame frame=new GameFrame();
        window.add(frame);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        frame.setupGame();
        frame.startThread();
    }
}