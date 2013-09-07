/**
 * MineTile type demanded by MineSweeperUtils. Have your Minesweeper Tiles (be
 * them buttons, panels, etc) implement this interface.
 * 
 * @author Eric
 * 
 */
public interface MineTile {

	/**
	 * Get the row the tile is in.
	 * 
	 * @return the row.
	 */
	int getRow();

	/**
	 * Get the column the tile is in.
	 * 
	 * @return the column.
	 */
	int getCol();

	/**
	 * Get the amount of adjacent mines.
	 * 
	 * @return the amount of adjacent mines.
	 */
	int getAdjacent();
}
