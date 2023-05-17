package org.sudoku.response;

import org.sudoku.SudokuBoard;

import java.util.ArrayList;
import java.util.List;

public class SudokuResponse {

    private String resolutionMethod;
    private String complexity;
    private String finalCost;

    private String initialCost;
    private String quantityOfVisitedNodes;
    private String quantityExploredNodes;
    private String spentTime;
    private String initialTemperature;

    public String getInitialTemperature() {
        return initialTemperature;
    }

    public void setInitialTemperature(String initialTemperature) {
        this.initialTemperature = initialTemperature;
    }

    public String getFinalTemperature() {
        return finalTemperature;
    }

    public void setFinalTemperature(String finalTemperature) {
        this.finalTemperature = finalTemperature;
    }

    private String finalTemperature;

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    private String depth;

    public String getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(String initialCost) {
        this.initialCost = initialCost;
    }


    public String getQuantityExploredNodes() {
        return quantityExploredNodes;
    }

    public void setQuantityExploredNodes(String quantityExploredNodes) {
        this.quantityExploredNodes = quantityExploredNodes;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }

    private List<SudokuBoard> steps = new ArrayList<>();


    public String getResolutionMethod() {
        return resolutionMethod;
    }

    public void setResolutionMethod(String resolutionMethod) {
        this.resolutionMethod = resolutionMethod;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public String getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(String finalCost) {
        this.finalCost = finalCost;
    }

    public String getQuantityOfVisitedNodes() {
        return quantityOfVisitedNodes;
    }

    public void setQuantityOfVisitedNodes(String quantityOfVisitedNodes) {
        this.quantityOfVisitedNodes = quantityOfVisitedNodes;
    }

    public List<SudokuBoard> getSteps() {
        return steps;
    }

    public void setSteps(List<SudokuBoard> steps) {
        this.steps = steps;
    }

    public SudokuResponse() {
    }
}
