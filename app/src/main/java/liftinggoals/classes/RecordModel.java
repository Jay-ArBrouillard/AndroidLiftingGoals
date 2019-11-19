package liftinggoals.classes;

public class RecordModel {
    private int recordId;
    private int userId;
    private int exerciseId;
    private double intensity;
    private int repsPerformed;
    //For RepRecord
    private String date;

    public RecordModel() {
    }

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
}
