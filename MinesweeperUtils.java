import java.util.ArrayList;
import java.util.Stack;

/**
 * This class contains utility methods for a game of MineSweeper. The only
 * requirements for using these methods is that your T class contain getRow()
 * and getCol() methods.
 * 
 * @author Leonard Smith
 * @version 1.0
 */
public class MinesweeperUtils {

	/**
	 * This method finds and returns an ArrayList of all valid adjacent Ts in
	 * the given 2-d board array.
	 * 
	 * @param x
	 *            The row of the MineTile in question
	 * @param y
	 *            The col of the MineTile in question
	 * @param board
	 *            The 2-dimensional array of MineTiles
	 * @return An ArrayList of all valid adjacent MineTiles in the 2-d board
	 *         array
	 */
	private static <T extends MineTile> ArrayList<T> getAdjacent(int x, int y, T[][] board) {
		ArrayList<T> adjList = new ArrayList<T>();

		if (x > 0 && y > 0) // northwest tile
			adjList.add(board[x - 1][y - 1]);
		if (x > 0) // north tile
			adjList.add(board[x - 1][y]);
		if (x > 0 && y < board[x].length - 1) // northeast tile
			adjList.add(board[x - 1][y + 1]);
		if (y > 0) // west tile
			adjList.add(board[x][y - 1]);
		if (y < board[x].length - 1) // east tile
			adjList.add(board[x][y + 1]);
		if (x < board.length - 1 && y > 0) // southwest tile
			adjList.add(board[x + 1][y - 1]);
		if (x < board.length - 1) // south tile
			adjList.add(board[x + 1][y]);
		if (x < board.length - 1 && y < board[x].length - 1) // southeast tile
			adjList.add(board[x + 1][y + 1]);

		return adjList;
	}

	/**
	 * This method returns a list of the MineTiles that should be opened when a
	 * given tile is opened due to having zero adjacent mines in a game of
	 * MineSweeper.
	 * 
	 * @param x
	 *            The row of the MineTile to be expanded
	 * @param y
	 *            The col of the MineTile to be expanded
	 * @param board
	 *            The 2-dimensional array of MineTiles
	 * @return An ArrayList containing all of the MineTiles that should be
	 *         opened.
	 */
	public static <T extends MineTile> ArrayList<T> expandTile(T tile, T[][] board) {
		ArrayList<T> toOpen = new ArrayList<T>();
		ArrayList<T> visited = new ArrayList<T>();
		Stack<T> stack = new Stack<T>();
		stack.push(tile);

		while (!stack.empty()) {
			T current = stack.pop();
			visited.add(current); // keep track of visited tiles (we don't want
									// an infinite loop!)
			if (current.getAdjacent() == 0) {
				for (T b : getAdjacent(current.getRow(), current.getCol(), board)) {
					if (!toOpen.contains(b)) // add adjacent tile to toOpen (no
												// duplicates)
						toOpen.add(b);
					if (!visited.contains(b))
						stack.push(b);
				}
			}

		}
		return toOpen;
	}
}
