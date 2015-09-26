package com.example.kevin.srproject.db;

/**
 * Created by Kevin on 9/22/2015.
 */
public class DictionaryEntry {
    private int id;
    private String exerciseName;
    private String bodyArea;
    //Store reps and sets as strings, they may be input as arrays
    private String reps;
    private String sets;

    public DictionaryEntry(){}

    public DictionaryEntry(int id, String name, String area, String reps, String sets){
        this.id=id;
        this.exerciseName=name;
        this.bodyArea=area;
        this.reps=reps;
        this.sets=sets;
    }

    public DictionaryEntry(String name, String area, String reps, String sets){
        this.exerciseName=name;
        this.bodyArea=area;
        this.reps=reps;
        this.sets=sets;
    }

    public int getId(){ return this.id;}
    public void setId(int id){this.id=id;}

    public String getExerciseName() { return this.exerciseName;}
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public String getBodyArea() { return this.bodyArea; }
    public void setBodyArea(String bodyArea) { this.bodyArea = bodyArea; }

    public String getReps() { return this.reps; }
    public void setReps(String reps) { this.reps = reps; }

    public String getSets() { return this.sets; }
    public void setSets(String sets) { this.sets = sets; }
}
