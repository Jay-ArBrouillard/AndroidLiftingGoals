package liftinggoals.classes;

import java.util.ArrayList;
import java.util.Date;

public class RoutineModel {
    private int imageResource;
    private String routineName;
    private String description;
    private Date lastPerformed;
    private ArrayList<WorkoutModel> workouts;

    public RoutineModel(String routineName, String description, ArrayList<WorkoutModel> workouts) {
        this.routineName = routineName;
        this.description = description;
        this.workouts = workouts;
    }

    public String getRoutineName() {
        return routineName;
    }

    public String getRoutineDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public Date getLastPerformed() {
        return lastPerformed;
    }

    public void setLastPerformed(Date lastPerformed) {
        this.lastPerformed = lastPerformed;
    }

    public ArrayList<WorkoutModel> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<WorkoutModel> workouts) {
        this.workouts = workouts;
    }
}
