package liftinggoals.classes;

public class WorkoutExerciseModel {
    private int workoutExerciseId;
    private int workoutId;
    private int exerciseId;
    private int minimumSets;
    private int minimumReps;
    private int maximumSets;
    private int maximumReps;
    private ExerciseModel exercise;

    public WorkoutExerciseModel()
    {

    }

    public int getWorkoutExerciseId() {
        return workoutExerciseId;
    }

    public void setWorkoutExerciseId(int workoutExerciseId) {
        this.workoutExerciseId = workoutExerciseId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getMinimumSets() {
        return minimumSets;
    }

    public void setMinimumSets(int minimumSets) {
        this.minimumSets = minimumSets;
    }

    public int getMinimumReps() {
        return minimumReps;
    }

    public void setMinimumReps(int minimumReps) {
        this.minimumReps = minimumReps;
    }

    public int getMaximumSets() {
        return maximumSets;
    }

    public void setMaximumSets(int maximumSets) {
        this.maximumSets = maximumSets;
    }

    public int getMaximumReps() {
        return maximumReps;
    }

    public void setMaximumReps(int maximumReps) {
        this.maximumReps = maximumReps;
    }

    public ExerciseModel getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseModel exercise) {
        this.exercise = exercise;
    }
}
