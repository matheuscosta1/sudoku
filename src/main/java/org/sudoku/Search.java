package org.sudoku;


import org.sudoku.response.SudokuResponse;

import java.util.*;

public class Search {

    private static final int SIMULATED_ANNEALING_MAX_MOVES = 10000;
    private static final double SIMULATED_ANNEALING_INITIAL_TEMPERATURE = 1.0;
    private static final double SIMULATED_ANNEALING_COOLING_FACTOR = 0.95;
    private static final double SIMULATED_ANNEALING_FINAL_TEMPERATURE = 0.0001;
    private static final int HILL_CLIMBING_MAX_ITERATIONS = 100000;
    private static final int MAX_MOVES_WITHOUT_IMPROVEMENT = 1000;

    Search() {

    }

    public SudokuResponse iterativeDepthSearch(int limit, SudokuBoard su) throws CloneNotSupportedException {
        SudokuResponse sudokuResponse = new SudokuResponse();
        List<SudokuBoard> steps = new ArrayList<>();
        steps.add(su);
        sudokuResponse.setSteps(steps);

        if (su.isSolution())
            return sudokuResponse;

        for (int depth = 1; depth <= limit; depth++) {

            sudokuResponse = this.depthLimitedSearch(depth, su, sudokuResponse);

            if (sudokuResponse.getSteps().get(sudokuResponse.getSteps().size() - 1).isSolution()) {
                System.out.println("Solução encontrada com profundidade " + depth);
                break;
            }
        }

        return sudokuResponse;
    }

    public void setHeuristics(ArrayList<SudokuBoard> sus) {
        for (SudokuBoard sudokuBoard : sus) {
            sudokuBoard.setHeuristic();
        }
    }

    public void setHeuristic2(SudokuBoard su) {

        int heuristic = 0;

        for (int row = 0; row < su.size; row++) {
            for (int col = 0; col < su.size; col++) {
                heuristic += su.getPossibilitiesCount(row, col);
            }
        }

        su.setHeuristicCost(heuristic);

    }

    public void setHeuristics2(ArrayList<SudokuBoard> sus) {

        for (SudokuBoard su : sus) {
            int heuristic = 0;

            for (int row = 0; row < su.size; row++) {
                for (int col = 0; col < su.size; col++) {
                    heuristic += su.getPossibilitiesCount(row, col);
                }
            }

            su.setHeuristicCost(heuristic);
        }

    }

    private void addInOrderToList(ArrayList<SudokuBoard> list, SudokuBoard su) {

        boolean isAdded = false;

        for (int i = 0; i < list.size(); i++) {
            if (su.getHeuristicCost() < list.get(i).getHeuristicCost()) {
                list.add(i, su);
                isAdded = true;
                break;
            }
        }

        if (!isAdded) {
            list.add(su);
        }
    }

    public SudokuResponse greedySearch(SudokuBoard su) throws CloneNotSupportedException {
        SudokuBoard selected_su;

        this.setHeuristic2(su);

        int numberOfSteps = 0;

        ArrayList<SudokuBoard> stack = new ArrayList<>();
        stack.add(su);

        ArrayList<SudokuBoard> visitedNodes = new ArrayList<>();

        ArrayList<SudokuBoard> exploredNodes = new ArrayList<>();

        SudokuResponse sudokuResponse = new SudokuResponse();

        while (true) {

            if (stack.size() == 0) {
                System.out.println("Solução não encontrada!");
                break;
            }

            selected_su = stack.get(0);

            stack.remove(0);

            visitedNodes.add(selected_su);

            if (selected_su.isSolution()) {
                System.out.println("Solução encontrada na profundidade: " + numberOfSteps + ". Explorando "
                        + exploredNodes.size() + " nós.");
                sudokuResponse.setDepth(String.valueOf(numberOfSteps));
                break;
            }

            ArrayList<SudokuBoard> new_sus = selected_su.extendBoard();

            if (new_sus == null)
                continue;

            if (new_sus.size() > 0) {

                exploredNodes.add(selected_su);

                this.setHeuristics2(new_sus);

                Collections.sort(new_sus, Comparator.comparing(SudokuBoard::getHeuristicCost));

                for (SudokuBoard newSus : new_sus) {
                    if (!stack.contains(newSus) && !visitedNodes.contains(newSus)) {
                        numberOfSteps++;
                        this.addInOrderToList(stack, newSus);
                    }
                }
            }
        }

        sudokuResponse.setResolutionMethod(
                "Busca gulosa de implementação '1' de tamanho " + su.board.length + "x" + su.board.length);
        sudokuResponse.setSteps(visitedNodes);
        sudokuResponse.setQuantityOfVisitedNodes(String.valueOf(visitedNodes.size()));
        sudokuResponse.setQuantityExploredNodes(String.valueOf(exploredNodes.size()));
        sudokuResponse.setComplexity(su.getSudokuBoardType().name());

        return sudokuResponse;

    }

