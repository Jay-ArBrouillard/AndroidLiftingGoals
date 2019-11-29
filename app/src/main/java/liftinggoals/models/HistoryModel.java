package liftinggoals.models;

import liftinggoals.calendar.Event;

public class HistoryModel {
    private String workoutName;
    private String exercises;
    private String date;
    private Event event;

    public HistoryModel(String workoutName, String exercises, String date) {
        this.workoutName = workoutName;
        this.exercises = exercises;
        this.date = date;
    }

    public HistoryModel(String workoutName, String exercises, String date, Event event) {
        this.workoutName = workoutName;
        this.exercises = exercises;
        this.date = date;
        this.event = event;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getExercises() {
        return exercises;
    }

    public void setExercises(String exercises) {
        this.exercises = exercises;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
