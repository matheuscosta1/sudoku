package org.sudoku;

import java.util.ArrayList;
import java.util.Random;

public class SudokuBoard implements Cloneable {
    public int[][] board;
    public int size;
    private int heuristicCost;
    private final Type type;
    private int cost;

    public String stringBoard;

    public SudokuBoard(int size, Type type) {
        this.size = size;
        this.type = type;
        this.board = new int[size][size];
    }

    public SudokuBoard(Type type) {
        this.type = type;
    }

    public void populateBoardByTxtFile(String str) {

        int auxIndex = 0;

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                while (true) {
                    char c = str.charAt(auxIndex);
                    auxIndex += 1;

                    if (!Character.isDigit(c))
                        continue;

                    if (Character.getNumericValue(c) != 0)
                        this.board[i][j] = Character.getNumericValue(c);

                    break;

                }

            }
        }

    }

    public int getHeuristicCost() {
        return this.heuristicCost;
    }

    public int getTotalCost() {
        return this.heuristicCost + this.getCostFunction();
    }

    public Type getSudokuBoardType() {
        return this.type;
    }

    public void setHeuristic() {
        int emptyCells = 0;

        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                if (this.board[row][col] == 0) {
                    emptyCells += 1;
                }
            }
        }

        this.heuristicCost = emptyCells;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public int getPossibilitiesCount(int row, int col) {

        if (this.board[row][col] != 0) {
            return 0; // Retorna 0 para células preenchidas
        }

        int count = 0;

        for (int num = 1; num <= this.size; num++) {
            if (isValidMove(row, col, num)) {
                count++;
            }
        }

        return count;
    }

    public boolean isValidMove(int row, int col, int value) {

        // Verifica se o valor já existe na linha, coluna ou subgrid
        for (int i = 0; i < this.size; i++) {

            if (isInvalidPlacement(row, col, value, i))
                return false;

        }
        return true;
    }

    public void setCosts(ArrayList<SudokuBoard> sus) {
        for (SudokuBoard su : sus) {
            int cost = 0;

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    cost += su.getManhattanDistance(row, col);
                }
            }

            su.setCost(cost);
        }
    }

    public int getCostFunction() {

        int cost = 0;

        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                // cost += this.getManhattanDistance(row, col);
                if (this.board[row][col] == 0)
                    cost += 1;
            }
        }

        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public int getManhattanDistance(int row, int col) {
        if (this.board[row][col] != 0) {
            return 0; // Retorna 0 para células preenchidas
        }

        int targetRow, targetCol;

        // Encontre a posição correta da célula vazia com base no valor do Sudoku
        // (subtraia 1 porque os índices de array começam em 0)
        targetRow = (this.board[row][col] - 1) / this.size;
        targetCol = (this.board[row][col] - 1) % this.size;

        // Calcule a distância de Manhattan
        int distance = Math.abs(row - targetRow) + Math.abs(col - targetCol);

        return distance;
    }

    private boolean isInvalidPlacement(int row, int col, int value, int i) {
        if (isComplexBoard()) {
            return isNumberInRow(row, value, i) || isNumberInColumn(col, value, i)
                    || isNumberInSubGrid(row, col, value, i);
        } else {
            return isNumberInRow(row, value, i) || isNumberInColumn(col, value, i);
        }
    }

    private boolean isComplexBoard() {
        return Type.COMPLEX.equals(getSudokuBoardType());
    }

    private boolean isNumberInColumn(int col, int value, int i) {
        return this.board[i][col] == value;
    }

    private boolean isNumberInRow(int row, int value, int i) {
        return this.board[row][i] == value;
    }

    private boolean isNumberInSubGrid(int row, int col, int value, int i) {
        int subRow = (int) Math.sqrt(this.size) * (row / (int) Math.sqrt(this.size))
                + i / (int) Math.sqrt(this.size);
        int subCol = (int) Math.sqrt(this.size) * (col / (int) Math.sqrt(this.size))
                + i % (int) Math.sqrt(this.size);
        return this.board[subRow][subCol] == value;
    }

    public void printBoard() {

        double repeatFactor = (this.size > 4 ? 2.8 : 3.3);

        String divisionBar = "-".repeat((int) (this.size * repeatFactor));

        System.out.print("\n" + divisionBar + "\n");

        for (int row = 0; row < this.size; row++) {
            if (row % Math.sqrt(this.size) == 0 && row != 0) {
                System.out.print(divisionBar + "\n");
            }
            for (int col = 0; col < this.size; col++) {
                if (col % Math.sqrt(this.size) == 0 && col != 0) {
                    System.out.print("| ");
                }
                if (col == 0) {
                    System.out.print("| " + this.board[row][col] + " ");
                } else if (col == this.size - 1) {
                    System.out.print(this.board[row][col] + " |");
                } else {
                    System.out.print(this.board[row][col] + " ");
                }
            }
            System.out.println();
        }
        System.out.print(divisionBar + "\n");
    }

    public String convertBoardIntoString() {

        double repeatFactor = (this.size > 4 ? 2.8 : 3.3);

        String divisionBar = "-".repeat((int) (this.size * repeatFactor));

        String divisionBarWithBreakLine = "\n" + divisionBar + "\n";
        StringBuilder sb = new StringBuilder();

        sb.append(divisionBarWithBreakLine);


        for (int row = 0; row < this.size; row++) {
            if (row % Math.sqrt(this.size) == 0 && row != 0) {
                sb.append(divisionBar).append("\n");
            }
            for (int col = 0; col < this.size; col++) {
                if (col % Math.sqrt(this.size) == 0 && col != 0) {
                    sb.append("| ");
                }
                if(col == 0) {
                    sb.append("| ").append(this.board[row][col]).append(" ");
                } else if (col == this.size - 1) {
                    sb.append(this.board[row][col]).append(" |");
                } else {
                    sb.append(this.board[row][col]).append(" ");
                }
            }
            sb.append("\n");
        }
        sb.append(divisionBar).append("\n");

        this.stringBoard = sb.toString();

        return sb.toString();
    }

    private int[] getNextEmptyCell() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j] == 0) {

                    return new int[]{ i, j };
                }
            }
        }

        return null;
    }

    public ArrayList<SudokuBoard> extendBoard() throws CloneNotSupportedException {
        int[] nextEmptyCell = this.getNextEmptyCell();

        if (nextEmptyCell == null)
            return null;

        int row = nextEmptyCell[0];
        int col = nextEmptyCell[1];

        ArrayList<SudokuBoard> newSus = new ArrayList<>();

        for (int value = 1; value <= this.size; value++) {
            if (this.isValidMove(row, col, value)) {
                SudokuBoard newSu = (SudokuBoard) this.clone();
                newSu.board[row][col] = value;
                newSus.add(newSu);
            }
        }

        return newSus;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuBoard cloned = (SudokuBoard) super.clone();
        cloned.size = this.size;
        int[][] board = new int[cloned.size][cloned.size];

        for (int i = 0; i < cloned.size; i++) {
            System.arraycopy(this.board[i], 0, board[i], 0, cloned.size);
        }

        cloned.board = board;

        return cloned;
    }

    public boolean isSolution() {
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                int value = this.board[row][col];
                this.board[row][col] = 0;
                if (!this.isValidMove(row, col, value) || value == 0) {
                    return false;
                }

                this.board[row][col] = value;
            }
        }

        return true;
    }

    public int calculateCostOfRepeatedNumbersInRowColumnOrSubGrid(int[][] board) {
        int cost = 0;

        cost = getCostOfRepeatedNumbersInRow(board, cost);

        cost = getCostOfRepeatedNumbersInColumn(board, cost);

        if(isComplexBoard()) {
            cost = getCostOfRepeatedNumbersInSubGrid(board, cost);

        }

        return cost;
    }

    public int calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(int[][] board) {
        int cost = 0;

        cost = getCostOfRepeatedNumbersInRow(board, cost);

        cost = getCostOfRepeatedNumbersInColumn(board, cost);

        cost = getCostOfEmptyCells(board, cost);

        if(isComplexBoard()) {
            cost = getCostOfRepeatedNumbersInSubGrid(board, cost);
        }

        return cost;
    }

    private int getCostOfRepeatedNumbersInSubGrid(int[][] board,int cost) {
        int boardSize = board.length;
        int regionSize = boardSize == 9 ? 3 : 2;

        for (int i = 0; i < boardSize; i += regionSize) {
            for (int j = 0; j < boardSize; j += regionSize) {
                int[] counts = new int[boardSize + 1];
                for (int k = 0; k < regionSize; k++) {
                    for (int l = 0; l < regionSize; l++) {
                        counts[board[i + k][j + l]]++;
                    }
                }
                for (int k = 1; k <= boardSize; k++) {
                    cost += counts[k] > 1 ? counts[k] - 1 : 0;
                }
            }
        }
        return cost;
    }

    private int getCostOfRepeatedNumbersInRow(int[][] board, int cost) {
        int length = board.length;
        for (int[] ints : board) {
            int[] counts = new int[length + 1];
            for (int j = 0; j < length; j++) {
                counts[ints[j]]++;
            }
            for (int j = 1; j <= length; j++) {
                cost += counts[j] > 1 ? counts[j] - 1 : 0;
            }
        }
        return cost;
    }

    private int getCostOfRepeatedNumbersInColumn(int[][] board, int cost) {
        int boardSize = board.length;
        for (int j = 0; j < boardSize; j++) {
            int[] counts = new int[boardSize + 1];
            for (int[] ints : board) {
                counts[ints[j]]++;
            }
            for (int i = 1; i <= boardSize; i++) {
                cost += counts[i] > 1 ? counts[i] - 1 : 0;
            }
        }
        return cost;
    }

    private int getCostOfEmptyCells(int[][] board, int cost) {
        int boardSize = board.length;
        for (int[] ints : board) {
            for (int j = 0; j < boardSize; j++) {
                if (ints[j] == 0) {
                    cost += 1;
                }
            }
        }
        return cost;
    }

    public int[][] perturbBoard() {
        Random random = new Random();

        int boardSize = board.length;
        int[][] newBoard = new int[boardSize][boardSize];
        copyBoard(this.board, newBoard);

        int row = random.nextInt(boardSize);
        int col = random.nextInt(boardSize);
        int value = random.nextInt(boardSize) + 1;
        newBoard[row][col] = value;

        return newBoard;
    }

    public int[][] perturbBoardForHillClimbing(int row, int column) {
        Random random = new Random();

        int boardSize = board.length;
        int[][] newBoard = new int[boardSize][boardSize];
        copyBoard(this.board, newBoard);

        int value = random.nextInt(boardSize) + 1;

        if(isValidMove(row, column, value)) {
            newBoard[row][column] = value;

            return newBoard;
        }

        return newBoard;
    }

    public boolean acceptPerturbedSolution(int newCost, int oldCost, double temperature) {
        Random random = new Random();

        if (newCost < oldCost) {
            return true;
        }
        double probability = Math.exp(-(newCost - oldCost) / temperature);
        return random.nextDouble() < probability;
    }

    private static void copyBoard(int[][] source, int[][] dest) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, dest[i], 0, source.length);
        }
    }

    public void initializeBoard() {
        int boardSize = this.size;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int value;

                Random random = new Random();
                value = random.nextInt(boardSize) + 1;

                if (isValidMove(i, j, value)) {
                    board[i][j] = value;
                }
            }
        }
    }

    public SudokuBoard initializeBoardRandomWay() {
        int boardSize = this.size;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int value;

                Random random = new Random();
                value = random.nextInt(boardSize) + 1;

                board[i][j] = value;
            }
        }
        return this;
    }

    public CoordinateCell listOfCoordinatesOfCells() {
        CoordinateCell coordinateCell= new CoordinateCell();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] != 0 || board[i][j] == 0) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.row = i;
                    coordinate.column = j;
                    coordinateCell.coordinate.add(coordinate);
                }
            }
        }
        return coordinateCell;
    }

    public CoordinateCell listOfCoordinatesOfCellsThatAreEmpty() {
        CoordinateCell coordinateCell= new CoordinateCell();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == 0) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.row = i;
                    coordinate.column = j;
                    coordinateCell.coordinate.add(coordinate);
                }
            }
        }
        return coordinateCell;
    }

    @Override
    public String toString() {

        return convertBoardIntoString();
    }
}
