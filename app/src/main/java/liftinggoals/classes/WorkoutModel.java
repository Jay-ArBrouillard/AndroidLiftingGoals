package liftinggoals.classes;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class WorkoutModel implements Parcelable {
    private int workoutId;
    private String workoutName;
    private String description;
    private double estimatedDuration;
    private int numberExercises;
    private ArrayList<WorkoutExerciseModel> exercises;
    private Color color;

    public WorkoutModel()
    {

    }

    public WorkoutModel(String workoutName, String description, double estimatedDuration) {

        this.workoutName = workoutName;
        this.description = description;
        this.estimatedDuration = estimatedDuration;
    }

    protected WorkoutModel(Parcel in) {
        workoutId = in.readInt();
        workoutName = in.readString();
        description = in.readString();
        estimatedDuration = in.readDouble();
        numberExercises = in.readInt();
        exercises = in.createTypedArrayList(WorkoutExerciseModel.CREATOR);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(double estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public ArrayList<WorkoutExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<WorkoutExerciseModel> exercises) {
        this.exercises = exercises;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNumberExercises() {
        return numberExercises;
    }

    public void setNumberExercises(int numberExercises) {
        this.numberExercises = numberExercises;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(workoutId);
        dest.writeString(workoutName);
        dest.writeString(description);
        dest.writeDouble(estimatedDuration);
        dest.writeInt(numberExercises);
        dest.writeTypedList(exercises);
    }
}
