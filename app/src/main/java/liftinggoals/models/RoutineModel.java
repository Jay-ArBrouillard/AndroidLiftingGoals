package liftinggoals.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class RoutineModel implements Parcelable {
    private int imageResource;
    private int routineId;
    private int userId;
    private String routineName;
    private String description;
    private ArrayList<WorkoutModel> workouts;
    private int defaultRoutine;

    public RoutineModel() {

    }
    public RoutineModel(int userId, String routineName, String description) {
        this.userId = userId;
        this.routineName = routineName;
        this.description = description;
    }

    protected RoutineModel(Parcel in) {
        imageResource = in.readInt();
        routineId = in.readInt();
        userId = in.readInt();
        routineName = in.readString();
        description = in.readString();
        workouts = in.createTypedArrayList(WorkoutModel.CREATOR);
        defaultRoutine = in.readInt();
    }

    public static final Creator<RoutineModel> CREATOR = new Creator<RoutineModel>() {
        @Override
        public RoutineModel createFromParcel(Parcel in) {
            return new RoutineModel(in);
        }

        @Override
        public RoutineModel[] newArray(int size) {
            return new RoutineModel[size];
        }
    };

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String name) {
        routineName = name;
    }

    public String getRoutineDescription() {
        return description;
    }

    public void setRoutineDescription(String description) {
        this.description = description;
    }


    public ArrayList<WorkoutModel> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<WorkoutModel> workouts) {
        this.workouts = workouts;
    }


    public int getDefaultRoutine() {
        return defaultRoutine;
    }

    public void setDefaultRoutine(int defaultRoutine) {
        this.defaultRoutine = defaultRoutine;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageResource);
        dest.writeInt(routineId);
        dest.writeInt(userId);
        dest.writeString(routineName);
        dest.writeString(description);
        dest.writeTypedList(workouts);
        dest.writeInt(defaultRoutine);
    }
}