    public SudokuResponse aStarSearch(SudokuBoard initialBoard) throws CloneNotSupportedException {

        int numberOfSteps = 0;

        // Inicialização do openSet e exploredNodes
        PriorityQueue<SudokuBoard> openSet = new PriorityQueue<>(
                Comparator.comparingInt(SudokuBoard::getTotalCost));

        ArrayList<SudokuBoard> exploredNodes = new ArrayList<>();

        // Definir o custo inicial e heurística para o estado inicial
        initialBoard.setCost(0);
        this.setHeuristic2(initialBoard);

        // Adicionar o estado inicial ao openSet
        openSet.add(initialBoard);

        SudokuResponse sudokuResponse = new SudokuResponse();

        SudokuBoard currentBoard;

        while (!openSet.isEmpty()) {

            currentBoard = openSet.poll();

            // Verificar se o estado atual é a solução
            if (currentBoard.isSolution()) {

                if (!exploredNodes.contains(currentBoard))
                    exploredNodes.add(currentBoard);

                sudokuResponse.setDepth(String.valueOf(numberOfSteps));

                System.out.println("Solução encontrada na profundidade: " + numberOfSteps + ". Explorando "
                        + exploredNodes.size() + " nós.");

                break;
            }

            exploredNodes.add(currentBoard);

            List<SudokuBoard> neighbors = currentBoard.extendBoard();

            for (SudokuBoard neighbor : neighbors) {
                // Verificar se o estado vizinho já foi explorado
                if (exploredNodes.contains(neighbor)) {
                    continue;
                }

                this.setHeuristic2(neighbor);

                // Calcular o custo para o vizinho
                int newTotalCost = currentBoard.getCost() + neighbor.getTotalCost();

                if (!exploredNodes.contains(neighbor) || newTotalCost < neighbor.getCost()) {
                    neighbor.setCost(newTotalCost);
                    this.setHeuristic2(neighbor);

                    openSet.add(neighbor);
                    numberOfSteps++;
                }
            }
            // }

        }

        sudokuResponse.setResolutionMethod(
                "Busca A estrela de tamanho " + initialBoard.board.length + "x" + initialBoard.board.length);
        sudokuResponse.setSteps(exploredNodes);
        sudokuResponse.setQuantityExploredNodes(String.valueOf(exploredNodes.size()));
        sudokuResponse.setComplexity(initialBoard.getSudokuBoardType().name());
        // System.out.println("Solução não encontrada!");
        return sudokuResponse;
    }

