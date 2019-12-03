package liftinggoals.calendar;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private int userId;
    private String EVENT, TIME, DATE, MONTH, YEAR, FULL_DATE;
    private String exercises;

    public Event()
    {

    }

    protected Event(Parcel in) {
        userId = in.readInt();
        EVENT = in.readString();
        TIME = in.readString();
        DATE = in.readString();
        MONTH = in.readString();
        YEAR = in.readString();
        FULL_DATE = in.readString();
        exercises = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getFULL_DATE() {
        return FULL_DATE;
    }

    public void setFULL_DATE(String FULL_DATE) {
        this.FULL_DATE = FULL_DATE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(EVENT);
        dest.writeString(TIME);
        dest.writeString(DATE);
        dest.writeString(MONTH);
        dest.writeString(YEAR);
        dest.writeString(FULL_DATE);
        dest.writeString(exercises);
    }
}
