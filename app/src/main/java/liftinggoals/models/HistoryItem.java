package liftinggoals.models;

public class HistoryItem {
    private String workoutName;
    private String exercises;
    private String date;

    public HistoryItem(String workoutName, String exercises, String date) {
        this.workoutName = workoutName;
        this.exercises = exercises;
        this.date = date;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
