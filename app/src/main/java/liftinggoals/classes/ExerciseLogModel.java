package liftinggoals.classes;

public class ExerciseLogModel {
    private int userExerciseLogId;
    private int userRoutineId;
    private int workoutExeriseId;
    private int setPerformed;
    private int repsPerformed;
    private double intensity;
    private double rpe;
    private double restDuration;
    private String tempo;

    public ExerciseLogModel() {
    }

    public int getUserExerciseLogId() {
        return userExerciseLogId;
    }

    public void setUserExerciseLogId(int userExerciseLogId) {
        this.userExerciseLogId = userExerciseLogId;
    }

    public int getUserRoutineId() {
        return userRoutineId;
    }

    public void setUserRoutineId(int userRoutineId) {
        this.userRoutineId = userRoutineId;
    }

    public int getWorkoutExeriseId() {
        return workoutExeriseId;
    }

    public void setWorkoutExeriseId(int workoutExeriseId) {
        this.workoutExeriseId = workoutExeriseId;
    }

    public int getSetPerformed() {
        return setPerformed;
    }

    public void setSetPerformed(int setPerformed) {
        this.setPerformed = setPerformed;
    }

    public int getRepsPerformed() {
        return repsPerformed;
    }

    public void setRepsPerformed(int repsPerformed) {
        this.repsPerformed = repsPerformed;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getRpe() {
        return rpe;
    }

    public void setRpe(double rpe) {
        this.rpe = rpe;
    }

    public double getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(double restDuration) {
        this.restDuration = restDuration;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }
}
