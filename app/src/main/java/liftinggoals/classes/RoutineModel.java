package liftinggoals.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class RoutineModel implements Parcelable {
    private int imageResource;
    private String routineName;
    private String description;
    private Date lastPerformed;
    private ArrayList<WorkoutModel> workouts;

    public RoutineModel() {
    }

    public RoutineModel(String routineName, String description, ArrayList<WorkoutModel> workouts) {
        this.routineName = routineName;
        this.description = description;
        this.workouts = workouts;
    }

    protected RoutineModel(Parcel in) {
        imageResource = in.readInt();
        routineName = in.readString();
        description = in.readString();
        workouts = in.createTypedArrayList(WorkoutModel.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(routineName);
        dest.writeString(description);
        //dest.write(lastPerformed);
        dest.writeTypedList(workouts);
    }
}