    public SudokuResponse greedySearch2(SudokuBoard su) throws CloneNotSupportedException {
        SudokuBoard selected_su;
        su.setHeuristic();

        int numberOfSteps = 0;

        ArrayList<SudokuBoard> stack = new ArrayList<>();
        stack.add(su);

        ArrayList<SudokuBoard> visitedNodes = new ArrayList<>();

        ArrayList<SudokuBoard> exploredNodes = new ArrayList<>();

        SudokuResponse sudokuResponse = new SudokuResponse();

        while (true) {

            numberOfSteps++;

            if (stack.size() == 0) {
                System.out.println("Solução não encontrada!");
                break;
            }

            selected_su = stack.get(0);

            stack.remove(0);

            visitedNodes.add(selected_su);

            if (selected_su.isSolution()) {
                System.out.println("Solução encontrada na profundidade: " + numberOfSteps + ". Explorando "
                        + exploredNodes.size() + " nós.");
                sudokuResponse.setDepth(String.valueOf(numberOfSteps));
                break;
            }

            ArrayList<SudokuBoard> new_sus = selected_su.extendBoard();

            if (new_sus == null)
                continue;

            if (new_sus.size() > 0) {

                exploredNodes.add(selected_su);

                this.setHeuristics(new_sus);

                Collections.sort(new_sus, Comparator.comparing(SudokuBoard::getHeuristicCost));

                for (SudokuBoard newSus : new_sus) {
                    if (!stack.contains(newSus) && !visitedNodes.contains(newSus)) {
                        this.addInOrderToList(stack, newSus);
                    }
                }
            }
        }

        sudokuResponse.setResolutionMethod(
                "Busca gulosa de implementação '2' de tamanho " + su.board.length + "x" + su.board.length);
        sudokuResponse.setSteps(visitedNodes);
        sudokuResponse.setQuantityOfVisitedNodes(String.valueOf(visitedNodes.size()));
        sudokuResponse.setQuantityExploredNodes(String.valueOf(exploredNodes.size()));
        sudokuResponse.setComplexity(su.getSudokuBoardType().name());

        return sudokuResponse;
    }

    public SudokuResponse depthLimitedSearch(int depth, SudokuBoard su, SudokuResponse sudokuResponse)
            throws CloneNotSupportedException {

        SudokuBoard selectedSudokuBoard;

        int numberOfSteps = 0;

        Stack<SudokuBoard> stack = new Stack<>();
        stack.push(su);

        ArrayList<SudokuBoard> visitedSudokuBoardNode = new ArrayList<>();
        ArrayList<SudokuBoard> exploredSudokuBoardNodes = new ArrayList<>();

        while (numberOfSteps < depth) {

            numberOfSteps++;

            if (stack.isEmpty()) {
                System.out.println("Solução não encontrada!");
                break;
            }

            selectedSudokuBoard = stack.pop();

            visitedSudokuBoardNode.add(selectedSudokuBoard);

            if (selectedSudokuBoard.isSolution()) {
                System.out.println("Solução encontrada na profundidade: " + numberOfSteps + ". Explorando "
                        + exploredSudokuBoardNodes.size() + " nós.");
                sudokuResponse.setDepth(String.valueOf(numberOfSteps));
                break;
            }

            ArrayList<SudokuBoard> newSudokuBoardNodesExtendedFromActualNode = selectedSudokuBoard.extendBoard();

            if (newSudokuBoardNodesExtendedFromActualNode == null)
                continue;

            addExpandedNodesThatWasNotVisitedIntoStack(selectedSudokuBoard, stack, visitedSudokuBoardNode,
                    exploredSudokuBoardNodes, newSudokuBoardNodesExtendedFromActualNode);
        }

        sudokuResponse
                .setResolutionMethod("Busca em profundidade Iterativa " + su.board.length + "x" + su.board.length);
        sudokuResponse.setSteps(visitedSudokuBoardNode);
        sudokuResponse.setQuantityOfVisitedNodes(String.valueOf(visitedSudokuBoardNode.size()));
        sudokuResponse.setQuantityExploredNodes(String.valueOf(exploredSudokuBoardNodes.size()));
        sudokuResponse.setComplexity(su.getSudokuBoardType().name());

        return sudokuResponse;

    }

    private static void addExpandedNodesThatWasNotVisitedIntoStack(SudokuBoard selectedSudokuBoard,
            Stack<SudokuBoard> stack, ArrayList<SudokuBoard> visitedSudokuBoardNode,
            ArrayList<SudokuBoard> exploredSudokuBoardNodes,
            ArrayList<SudokuBoard> newSudokuBoardNodesExtendedFromActualNode) {
        if (newSudokuBoardNodesExtendedFromActualNode.size() > 0) {
            exploredSudokuBoardNodes.add(selectedSudokuBoard);
            for (int i = newSudokuBoardNodesExtendedFromActualNode.size() - 1; i >= 0; i--) {
                if (!stack.contains(newSudokuBoardNodesExtendedFromActualNode.get(i))
                        && !visitedSudokuBoardNode.contains(newSudokuBoardNodesExtendedFromActualNode.get(i))) {
                    stack.push(newSudokuBoardNodesExtendedFromActualNode.get(i));
                }
            }
        }
    }

