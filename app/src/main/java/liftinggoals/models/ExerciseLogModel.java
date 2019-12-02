package liftinggoals.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseLogModel implements Parcelable {
    private int userExerciseLogId;
    private int userRoutineId;
    private int workoutExerciseId;
    private int setPerformed;
    private int repsPerformed;
    private double intensity;
    private double rpe;
    private double restDuration;
    private String tempo;
    private String date;


    public ExerciseLogModel() {
    }

    protected ExerciseLogModel(Parcel in) {
        userExerciseLogId = in.readInt();
        userRoutineId = in.readInt();
        workoutExerciseId = in.readInt();
        setPerformed = in.readInt();
        repsPerformed = in.readInt();
        intensity = in.readDouble();
        rpe = in.readDouble();
        restDuration = in.readDouble();
        tempo = in.readString();
        date = in.readString();
    }

    public static final Creator<ExerciseLogModel> CREATOR = new Creator<ExerciseLogModel>() {
        @Override
        public ExerciseLogModel createFromParcel(Parcel in) {
            return new ExerciseLogModel(in);
        }

        @Override
        public ExerciseLogModel[] newArray(int size) {
            return new ExerciseLogModel[size];
        }
    };

    public int getUserExerciseLogId() {
        return userExerciseLogId;
    }

    public void setUserExerciseLogId(int userExerciseLogId) {
        this.userExerciseLogId = userExerciseLogId;
    }

    public int getUserRoutineId() {
        return userRoutineId;
    }

    public void setUserRoutineId(int userRoutineId) {
        this.userRoutineId = userRoutineId;
    }

    public int getWorkoutExerciseId() {
        return workoutExerciseId;
    }

    public void setWorkoutExerciseId(int workoutExerciseId) {
        this.workoutExerciseId = workoutExerciseId;
    }

    public int getSetPerformed() {
        return setPerformed;
    }

    public void setSetPerformed(int setPerformed) {
        this.setPerformed = setPerformed;
    }

    public int getRepsPerformed() {
        return repsPerformed;
    }

    public void setRepsPerformed(int repsPerformed) {
        this.repsPerformed = repsPerformed;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double getRpe() {
        return rpe;
    }

    public void setRpe(double rpe) {
        this.rpe = rpe;
    }

    public double getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(double restDuration) {
        this.restDuration = restDuration;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userExerciseLogId);
        dest.writeInt(userRoutineId);
        dest.writeInt(workoutExerciseId);
        dest.writeInt(setPerformed);
        dest.writeInt(repsPerformed);
        dest.writeDouble(intensity);
        dest.writeDouble(rpe);
        dest.writeDouble(restDuration);
        dest.writeString(tempo);
        dest.writeString(date);
    }
}
