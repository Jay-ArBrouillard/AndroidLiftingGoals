package com.example.liftinggoals;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WorkoutModel implements Parcelable {
    private String workoutName;
    private int estimatedDuration;
    private ArrayList<ExerciseModel> exercises;
    private Color color;

    public WorkoutModel(String workoutName) {
        this.workoutName = workoutName;
    }

    public WorkoutModel(String workoutName, int estimatedDuration, ArrayList<ExerciseModel> exercises) {
        this.workoutName = workoutName;
        this.estimatedDuration = estimatedDuration;
        this.exercises = exercises;
    }

    protected WorkoutModel(Parcel in) {
        workoutName = in.readString();
        estimatedDuration = in.readInt();
        exercises = in.createTypedArrayList(ExerciseModel.CREATOR);
    }

    public static final Creator<WorkoutModel> CREATOR = new Creator<WorkoutModel>() {
        @Override
        public WorkoutModel createFromParcel(Parcel in) {
            return new WorkoutModel(in);
        }

        @Override
        public WorkoutModel[] newArray(int size) {
            return new WorkoutModel[size];
        }
    };

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public ArrayList<ExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ExerciseModel> exercises) {
        this.exercises = exercises;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workoutName);
        dest.writeInt(estimatedDuration);
        dest.writeTypedList(exercises);
    }
}
