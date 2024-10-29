package com.example.diarynotes;

public class Note {
    private String id;
    private String date;
    private String content;

    // Required empty constructor for Firebase deserialization
    public Note() {}

    public Note(String id, String date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getContent() { return content; }
}
