package liftinggoals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseModel implements Parcelable {
    private int exerciseId;
    private String exerciseName;
    private int userId;

    public ExerciseModel() {
    }

    public ExerciseModel(int exerciseId, String exerciseName)
    {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
    }

    public ExerciseModel(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    protected ExerciseModel(Parcel in) {
        exerciseId = in.readInt();
        exerciseName = in.readString();
        userId = in.readInt();
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String toString()
    {
        return exerciseName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(exerciseId);
        dest.writeString(exerciseName);
        dest.writeInt(userId);
    }
}