    public SudokuResponse simulatedAnnealing(SudokuBoard sudokuBoard) {
        int currentCost = sudokuBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);
        int initialCost = sudokuBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);
        double temperature = SIMULATED_ANNEALING_INITIAL_TEMPERATURE;

        int visitedNodes = 0;

        SudokuResponse sudokuResponse = new SudokuResponse();
        sudokuResponse.setInitialTemperature(String.valueOf(temperature));

        List<SudokuBoard> steps = new ArrayList<>();

        SudokuBoard firstBoardVersion = new SudokuBoard(sudokuBoard.getSudokuBoardType());
        firstBoardVersion.board = sudokuBoard.board;
        firstBoardVersion.size = sudokuBoard.size;

        steps.add(firstBoardVersion);

        int maxMoves;
        maxMoves = sudokuBoard.getSudokuBoardType().equals(Type.COMPLEX) ? SIMULATED_ANNEALING_MAX_MOVES : 1000;

        while (!sudokuBoard.isSolution() && temperature > SIMULATED_ANNEALING_FINAL_TEMPERATURE) {
            for (int i = 0; i < maxMoves; i++) {
                int[][] newBoard = sudokuBoard.perturbBoard();
                int newCost = sudokuBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(newBoard);
                if (sudokuBoard.acceptPerturbedSolution(newCost, currentCost, temperature)) {

                    if (sudokuBoard.isSolution()) {
                        break;
                    }

                    SudokuBoard sudokuBoardCopy = new SudokuBoard(sudokuBoard.getSudokuBoardType());

                    sudokuBoard.board = newBoard;

                    sudokuBoardCopy.board = sudokuBoard.board;
                    sudokuBoardCopy.size = sudokuBoard.size;
                    sudokuBoardCopy.stringBoard = sudokuBoardCopy.convertBoardIntoString();

                    steps.add(sudokuBoardCopy);
                    visitedNodes++;

                    currentCost = newCost;
                }
            }
            temperature *= SIMULATED_ANNEALING_COOLING_FACTOR;
        }

        int finalCost = sudokuBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);

        sudokuResponse.setFinalTemperature(String.valueOf(temperature));

        sudokuResponse.setResolutionMethod(
                "Têmpera Simulada de tamanho " + sudokuBoard.board.length + "x" + sudokuBoard.board.length);
        sudokuResponse.setFinalCost(String.valueOf(finalCost));
        sudokuResponse.setInitialCost(String.valueOf(initialCost));
        sudokuResponse.setSteps(steps);
        sudokuResponse.setQuantityOfVisitedNodes(String.valueOf(visitedNodes));
        sudokuResponse.setComplexity(sudokuBoard.getSudokuBoardType().name());

        return sudokuResponse;
    }

    public SudokuResponse hillClimbingWithLateralMoves(SudokuBoard sudokuBoard) {
        int quantityOfLateralMoves = 0;
        boolean foundBetterCostBoard;
        int maxIterations = 0;

        int row, column;
        Random random = new Random();

        int visitedNodes = 0;

        SudokuResponse sudokuResponse = new SudokuResponse();
        List<SudokuBoard> steps = new ArrayList<>();

        SudokuBoard firstBoardVersion = new SudokuBoard(sudokuBoard.getSudokuBoardType());
        firstBoardVersion.board = sudokuBoard.board;
        firstBoardVersion.size = sudokuBoard.size;
        int initialCost = sudokuBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);

        steps.add(firstBoardVersion);
        int costOriginalBoard, neighborCost;
        while (!sudokuBoard.isSolution() && maxIterations < HILL_CLIMBING_MAX_ITERATIONS) {

            CoordinateCell coordinateEmptyCells = sudokuBoard.listOfCoordinatesOfCellsThatAreEmpty();

            Coordinate randomEmptyCell = coordinateEmptyCells.coordinate
                    .get(random.nextInt(coordinateEmptyCells.coordinate.size()));

            row = randomEmptyCell.row;
            column = randomEmptyCell.column;
            foundBetterCostBoard = false;

            SudokuBoard neighborBoard = new SudokuBoard(sudokuBoard.board.length, sudokuBoard.getSudokuBoardType());
            neighborBoard.board = sudokuBoard.board;
            neighborBoard.size = sudokuBoard.size;

            int[][] newBoard = neighborBoard.perturbBoardForHillClimbing(row, column);

            neighborCost = neighborBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(newBoard);
            costOriginalBoard = sudokuBoard
                    .calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);

            if (neighborCost < costOriginalBoard) {

                SudokuBoard sudokuBoardCopy = new SudokuBoard(sudokuBoard.getSudokuBoardType());

                sudokuBoard.board = newBoard;

                sudokuBoardCopy.board = sudokuBoard.board;
                sudokuBoardCopy.size = sudokuBoard.size;
                sudokuBoardCopy.stringBoard = sudokuBoardCopy.convertBoardIntoString();

                steps.add(sudokuBoardCopy);
                visitedNodes++;

                foundBetterCostBoard = true;
            }

            if (!foundBetterCostBoard) {
                while (true) {

                    newBoard = new int[sudokuBoard.board.length][sudokuBoard.board.length];

                    copyBoard(sudokuBoard.board, newBoard);

                    CoordinateCell coordinateCell = sudokuBoard.listOfCoordinatesOfCells();

                    Coordinate first = coordinateCell.coordinate.get(random.nextInt(coordinateCell.coordinate.size()));
                    Coordinate second = coordinateCell.coordinate.get(random.nextInt(coordinateCell.coordinate.size()));

                    int firstValueTemp, secondValueTemp;

                    int firstRow = first.row;
                    int firstColumn = first.column;
                    int secondRow = second.row;
                    int secondColumn = second.column;

                    firstValueTemp = newBoard[firstRow][firstColumn];
                    secondValueTemp = newBoard[secondRow][secondColumn];

                    newBoard[firstRow][firstColumn] = secondValueTemp;
                    newBoard[secondRow][secondColumn] = firstValueTemp;

                    int costNeighborThatHadLateralMove = sudokuBoard
                            .calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(newBoard);
                    costOriginalBoard = sudokuBoard
                            .calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);

                    quantityOfLateralMoves++;
                    if (costNeighborThatHadLateralMove <= costOriginalBoard) {

                        SudokuBoard sudokuBoardCopy = new SudokuBoard(sudokuBoard.getSudokuBoardType());

                        sudokuBoard.board = newBoard;

                        sudokuBoardCopy.board = sudokuBoard.board;
                        sudokuBoardCopy.size = sudokuBoard.size;
                        sudokuBoardCopy.stringBoard = sudokuBoardCopy.convertBoardIntoString();

                        steps.add(sudokuBoardCopy);
                        visitedNodes++;

                        quantityOfLateralMoves = 0;
                        break;
                    }

                    if (quantityOfLateralMoves >= MAX_MOVES_WITHOUT_IMPROVEMENT) {
                        quantityOfLateralMoves = 0;
                        break;
                    }
                }
            }

            maxIterations++;
        }

        int finalCost = sudokuBoard.calculateCostOfRepeatedNumbersInRowColumnOrSubGridOrEmptyCells(sudokuBoard.board);

        sudokuResponse.setResolutionMethod("Subida Encosta com Movimentos Laterais de tamanho "
                + sudokuBoard.board.length + "x" + sudokuBoard.board.length);
        sudokuResponse.setFinalCost(String.valueOf(finalCost));
        sudokuResponse.setInitialCost(String.valueOf(initialCost));
        sudokuResponse.setSteps(steps);
        sudokuResponse.setQuantityOfVisitedNodes(String.valueOf(visitedNodes));
        sudokuResponse.setComplexity(sudokuBoard.getSudokuBoardType().name());

        return sudokuResponse;
    }

    private static void copyBoard(int[][] source, int[][] dest) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, dest[i], 0, source.length);
        }
    }

}
