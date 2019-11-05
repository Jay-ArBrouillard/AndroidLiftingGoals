package liftinggoals.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseModel {
    private int exerciseId;
    private String exerciseName;

    public ExerciseModel() {
    }

    public ExerciseModel(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
}
