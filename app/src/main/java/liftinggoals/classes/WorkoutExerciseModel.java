package liftinggoals.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkoutExerciseModel implements Parcelable {
    private int workoutExerciseId;
    private int workoutId;
    private int exerciseId;
    private int minimumSets;
    private int minimumReps;
    private int maximumSets;
    private int maximumReps;
    private ExerciseModel exercise;

    public WorkoutExerciseModel()
    {

    }

    protected WorkoutExerciseModel(Parcel in) {
        workoutExerciseId = in.readInt();
        workoutId = in.readInt();
        exerciseId = in.readInt();
        minimumSets = in.readInt();
        minimumReps = in.readInt();
        maximumSets = in.readInt();
        maximumReps = in.readInt();
        exercise = in.readParcelable(ExerciseModel.class.getClassLoader());
    }

    public static final Creator<WorkoutExerciseModel> CREATOR = new Creator<WorkoutExerciseModel>() {
        @Override
        public WorkoutExerciseModel createFromParcel(Parcel in) {
            return new WorkoutExerciseModel(in);
        }

        @Override
        public WorkoutExerciseModel[] newArray(int size) {
            return new WorkoutExerciseModel[size];
        }
    };

    public int getWorkoutExerciseId() {
        return workoutExerciseId;
    }

    public void setWorkoutExerciseId(int workoutExerciseId) {
        this.workoutExerciseId = workoutExerciseId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getMinimumSets() {
        return minimumSets;
    }

    public void setMinimumSets(int minimumSets) {
        this.minimumSets = minimumSets;
    }

    public int getMinimumReps() {
        return minimumReps;
    }

    public void setMinimumReps(int minimumReps) {
        this.minimumReps = minimumReps;
    }

    public int getMaximumSets() {
        return maximumSets;
    }

    public void setMaximumSets(int maximumSets) {
        this.maximumSets = maximumSets;
    }

    public int getMaximumReps() {
        return maximumReps;
    }

    public void setMaximumReps(int maximumReps) {
        this.maximumReps = maximumReps;
    }

    public ExerciseModel getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseModel exercise) {
        this.exercise = exercise;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(workoutExerciseId);
        dest.writeInt(workoutId);
        dest.writeInt(exerciseId);
        dest.writeInt(minimumSets);
        dest.writeInt(minimumReps);
        dest.writeInt(maximumSets);
        dest.writeInt(maximumReps);
        dest.writeParcelable(exercise, flags);
    }
}
