package liftinggoals.classes;

public class RoutineWorkoutModel {
    private int routine_workout_id;
    private int routine_id;
    private int workout_id;

    public RoutineWorkoutModel() {
    }

    public int getRoutine_workout_id() {
        return routine_workout_id;
    }

    public void setRoutine_workout_id(int routine_workout_id) {
        this.routine_workout_id = routine_workout_id;
    }

    public int getRoutine_id() {
        return routine_id;
    }

    public void setRoutine_id(int routine_id) {
        this.routine_id = routine_id;
    }

    public int getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(int workout_id) {
        this.workout_id = workout_id;
    }
}
