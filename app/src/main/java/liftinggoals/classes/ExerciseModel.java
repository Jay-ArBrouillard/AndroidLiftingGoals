package liftinggoals.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseModel implements Parcelable {
    private String exerciseName;
    private String minSets;
    private String maxSets;
    private String minReps;
    private String maxReps;
    private String restDuration;
    private String RPE;
    private String tempo;
    private String trainingMax;

    public ExerciseModel() {

    }

    public ExerciseModel(String exerciseName) {
        this.exerciseName = exerciseName;
    }


    protected ExerciseModel(Parcel in) {
        exerciseName = in.readString();
        minSets = in.readString();
        maxSets = in.readString();
        minReps = in.readString();
        maxReps = in.readString();
        restDuration = in.readString();
        RPE = in.readString();
        tempo = in.readString();
        trainingMax = in.readString();
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

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMinSets() {
        return minSets;
    }

    public void setMinSets(String minSets) {
        this.minSets = minSets;
    }

    public String getMaxSets() {
        return maxSets;
    }

    public void setMaxSets(String maxSets) {
        this.maxSets = maxSets;
    }

    public String getMinReps() {
        return minReps;
    }

    public void setMinReps(String minReps) {
        this.minReps = minReps;
    }

    public String getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(String maxReps) {
        this.maxReps = maxReps;
    }

    public String getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(String restDuration) {
        this.restDuration = restDuration;
    }

    public String getRPE() {
        return RPE;
    }

    public void setRPE(String RPE) {
        this.RPE = RPE;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getTrainingMax() {
        return trainingMax;
    }

    public void setTrainingMax(String trainingMax) {
        this.trainingMax = trainingMax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exerciseName);
        dest.writeString(minSets);
        dest.writeString(maxSets);
        dest.writeString(minReps);
        dest.writeString(maxReps);
        dest.writeString(restDuration);
        dest.writeString(RPE);
        dest.writeString(tempo);
        dest.writeString(trainingMax);
    }
}
