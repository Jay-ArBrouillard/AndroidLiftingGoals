package com.example.liftinggoals;

import java.util.Date;
import java.util.List;

public class RoutineModel {
    private int imageResource; //Not used
    private String routineName;
    private String description;
    private Date lastPerformed;
    private List<WorkoutModel> workouts;

    public RoutineModel(String mText1, String mText2) {
        this.routineName = mText1;
        this.description = mText2;
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

    public List<WorkoutModel> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutModel> workouts) {
        this.workouts = workouts;
    }
}
