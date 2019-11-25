package liftinggoals.classes;

public class MusclesTrainedModel {
    private int musclesTrainedId;
    private int exerciseId;
    private String muscleGroup;

    public MusclesTrainedModel() {
    }

    public int getMusclesTrainedId() {
        return musclesTrainedId;
    }

    public void setMusclesTrainedId(int musclesTrainedId) {
        this.musclesTrainedId = musclesTrainedId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}