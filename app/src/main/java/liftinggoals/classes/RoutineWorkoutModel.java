package liftinggoals.classes;

public class RoutineWorkoutModel {
    private int routineWorkoutId;
    private int routineId;
    private int workoutId;

    public RoutineWorkoutModel() {
    }

    public int getRoutineWorkoutId() {
        return routineWorkoutId;
    }

    public void setRoutineWorkoutId(int routineWorkoutId) {
        this.routineWorkoutId = routineWorkoutId;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }
}
