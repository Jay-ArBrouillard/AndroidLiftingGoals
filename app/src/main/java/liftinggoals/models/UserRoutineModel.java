package liftinggoals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRoutineModel implements Parcelable {
    private int userRoutineId;
    private int userId;
    private int routineId;

    public UserRoutineModel()
    {
    }

    protected UserRoutineModel(Parcel in) {
        userRoutineId = in.readInt();
        userId = in.readInt();
        routineId = in.readInt();
    }

    public static final Creator<UserRoutineModel> CREATOR = new Creator<UserRoutineModel>() {
        @Override
        public UserRoutineModel createFromParcel(Parcel in) {
            return new UserRoutineModel(in);
        }

        @Override
        public UserRoutineModel[] newArray(int size) {
            return new UserRoutineModel[size];
        }
    };

    public int getUserRoutineId() {
        return userRoutineId;
    }

    public void setUserRoutineId(int userRoutineId) {
        this.userRoutineId = userRoutineId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userRoutineId);
        dest.writeInt(userId);
        dest.writeInt(routineId);
    }
}
