package com.example.liftinggoals;

public class ExerciseModel {
    private String exerciseName;
    private int minSets;
    private int maxSets;
    private int minReps;
    private int maxReps;
    private int restDuration;
    private int RPE;
    private int temp;
    private int trainingMax;

    public ExerciseModel() {
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getMinSets() {
        return minSets;
    }

    public void setMinSets(int minSets) {
        this.minSets = minSets;
    }

    public int getMaxSets() {
        return maxSets;
    }

    public void setMaxSets(int maxSets) {
        this.maxSets = maxSets;
    }

    public int getMinReps() {
        return minReps;
    }

    public void setMinReps(int minReps) {
        this.minReps = minReps;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(int restDuration) {
        this.restDuration = restDuration;
    }

    public int getRPE() {
        return RPE;
    }

    public void setRPE(int RPE) {
        this.RPE = RPE;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTrainingMax() {
        return trainingMax;
    }

    public void setTrainingMax(int trainingMax) {
        this.trainingMax = trainingMax;
    }
}
