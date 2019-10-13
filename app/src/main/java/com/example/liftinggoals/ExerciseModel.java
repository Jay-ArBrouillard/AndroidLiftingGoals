package com.example.liftinggoals;

public class ExerciseModel {
    private String exerciseName;
    private int minSets;
    private int maxSets;
    private int minReps;
    private int maxReps;
    private int restDuration;
    private int RPE;
    private int tempo;
    private int trainingMax;

    public ExerciseModel(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public ExerciseModel(String exerciseName, int minSets, int minReps, int maxReps, int RPE, int trainingMax) {
        this.exerciseName = exerciseName;
        this.minSets = minSets;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.RPE = RPE;
        this.trainingMax = trainingMax;
    }

    public ExerciseModel(String exerciseName, int minSets, int maxSets, int minReps, int maxReps, int restDuration, int RPE, int temp, int trainingMax) {
        this.exerciseName = exerciseName;
        this.minSets = minSets;
        this.maxSets = maxSets;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.restDuration = restDuration;
        this.RPE = RPE;
        this.tempo = temp;
        this.trainingMax = trainingMax;
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

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getTrainingMax() {
        return trainingMax;
    }

    public void setTrainingMax(int trainingMax) {
        this.trainingMax = trainingMax;
    }
}
