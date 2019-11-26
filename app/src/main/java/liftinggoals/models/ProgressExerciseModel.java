package liftinggoals.models;

public class ProgressExerciseModel {
    private String index;
    private String exerciseName;
    private String specs;
    private String time;

    public ProgressExerciseModel()
    {

    }

    public ProgressExerciseModel(String index, String exerciseName, String specs, String time) {
        this.index = index;
        this.exerciseName = exerciseName;
        this.specs = specs;
        this.time = time;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
