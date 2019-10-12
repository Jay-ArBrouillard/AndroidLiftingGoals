package com.example.liftinggoals;

public class RoutineModel {
    private int mImageResource; //Not used
    private String routineName;
    private String description;

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
}
