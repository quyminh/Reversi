
import java.util.*;

class State {
	char[] board;

	public State(char[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
	}

	public int getScore() {
		int light = 0;
		int dark = 0;
		for (char c : board) {
			if (c == '1')
				dark++;
			else if (c == '2')
				light++;
		}
		if (dark > light)
			return 1;
		else if (dark < light)
			return -1;
		else
			return 0;
	}

	public boolean isTerminal() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[4 * i + j] == '0')
					for (int x = -1; x <= 1; x++)
						for (int y = -1; y <= 1; y++)
							if (i + x <= 3 && i + x >= 0 && j + y <= 3 && j + y >= 0 && (x != 0 || y != 0))
								if (board[(i + x) * 4 + j + y] == '2') {
									if (i + 2 * x <= 3 && i + 2 * x >= 0 && j + 2 * y <= 3 && j + 2 * y >= 0)
										if (board[(i + 2 * x) * 4 + j + 2 * y] == '1')
											return false;
										else if (i + 3 * x <= 3 && i + 3 * x >= 0 && j + 3 * y <= 3 && j + 3 * y >= 0
										        && board[(i + 2 * x) * 4 + j + 2 * y] != '0')
											if (board[(i + 3 * x) * 4 + j + 3 * y] == '1')
												return false;
								}

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[4 * i + j] == '0')
					for (int x = -1; x <= 1; x++)
						for (int y = -1; y <= 1; y++)
							if (i + x <= 3 && i + x >= 0 && j + y <= 3 && j + y >= 0 && (x != 0 || y != 0))
								if (board[(i + x) * 4 + j + y] == '1')
									if (i + 2 * x <= 3 && i + 2 * x >= 0 && j + 2 * y <= 3 && j + 2 * y >= 0)
										if (board[(i + 2 * x) * 4 + j + 2 * y] == '2')
											return false;
										else if (i + 3 * x <= 3 && i + 3 * x >= 0 && j + 3 * y <= 3 && j + 3 * y >= 0
										        && board[(i + 2 * x) * 4 + j + 2 * y] != '0')
											if (board[(i + 3 * x) * 4 + j + 3 * y] == '2')
												return false;

		return true;
	}

	public State[] getSuccessors(char player) {
		char opponent = (char) ('c' - player);
		ArrayList<State> successors = new ArrayList<State>();
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[4 * i + j] == '0') {
					boolean bl = false;
					char[] temp = board.clone();
					for (int x = -1; x <= 1; x++)
						for (int y = -1; y <= 1; y++)
							if (i + x <= 3 && i + x >= 0 && j + y <= 3 && j + y >= 0 && (x != 0 || y != 0))
								if (board[(i + x) * 4 + j + y] == opponent) {
									if (i + 2 * x <= 3 && i + 2 * x >= 0 && j + 2 * y <= 3 && j + 2 * y >= 0)
										if (board[(i + 2 * x) * 4 + j + 2 * y] == player) {
											temp[(i + x) * 4 + j + y] = player;
											bl = true;
										} else if (i + 3 * x <= 3 && i + 3 * x >= 0 && j + 3 * y <= 3 && j + 3 * y >= 0
										        && board[(i + 2 * x) * 4 + j + 2 * y] != '0')
											if (board[(i + 3 * x) * 4 + j + 3 * y] == player) {
												temp[(i + x) * 4 + j + y] = player;
												temp[(i + 2 * x) * 4 + j + 2 * y] = player;
												bl = true;
											}
								}
					if (bl) {
						temp[i * 4 + j] = player;
						successors.add(new State(temp));
					}
				}
		if (successors.isEmpty())
			for (int i = 0; i < 4; i++)
				for (int j = 0; j < 4; j++)
					if (board[4 * i + j] == '0')
						for (int x = -1; x <= 1; x++)
							for (int y = -1; y <= 1; y++)
								if (i + x <= 3 && i + x >= 0 && j + y <= 3 && j + y >= 0 && (x != 0 || y != 0))
									if (board[(i + x) * 4 + j + y] == player)
										if (i + 2 * x <= 3 && i + 2 * x >= 0 && j + 2 * y <= 3 && j + 2 * y >= 0)
											if (board[(i + 2 * x) * 4 + j + 2 * y] == opponent)
												return new State[] { this };
											else if (i + 3 * x <= 3 && i + 3 * x >= 0 && j + 3 * y <= 3 && j + 3 * y >= 0
											        && board[(i + 2 * x) * 4 + j + 2 * y] != '0')
												if (board[(i + 3 * x) * 4 + j + 3 * y] == opponent)
													return new State[] { this };

		return successors.toArray(new State[successors.size()]);
	}

	public void printState(int option, char player) {
		if (option == 1)
			for (State state : getSuccessors(player))
				System.out.println(state.getBoard());
		if (option == 2)
			if (!isTerminal())
				System.out.println("non-terminal");
			else
				System.out.println(getScore());
		if (option == 3) {
			System.out.println(Minimax.run(this, player));
			System.out.println(Minimax.count);
		}
		if (option == 4) {
			Minimax.run(this, player);
			System.out.println(Minimax.first);
		}
		if (option == 5) {
			System.out.println(Minimax.run_with_pruning(this, player));
			System.out.println(Minimax.count);
		}
		if (option == 6) {
			Minimax.run_with_pruning(this, player);
			System.out.println(Minimax.first);
		}

	}

	public String getBoard() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 16; i++)
			builder.append(this.board[i]);
		return builder.toString().trim();
	}

	public boolean equals(State src) {
		for (int i = 0; i < 16; i++)
			if (this.board[i] != src.board[i])
				return false;
		return true;
	}
}

