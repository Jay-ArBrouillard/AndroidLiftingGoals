package liftinggoals.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseModel implements Parcelable {
    private int exerciseId;
    private String exerciseName;

    public ExerciseModel() {
    }

    public ExerciseModel(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    protected ExerciseModel(Parcel in) {
        exerciseId = in.readInt();
        exerciseName = in.readString();
    }

    public static final Creator<ExerciseModel> CREATOR = new Creator<ExerciseModel>() {
        @Override
        public ExerciseModel createFromParcel(Parcel in) {
            return new ExerciseModel(in);
        }

        @Override
        public ExerciseModel[] newArray(int size) {
            return new ExerciseModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(exerciseId);
        dest.writeString(exerciseName);
    }
}
