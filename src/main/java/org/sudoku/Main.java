package org.sudoku;

import org.sudoku.response.SudokuResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        Search search = new Search();

        long start, finish, timeElapsed;

        SudokuBoard sudokuBoard;
        Type type;
        String fileName;
        int option;

        Random random = new Random();
        do {
            menu();
            Scanner input = new Scanner(System.in);
            option = input.nextInt();
            switch (option) {
                case 1 -> {
                    System.out.println("Busca Profundidade Iterativa:");

                    type = chooseSudokuType(input);

                    System.out.println("Entre com o nome do arquivo do jogo sudoku:");
                    fileName = input.next();

                    sudokuBoard = readSudokuGameFromFile(fileName, type);

                    System.out.println("-----------Tabuleiro inicial------------");
                    System.out.println("Nível do jogo: " + (type.equals(Type.COMPLEX) ? "COMPLEXO" : "SIMPLES"));
                    sudokuBoard.printBoard();

                    start = System.currentTimeMillis();

                    SudokuResponse sudokuResponse;

                    sudokuResponse = search.iterativeDepthSearch(10000, sudokuBoard);

                    finish = System.currentTimeMillis();
                    timeElapsed = finish - start;

                    System.out.println("-----------Tabuleiro final------------");
                    sudokuResponse.getSteps().get(sudokuResponse.getSteps().size()-1).printBoard();

                    System.out.println("\nTempo execução: " + (timeElapsed / 1000d) + " segundos\n");

                    int randomNumber = random.nextInt();

                    String fileOutputName = "Busca-Profundidade-Iterativa-" + LocalDateTime.now().toLocalDate() + randomNumber;
                    String outputPath = "saida/".concat(fileOutputName).concat(".txt");
                    System.out.println("\nFile output name: "+fileOutputName+"\n");

                    sudokuResponse.setSpentTime(String.valueOf(timeElapsed / 1000d));
                    FileHandler.writeToFile(outputPath, sudokuResponse);

                }
                case 2 -> {
                    System.out.println("Greedy 1:");

                    type = chooseSudokuType(input);

                    System.out.println("Entre com o nome do arquivo do jogo sudoku:");
                    fileName = input.next();

                    sudokuBoard = readSudokuGameFromFile(fileName, type);

                    System.out.println("-----------Tabuleiro inicial------------");
                    System.out.println("Nível do jogo: " + (type.equals(Type.COMPLEX) ? "COMPLEXO" : "SIMPLES"));

                    sudokuBoard.printBoard();

                    start = System.currentTimeMillis();
                    SudokuResponse sudokuResponse;

                    sudokuResponse = search.greedySearch(sudokuBoard);

                    //System.out.println("\nGreedy 2:");
                    //sudokuResponse = search.greedySearch2(sudokuBoard);

                    finish = System.currentTimeMillis();
                    timeElapsed = finish - start;

                    System.out.println("-----------Tabuleiro final------------");
                    sudokuResponse.getSteps().get(sudokuResponse.getSteps().size()-1).printBoard();
                    System.out.println("\nTempo execução: " + (timeElapsed / 1000d) + " segundos");

                    int randomNumber = random.nextInt();

                    String fileOutputName = "Busca-Gulosa-" + LocalDateTime.now().toLocalDate() + randomNumber;
                    String outputPath = "saida/".concat(fileOutputName).concat(".txt");
                    System.out.println("\nFile output name: "+fileOutputName+"\n");

                    sudokuResponse.setSpentTime(String.valueOf(timeElapsed / 1000d));
                    FileHandler.writeToFile(outputPath, sudokuResponse);
                }
                case 3 -> {
                    System.out.println("A Estrela (A*):");
                    type = chooseSudokuType(input);
                    System.out.println("Entre com o nome do arquivo do jogo sudoku:");
                    fileName = input.next();
                    sudokuBoard = readSudokuGameFromFile(fileName, type);
                    System.out.println("Nível do jogo: " + (type.equals(Type.COMPLEX) ? "COMPLEXO" : "SIMPLES"));
                    System.out.println("-----------Tabuleiro inicial------------");
                    sudokuBoard.printBoard();
                    start = System.currentTimeMillis();

                    SudokuResponse sudokuResponse;

                    sudokuResponse = search.aStarSearch(sudokuBoard);

                    finish = System.currentTimeMillis();
                    timeElapsed = finish - start;

                    System.out.println("-----------Tabuleiro final------------");
                    sudokuResponse.getSteps().get(sudokuResponse.getSteps().size()-1).printBoard();
                    System.out.println("\nTempo execução: " + (timeElapsed / 1000d) + " segundos");

                    int randomNumber = random.nextInt();

                    String fileOutputName = "A-Estrela-"+ LocalDateTime.now().toLocalDate()+randomNumber;
                    String outputPath = "saida/".concat(fileOutputName).concat(".txt");
                    System.out.println("\nFile output name: "+fileOutputName+"\n");

                    sudokuResponse.setSpentTime(String.valueOf(timeElapsed / 1000d));
                    FileHandler.writeToFile(outputPath, sudokuResponse);
                }
                case 4 -> {
                    System.out.println("Subida de Encosta com Movimentos Laterais: ");

                    type = chooseSudokuType(input);

                    System.out.println("Entre com o nome do arquivo do jogo sudoku:");
                    fileName = input.next();

                    sudokuBoard = readSudokuGameFromFile(fileName, type);

                    System.out.println("Nível do jogo: " + (type.equals(Type.COMPLEX) ? "COMPLEXO" : "SIMPLES"));

                    System.out.println("-----------Tabuleiro inicial------------");
                    sudokuBoard.printBoard();

                    start = System.currentTimeMillis();
                    SudokuResponse sudokuResponse;

                    sudokuResponse = search.hillClimbingWithLateralMoves(sudokuBoard);

                    finish = System.currentTimeMillis();
                    timeElapsed = finish - start;

                    System.out.println("-----------Tabuleiro final------------");
                    sudokuResponse.getSteps().get(sudokuResponse.getSteps().size()-1).printBoard();
                    System.out.println("\nTempo execução: " + (timeElapsed / 1000d) + " segundos");

                    int randomNumber = random.nextInt();

                    String fileOutputName = "Subida-Encosta-"+ LocalDateTime.now().toLocalDate()+randomNumber;
                    String outputPath = "saida/".concat(fileOutputName).concat(".txt");
                    System.out.println("\nFile output name: "+fileOutputName+"\n");

                    sudokuResponse.setSpentTime(String.valueOf(timeElapsed / 1000d));
                    FileHandler.writeToFile(outputPath, sudokuResponse);

                }
                case 5 -> {
                    System.out.println("\nTêmpera simulada: ");

                    type = chooseSudokuType(input);

                    System.out.println("Entre com o nome do arquivo do jogo sudoku:");
                    fileName = input.next();

                    sudokuBoard = readSudokuGameFromFile(fileName, type);

                    System.out.println("Nível do jogo: " + (type.equals(Type.COMPLEX) ? "COMPLEXO" : "SIMPLES"));

                    System.out.println("-----------Tabuleiro inicial------------");
                    sudokuBoard.printBoard();

                    start = System.currentTimeMillis();

                    SudokuResponse sudokuResponse;

                    sudokuResponse = search.simulatedAnnealing(sudokuBoard);

                    finish = System.currentTimeMillis();
                    timeElapsed = finish - start;

                    System.out.println("-----------Tabuleiro final------------");
                    sudokuResponse.getSteps().get(sudokuResponse.getSteps().size()-1).printBoard();
                    System.out.println("\nTempo execução: " + (timeElapsed / 1000d) + " segundos");
                    int randomNumber = random.nextInt();

                    String fileOutputName = "Têmpera-Simulada-"+ LocalDateTime.now().toLocalDate()+randomNumber;
                    String outputPath = "saida/".concat(fileOutputName).concat(".txt");
                    System.out.println("\nFile output name: "+fileOutputName+"\n");

                    sudokuResponse.setSpentTime(String.valueOf(timeElapsed / 1000d));
                    FileHandler.writeToFile(outputPath, sudokuResponse);
                }
            }
        } while(option != 6);

    }

    private static SudokuBoard readSudokuGameFromFile(String fileName, Type type) throws IOException {

        Tuple tuple = FileHandler.readFile(fileName);

        String strTable = tuple.strTable;
        int n = tuple.numberOfLines;


        SudokuBoard sudokuBoard = new SudokuBoard(n, type);
        sudokuBoard.populateBoardByTxtFile(strTable);

        return sudokuBoard;
    }

    private static Type chooseSudokuType(Scanner input) {
        System.out.println("Escolha o método de resolução (COMPLEXO/SIMPLES):");
        System.out.println("1: SIMPLES");
        System.out.println("2: COMPLEXO");

        int subOption = input.nextInt();
        if (subOption == 2) {
            return Type.COMPLEX;
        }
        return Type.SIMPLE;
    }


    private static void menu() {
        System.out.println("----------------------------------------------------------------------\n");
        System.out.println("Resolução do jogo Sudoku com algoritmos de Inteligência Artificial\n");
        System.out.println("Escolha a opção que deseja resolver o jogo:\n");
        System.out.println("1- Busca em profundidade iterativa\n");
        System.out.println("2- Busca gulosa\n");
        System.out.println("3- Busca A*\n");
        System.out.println("4- Subida de Encosta com Movimentos Laterais\n");
        System.out.println("5- Têmpera Simulada\n");
        System.out.println("6- Sair\n");
        System.out.println("----------------------------------------------------------------------\n");
    }
}