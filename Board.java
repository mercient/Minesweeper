import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.*;


/**
 * This is the Board class and takes care of Tiles and creating new games.
 * 
 * @author Wataru Ueno
 * @version 1
 */
public class Board{
    private Tile[][] tiles;
    private int xsize, ysize, width, height, mines;
    private int time;
    private boolean firstclick, gamestarted,gameover;
    private Timer timer; 
    private String[] scores;
    
    /**
     * This is the constructor of the Board class. Sets up important things.
     */
    public Board(){
        xsize = 1;
        ysize = 1;
        scores = new String[1];
        gamestarted = false;
        gameover = true;
        timer = new Timer(100, new TimeListener());
    }
    
    /**
     * This method creates new board layout.
     */
    public void createNewGame(){
        gamestarted = false;
        int tempsize = 0;
        String tempstring = "";
        do{
            try{
                tempstring = JOptionPane.showInputDialog("Width of the Tile?");
                if(tempstring!=null && !tempstring.equals("")){
                    tempsize = Integer.parseInt(tempstring);
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Choose appropriate size (>1).");
                tempsize=0;
            }
            if(tempstring==null || tempstring.equals("")){
                return;
            }
        }while(tempsize<2);
       
        xsize = tempsize;
        
        do{
            try{
                tempstring = JOptionPane.showInputDialog("Height of the Tile?");
                if(tempstring!=null && !tempstring.equals("")){
                    tempsize = Integer.parseInt(tempstring);
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Choose appropriate size (>1).");
                tempsize=0;
            }
            if(tempstring==null || tempstring.equals("")){
                return;
            }
        }while(tempsize<2);
        ysize = tempsize;
        
        do{
            try{
                tempstring = JOptionPane.showInputDialog("Number of mines?");
                if(tempstring!=null && !tempstring.equals("")){
                    tempsize = Integer.parseInt(tempstring);
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Choose appropriate size (>0).");
                tempsize=0;
            }
            if(tempstring==null || tempstring.equals("")){
                return;
            }
        }while(tempsize<1);
        mines = tempsize;
        
        createBoard(xsize, ysize);
        time = 0;
        firstclick = true;
        gamestarted = true;
        gameover = false;
    }
    
    /**
     * This method checks the current state of the game and updates appropriately.
     * @param Point point is the pixel position of the click
     * @param String button determines if right click or left click
     */
    public void update(Point point, String button){
        if(gameover){
            return;
        }
        int row = point.x / width;
        int col = point.y / height;
        
        if(row >= xsize || col >= ysize){
            return;
        }
        
        if(firstclick && button.equals("LEFT")){
            createMines(row, col);
            timer.start();
            firstclick = false;
        }
        
        Tile tile = getTile(row, col);
        
        if(button.equals("LEFT") && !tile.isFlagged()){
            int mines = tile.clickTile();
                  
            if(mines==0){
                openEmpty(tile);
            }
            
            if(tile.hasMine()){
                timer.stop();
                for(Tile[] ts : tiles){
                    for(Tile t : ts){
                        if(t.hasMine()){
                            t.clickTile();
                        }
                    }
                }
                gameover = true;
                if(JOptionPane.showConfirmDialog(null, "You clicked on a mine..GAME OVER! New Game?")==0){
                    createNewGame();
                }
            }
        }
        if(button.equals("RIGHT")){
            if(!tile.isClicked()){
                tile.flag();
            }
        }
        
        int opened = 1;
        for(Tile[] ts : tiles){
            for(Tile t : ts){
                opened *= t.update();
            }
        }
        
        if(opened>0){
            timer.stop();
            gameover = true;
            sortHighScore();
            
            if(JOptionPane.showConfirmDialog(null, "You won the game!! New Game?")==0){
                createNewGame();
            }
        }
    }
    
    /**
     * This is the draw method that gets called when paintComponent gets called in DisplayPanel
     * 
     * @param Graphics g this is the graphic object to draw on.
     */
    public void updateDraw(Graphics g){
        if(gamestarted){
            for(Tile[] ts : tiles){
                for(Tile t : ts){
                    t.draw(g, width, height);
                }
            }
        }
        else{
            g.drawString("Click New Game or click High Score", 10, 20);
        }
    }
    
    /**
     * This method sets pixel dimensions for the Tiles and the board layout. 
     * @param int width the pixel width of the panel
     * @param int height the pixel height of the panel
     */
    public void setDim(int width, int height){
        if(width/xsize < height/ysize){
            this.width = width/xsize;
            this.height = width/xsize;
        }
        else{
            this.width = height/ysize;
            this.height = height/ysize;
        }
    }
    
    /**
     * This method returns the tile that is at row x and column y.
     * @param int x the row index
     * @param int y the column index
     * @return Tile tile is the tile at the index. Returns if index unexpected. 
     */
    public Tile getTile(int x, int y){
        if(x>=xsize || y>=ysize || x<0 || y<0){
            return null;
        }
        else{
            return tiles[x][y];
        }
    }
    
    /**
     * This method creates the Tile layout
     * @param int row the row size for the layout
     * @param int col the column size for the layout 
     */
    public void createBoard(int row, int col){
        tiles = new Tile[row][col];
        for(int x = 0; x < row ; x++){
            for(int y = 0 ; y < col ; y++){
                tiles[x][y] = new Tile(this, x, y);
            }
        }
    }
    
    /**
     * This method creates mines after the user clicks
     * @param int row the row index of the tile the user clicked
     * @param int col the column index of the tile the user clicked
     */
    public void createMines(int row, int col){
        Random r = new Random();
        boolean mineset = false;
        int minenum = 0;
        while(minenum<mines){
            for(int x = 0; x < xsize; x++){
                for(int y = 0 ; y < ysize ; y++){
                    if( !(x==row && y==col) && r.nextInt(100)>84 && mines>minenum && !tiles[x][y].hasMine()){
                        tiles[x][y].setMine();
                        minenum++;
                    }
                }
            }
        }
        minenum = 0;
    }
    
    /**
     * This method opens all the tiles that should open if an empty tile is clicked.
     * @param Tile tile the Empty tile to expand from 
     */
    public void openEmpty(Tile tile){
        ArrayList<Tile> tilesToOpen = MinesweeperUtils.expandTile(tile, tiles);
        
        for(Tile t : tilesToOpen){
            t.clickTile();
        }
    }
    
    /**
     * This method signals the user where the mine is. 
     */
    public void showHint(){
        if(firstclick){
            JOptionPane.showMessageDialog(null, "Click somewhere first to show hint!");
        }
        if(!gameover && gamestarted){
            for(int x = 0; x< xsize ; x++){
                for(int y = 0 ; y< ysize ; y++){
                    if(tiles[x][y].hasMine() && !tiles[x][y].isFlagged() && !tiles[x][y].isHinted()){
                        tiles[x][y].hinted();
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * This method gets the Time of the current running game.
     * @return double the time 
     */
    public double getTime(){
        return time/10.0;
    }
    
    /**
     * This method sorts the new high score to the appropriate place. 
     */
    public void sortHighScore(){
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
            try{
                boolean sorted = false;
                PrintWriter outFile = new PrintWriter("HighScore.txt");
                for(int x = 0; x<scores.length; x+=3){
                    if(xsize * ysize > Integer.parseInt(scores[x]) && !sorted){
                       outFile.println(xsize*ysize + " " + xsize + "x" + ysize + " " + time/10.0);
                       sorted = true;
                    }
                    if(xsize * ysize == Integer.parseInt(scores[x]) && !sorted){
                        if(time/10.0 < Double.parseDouble(scores[x+2])){
                           outFile.println(xsize*ysize + " " + xsize + "x" + ysize + " " + time/10.0);
                           sorted = true;
                       }
                    }
                    outFile.println(scores[x] + " "  + scores[x+1] + " " + scores[x+2]);
                }
                if(!sorted){
                     outFile.println(xsize*ysize + " " + xsize + "x" + ysize + " " + time/10.0);
                }
                
                outFile.close();
            }catch(Exception exc){
            }
        }
    
    /**
     * This class is the ActionListener for the Timer
     * 
     * @author Wataru Ueno
     * @version 1
     */
    private class TimeListener implements ActionListener{
        /**
         * This is the method that gets called when event occurs and increments time by 0.1 second 
         * 
         * @param ActionEvent e the event source that created the event
         */
        public void actionPerformed(ActionEvent e){
            time+=1;
        }
    }
}