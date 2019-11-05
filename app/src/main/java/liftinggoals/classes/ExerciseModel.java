package liftinggoals.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseModel implements Parcelable {
    private String exerciseName;
    private int minSets;
    private int maxSets;
    private int minReps;
    private int maxReps;
    private int restDuration;
    private int RPE;
    private int tempo;
    private int trainingMax;

    public ExerciseModel(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public ExerciseModel(int minSets, int minReps, int maxSets, int maxReps) {
        this.minSets = minSets;
        this.minReps = minReps;
        this.maxSets = maxSets;
        this.maxReps = maxReps;
    }

    public ExerciseModel(String exerciseName, int minSets, int maxSets, int minReps, int maxReps, int restDuration, int RPE, int temp, int trainingMax) {
        this.exerciseName = exerciseName;
        this.minSets = minSets;
        this.maxSets = maxSets;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.restDuration = restDuration;
        this.RPE = RPE;
        this.tempo = temp;
        this.trainingMax = trainingMax;
    }

    protected ExerciseModel(Parcel in) {
        exerciseName = in.readString();
        minSets = in.readInt();
        maxSets = in.readInt();
        minReps = in.readInt();
        maxReps = in.readInt();
        restDuration = in.readInt();
        RPE = in.readInt();
        tempo = in.readInt();
        trainingMax = in.readInt();
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

    public int getMinSets() {
        return minSets;
    }

    public void setMinSets(int minSets) {
        this.minSets = minSets;
    }

    public int getMaxSets() {
        return maxSets;
    }

    public void setMaxSets(int maxSets) {
        this.maxSets = maxSets;
    }

    public int getMinReps() {
        return minReps;
    }

    public void setMinReps(int minReps) {
        this.minReps = minReps;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(int restDuration) {
        this.restDuration = restDuration;
    }

    public int getRPE() {
        return RPE;
    }

    public void setRPE(int RPE) {
        this.RPE = RPE;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getTrainingMax() {
        return trainingMax;
    }

    public void setTrainingMax(int trainingMax) {
        this.trainingMax = trainingMax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exerciseName);
        dest.writeInt(minSets);
        dest.writeInt(maxSets);
        dest.writeInt(minReps);
        dest.writeInt(maxReps);
        dest.writeInt(restDuration);
        dest.writeInt(RPE);
        dest.writeInt(tempo);
        dest.writeInt(trainingMax);
    }
}