class Minimax {
	static int count = 0;
	static String first = "";

	private static int max_value(State curr_state) {
		count++;
		int alpha = Integer.MIN_VALUE;
		if (curr_state.isTerminal())
			return curr_state.getScore();
		else
			for (State succ : curr_state.getSuccessors('1')) {
				int temp = min_value(succ);
				if (alpha < temp)
					alpha = temp;
			}
		return alpha;
	}

	private static int min_value(State curr_state) {
		count++;
		int beta = Integer.MAX_VALUE;
		if (curr_state.isTerminal())
			return curr_state.getScore();
		else
			for (State succ : curr_state.getSuccessors('2')) {
				int temp = max_value(succ);
				if (beta > temp)
					beta = temp;
			}
		return beta;
	}

	private static int max_value_with_pruning(State curr_state, int alpha, int beta) {
		count++;
		if (curr_state.isTerminal())
			return curr_state.getScore();
		else
			for (State succ : curr_state.getSuccessors('1')) {
				int temp = min_value_with_pruning(succ, alpha, beta);
				if (alpha < temp)
					alpha = temp;
				if (alpha >= beta)
					return beta;
			}
		return alpha;
	}

	private static int min_value_with_pruning(State curr_state, int alpha, int beta) {
		count++;
		if (curr_state.isTerminal())
			return curr_state.getScore();
		else
			for (State succ : curr_state.getSuccessors('2')) {
				int temp = max_value_with_pruning(succ, alpha, beta);
				if (beta > temp)
					beta = temp;
				if (alpha >= beta)
					return alpha;
			}
		return beta;
	}

	public static int run(State curr_state, char player) {
		count = 1;
		first = "";
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		if (curr_state.isTerminal())
			return curr_state.getScore();
		else if (player == '1') {
			for (State succ : curr_state.getSuccessors('1')) {
				int temp = min_value(succ);
				if (alpha < temp) {
					first = succ.getBoard();
					alpha = temp;
				}
			}
			return alpha;
		} else {
			for (State succ : curr_state.getSuccessors('2')) {
				int temp = max_value(succ);
				if (beta > temp) {
					first = succ.getBoard();
					beta = temp;
				}
			}
			return beta;
		}
	}

	public static int run_with_pruning(State curr_state, char player) {
		count = 1;
		first = "";
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		if (curr_state.isTerminal())
			return curr_state.getScore();
		if (player == '1') {
			for (State succ : curr_state.getSuccessors('1')) {
				int temp = min_value_with_pruning(succ, alpha, beta);
				if (alpha < temp) {
					first = succ.getBoard();
					alpha = temp;
				}
				if (alpha >= beta)
					return beta;

			}
			return alpha;
		} else {
			for (State succ : curr_state.getSuccessors('2')) {
				int temp = max_value_with_pruning(succ, alpha, beta);
				if (beta > temp) {
					first = succ.getBoard();
					beta = temp;
				}
				if (alpha >= beta)
					return alpha;
			}
			return beta;
		}
	}

}

public class Reversi {
	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.println("Invalid Number of Input Arguments");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		char[] board = new char[16];
		for (int i = 0; i < 16; i++)
			board[i] = args[2].charAt(i);
		int option = flag / 100;
		char player = args[1].charAt(0);
		if ((player != '1' && player != '2') || args[1].length() != 1) {
			System.out.println("Invalid Player Input");
			return;
		}
		State init = new State(board);
		init.printState(option, player);
	}
}
