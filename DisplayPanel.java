import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * This class is for HW10 MineSweeper. This class has the main and is the Jpanel of the tiles and buttons.
 * Collaboration Statement: I worked on this homework alone, using only course materials.
 *
 * @author Wataru Ueno
 * @version 1
 */

public class DisplayPanel extends JPanel{
    private Board board;
    private Point point;
    private JButton newgame, hint, highscore;
    private JLabel timelabel;
    private TilePanel tp;
    
    /**
     * This is the main method for this project. Sets up display panel and mine tiles. 
     * @param String[] args takes in argument from commandline
     */
    public static void main(String[] args){
        JFrame frame = new JFrame("MineSweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        Board board = new Board();
        DisplayPanel dp = new DisplayPanel(board);
        
        frame.getContentPane().add(dp);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * This is the constructor of the Display Panel.
     * @param Board board is the instance of the board.
     */
    public DisplayPanel(Board board){
        this.board = board;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tp = new TilePanel();
        add(new MenuButtons());
        add(tp);
    }
    
    /**
     * This is the MenuButtons class and extends JPanel
     * 
     * @author Wataru Ueno
     * @version 1
     */
    private class MenuButtons extends JPanel{
        
        /**
         * This is the constructor for the MenuButtons class. Sets up buttons and buttonlistener.
         */
        public MenuButtons(){
            newgame = new JButton("New Game");
            hint = new JButton("Hint");
            highscore = new JButton("High Score");
            timelabel = new JLabel("Time");
            
            newgame.addActionListener(new BListener());
            hint.addActionListener(new BListener());
            highscore.addActionListener(new BListener());
            
            add(newgame);
            add(hint);
            add(highscore);
            add(timelabel);
        }
    }
    
    /**
     * This is the TilePanel class and extends JPanel. Displays tiles.
     * 
     * @author Wataru Ueno
     * @version 1
     */
    private class TilePanel extends JPanel{
        /**
         * This is the constructor for the TilePanel class.
         */
        public TilePanel(){
            setPreferredSize(new Dimension(600,600));
            board.setDim(getWidth(), getHeight());
            
            addMouseListener(new MListener());
        }   
        /**
         * This is the paintComponent method that gets called when repaint or resizes.
         * 
         * @param Graphics g this is the graphic object to draw on.
         */
        public void paintComponent(Graphics g){
            super.repaint();
            board.setDim(getWidth(), getHeight());
            
            timelabel.setText("Time:" + board.getTime());
            board.updateDraw(g);
        }
    }
    
    /**
     * This class implements the MouseListener and processes clicks.
     * 
     * @author Wataru Ueno
     * @version 1
     */
    private class MListener implements MouseListener{
        /**
         * This method detects point clicked and passes the value to board.update method.
         * @param MouseEvent e the eventsource of the mouseclick
         */
        public void mouseClicked(MouseEvent e){
            String button = "RIGHT";
            if(e.getButton()==e.BUTTON1){
                button = "LEFT";
            }
            
            board.update(e.getPoint(),button);

            repaint();
        }
        /**
         * This method fulfills the implementation of MouseClicker.
         * @param MouseEvent e the eventsource of the mouseclick
         */
        public void mousePressed(MouseEvent e){
        }
        /**
         * This method fulfills the implementation of MouseClicker.
         * @param MouseEvent e the eventsource of the mouseclick
         */
        public void mouseReleased(MouseEvent e){
        }
        /**
         * This method fulfills the implementation of MouseClicker.
         * @param MouseEvent e the eventsource of the mouseclick
         */
        public void mouseEntered(MouseEvent e){
        }
        /**
         * This method fulfills the implementation of MouseClicker.
         * @param MouseEvent e the eventsource of the mouseclick
         */
        public void mouseExited(MouseEvent e){
        }
    }
    
    /**
     * This class implements the ActionListener and processes Button clicks.
     * 
     * @author Wataru Ueno
     * @version 1
     */
    private class BListener implements ActionListener{
        /**
         * This method processes events when the Buttons are clicked
         * @param ActionEvent e the event source that called the ActionListener
         */
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==newgame){
                board.createNewGame();
            }
            if(e.getSource()==hint){
                board.showHint();
            }
            if(e.getSource()==highscore){
                JFrame frame = new JFrame("High Score");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                frame.getContentPane().add(new HighScore());
               
                frame.pack();
                frame.setVisible(true);
            }
        }
    }
    /**
     * This class is the HighScore panel and shows the scores of the tiles.
     * 
     * @author Wataru Ueno
     * @version 1
     */
    private class HighScore extends JPanel{
        private String[] scores = new String[1];
        
        /**
         * This is the Constructor of HighScore class and does set up.
         */
        public HighScore(){
            setPreferredSize(new Dimension(250,400));
            
            try{
               Scanner sc = new Scanner(new File("HighScore.txt"));
               int count = 0;
               int x = 0;
               while(sc.hasNext()){
                    sc.next();
                    count++;
               }
               scores = new String[count];
               sc = new Scanner(new File("HighScore.txt"));
               while(sc.hasNext()){
                     scores[x] = sc.next();
                     x++;
               }
            }catch(FileNotFoundException fnfe){
            }  
        }
        
        /**
         * This is the paintComponent method that gets called when repaint or resizes.
         * 
         * @param Graphics g this is the graphic object to draw on.
         */
        public void paintComponent(Graphics g){
            if(scores.length>2){
                for(int x = 0; x<scores.length ; x+=3){
                    g.drawString((x+3)/3 + ". " + "Layout: " + scores[x+1] + "    Time: " + scores[x+2], 10, 20 + x*5);
                }
            }
            if(scores.length<3){
                 g.drawString("Win a game to view high score.", 10, 20);
            }
        }
    }
}
