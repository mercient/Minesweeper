import java.awt.*;
import javax.swing.ImageIcon;

/**
 * This class implements MineTile and it takes care of individual mine operations. Also holds images. 
 * 
 * @author Wataru Ueno
 * @version 1
 */
public class Tile implements MineTile {
    private Point position;
    private boolean hasmine, clicked, flagged, hinted;
    private Board board;
    private int mines;
    private Image mineimage, hiddenimage, emptyimage, flagimage, hintimage;
    
    /**
     * This is the constructor for the Tile class.
     * 
     * @param Board board the reference to the Board.
     * @param int x the row position of the tile
     * @param int y the column position of the tile
     */
    public Tile(Board board, int x, int y){
        this.board = board;
        clicked = false;
        flagged = false;
        position = new Point(x,y);
        
        mineimage = (new ImageIcon("mine.png")).getImage();
        hiddenimage = (new ImageIcon("hidden.png")).getImage();
        emptyimage = (new ImageIcon("empty.png")).getImage();
        flagimage = (new ImageIcon("flag.png")).getImage();
        hintimage = (new ImageIcon("hint.png")).getImage();
    }
    
    /**
     * This method returns the row index of the tile.
     * @return int the row index
     */
    public int getRow(){
        return position.x;
    }
    
    /**
     * This method returns the column index of the tile.
     * @return int the column index
     */
    public int getCol(){
        return position.y;
    }
    
    /**
     * This method gets the number of adjacent mines
     * 
     * @return int the number of mines in the neighboring 8 tiles
     */
    public int getAdjacent(){
        mines = 0;
        if(hasmine){
            mines+=10;
        }
        if(board.getTile(position.x-1, position.y-1)!=null && board.getTile(position.x-1, position.y-1).hasMine()){
            mines++;
        }
        if(board.getTile(position.x-1, position.y)!=null && board.getTile(position.x-1, position.y).hasMine()){
            mines++;
        }
        if(board.getTile(position.x-1, position.y+1)!=null && board.getTile(position.x-1, position.y+1).hasMine()){
            mines++;
        }
        if(board.getTile(position.x, position.y-1)!=null && board.getTile(position.x, position.y-1).hasMine()){
            mines++;
        }
        if(board.getTile(position.x, position.y+1)!=null && board.getTile(position.x, position.y+1).hasMine()){
            mines++;
        }
        if(board.getTile(position.x + 1, position.y-1)!=null && board.getTile(position.x +1, position.y-1).hasMine()){
            mines++;
        }
        if(board.getTile(position.x+1, position.y)!=null && board.getTile(position.x + 1, position.y).hasMine()){
            mines++;
        }
        if(board.getTile(position.x+1, position.y+1)!=null && board.getTile(position.x + 1, position.y+1).hasMine()){
            mines++;
        }
        return mines;
    }
    /**
     * This method sets mine in the tile
     */
    public void setMine(){
        hasmine = true;
    }
    /**
     * This method returns if tile has mine or not
     * @return boolean tile has mine or not
     */
    public boolean hasMine(){
        return hasmine;
    }
    
    /**
     * This method clicks the tile. Sets click to true and returns mines.
     * @return int the number of mines. 
     */
    public int clickTile(){
        clicked = true;
        return mines;
    }
    
    /**
     * This method checks if the tile is already clicked.
     * @return boolean clicked or not.
     */
    public boolean isClicked(){
        return clicked;
    }
    
    /**
     * This method flags or unflags the tile.
     */
    public void flag(){
        flagged = !flagged;
    }
    
    /**
     * This method returns if its flagged or not.
     * @return boolean flagged or not.
     */
    public boolean isFlagged(){
        return flagged;
    }
    
    /**
     * This method turns on the hint.
     */
    public void hinted(){
        hinted = true;
    }
    
    /**
     * This method returns if its hinted or not.
     * @return boolean hinted or not.
     */
    public boolean isHinted(){
        return hinted;
    }
    
    /**
     * This method returns if the tile has mines, clicked, or neither.
     * @return int returns meaningful values so the caller knows if the tile needs to be clicked or not.
     */
    public int update(){
        if(clicked && !hasmine){
            return 1;
        }
        if(!clicked && hasmine){
            return 2;
        }
        return 0;
    }
   
    /**
     * This method draws the tiles. Gets called when paintComponent is called in DisplayPanel
     * 
     * @param Graphics g the graphics object to draw on.
     * @param int width the size of the width of the tile
     * @param int height the size of the height of the tile
     */
    public void draw(Graphics g, int width, int height){
        if(clicked){
            if(hasmine){
                g.drawImage(mineimage, position.x * width, position.y * height, width, height, null);
            }
            else{
                if(mines==0){
                    g.drawImage(emptyimage, position.x * width, position.y * height, width, height, null);
                }
                else{
                    Image numberimage;
                    if(mines==1){
                        numberimage = (new ImageIcon("1.png")).getImage();
                    }
                    else if(mines==2){
                        numberimage = (new ImageIcon("2.png")).getImage();
                    }
                    else if(mines==3){
                        numberimage = (new ImageIcon("3.png")).getImage();
                    }
                    else if(mines==4){
                        numberimage = (new ImageIcon("4.png")).getImage();
                    }
                    else if(mines==5){
                        numberimage = (new ImageIcon("5.png")).getImage();
                    }
                    else if(mines==6){
                        numberimage = (new ImageIcon("6.png")).getImage();
                    }
                    else if(mines==7){
                        numberimage = (new ImageIcon("7.png")).getImage();
                    }
                    else{
                        numberimage = (new ImageIcon("8.png")).getImage();
                    }
                    g.drawImage(numberimage, position.x * width, position.y * height, width, height, null);
                }
            }
        }
        else{
            if(hinted){
                g.drawImage(hintimage, position.x * width, position.y * height, width, height, null);
            }
            if(flagged){
                g.drawImage(flagimage, position.x * width, position.y * height, width, height, null);
            }
            if(!hinted && !flagged){
                g.drawImage(hiddenimage, position.x * width, position.y * height, width, height, null);
            }
        }
    }
}