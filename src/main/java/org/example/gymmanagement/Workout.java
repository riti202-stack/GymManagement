package org.example.gymmanagement;

import javafx.beans.property.*;

public class Workout {

    private final StringProperty exercise;
    private final IntegerProperty sets;
    private final IntegerProperty reps;
    private final StringProperty day;

    public Workout(String exercise, int sets, int reps, String day) {
        this.exercise = new SimpleStringProperty(exercise);
        this.sets = new SimpleIntegerProperty(sets);
        this.reps = new SimpleIntegerProperty(reps);
        this.day = new SimpleStringProperty(day);
    }

    public StringProperty exerciseProperty() {
        return exercise;
    }

    public IntegerProperty setsProperty() {
        return sets;
    }

    public IntegerProperty repsProperty() {
        return reps;
    }

    public StringProperty dayProperty() {
        return day;
    }
}
