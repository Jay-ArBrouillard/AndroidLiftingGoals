package liftinggoals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RoutineWorkoutModel implements Parcelable {
    private int routineWorkoutId;
    private int routineId;
    private int workoutId;

    public RoutineWorkoutModel() {
    }

    protected RoutineWorkoutModel(Parcel in) {
        routineWorkoutId = in.readInt();
        routineId = in.readInt();
        workoutId = in.readInt();
    }

    public static final Creator<RoutineWorkoutModel> CREATOR = new Creator<RoutineWorkoutModel>() {
        @Override
        public RoutineWorkoutModel createFromParcel(Parcel in) {
            return new RoutineWorkoutModel(in);
        }

        @Override
        public RoutineWorkoutModel[] newArray(int size) {
            return new RoutineWorkoutModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(routineWorkoutId);
        dest.writeInt(routineId);
        dest.writeInt(workoutId);
    }
}
