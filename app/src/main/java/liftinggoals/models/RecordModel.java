package liftinggoals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordModel implements Parcelable {
    private int recordId;
    private int userId;
    private int exerciseId;
    private double intensity;
    private int repsPerformed;
    //For RepRecord
    private String date;

    public RecordModel()
    {
    }

    public RecordModel(int recordId, int userId, int exerciseId, double weight, int reps, String format)
    {
        this.recordId = recordId;
        this.userId = userId;
        this.exerciseId = exerciseId;
        intensity = weight;
        repsPerformed = reps;
        date = format;
    }

    protected RecordModel(Parcel in) {
        recordId = in.readInt();
        userId = in.readInt();
        exerciseId = in.readInt();
        intensity = in.readDouble();
        repsPerformed = in.readInt();
        date = in.readString();
    }

    public static final Creator<RecordModel> CREATOR = new Creator<RecordModel>() {
        @Override
        public RecordModel createFromParcel(Parcel in) {
            return new RecordModel(in);
        }

        @Override
        public RecordModel[] newArray(int size) {
            return new RecordModel[size];
        }
    };

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public int getRepsPerformed() {
        return repsPerformed;
    }

    public void setRepsPerformed(int repsPerformed) {
        this.repsPerformed = repsPerformed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recordId);
        dest.writeInt(userId);
        dest.writeInt(exerciseId);
        dest.writeDouble(intensity);
        dest.writeInt(repsPerformed);
        dest.writeString(date);
    }
}
