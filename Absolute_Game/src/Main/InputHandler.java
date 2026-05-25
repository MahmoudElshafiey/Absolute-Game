package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    public boolean upPressed, downPressed,leftPressed,rightPressed;
    @Override
    public void keyTyped(KeyEvent e) {
        //never used but has to be implemented
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code==KeyEvent.VK_W|| code==KeyEvent.VK_UP){
            //if the user pressed W
            upPressed=true;
        }
        if (code==KeyEvent.VK_A|| code==KeyEvent.VK_LEFT){
            //if the user pressed A
            leftPressed=true;
        }
        if (code==KeyEvent.VK_S|| code==KeyEvent.VK_DOWN){
            //if the user pressed S
            downPressed=true;
        }
        if (code==KeyEvent.VK_D || code==KeyEvent.VK_RIGHT){
            //if the user pressed D
            rightPressed=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code==KeyEvent.VK_W|| code==KeyEvent.VK_UP){
            //When the user stops pressing W
            upPressed=false;
        }
        if (code==KeyEvent.VK_A|| code==KeyEvent.VK_LEFT){
            //When the user stops pressing A
            leftPressed=false;
        }
        if (code==KeyEvent.VK_S|| code==KeyEvent.VK_DOWN){
            //When the user stops pressing S
            downPressed=false;
        }
        if (code==KeyEvent.VK_D|| code==KeyEvent.VK_RIGHT){
            //When the user stops pressing D
            rightPressed=false;
        }
    }
}
