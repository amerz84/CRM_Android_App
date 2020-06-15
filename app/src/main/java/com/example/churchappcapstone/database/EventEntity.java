package com.example.churchappcapstone.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "events")
public class EventEntity {
    @PrimaryKey(autoGenerate = true)
    private int eventId;
    private String eventTitle;
    private String eventNote;
    private Date eventStart;
    private Date eventEnd;

    @Ignore
    public EventEntity() {}

    public EventEntity(String eventTitle, String eventNote, Date eventStart, Date eventEnd) {
        this.eventTitle = eventTitle;
        this.eventNote = eventNote;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventNote() {
        return eventNote;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public Date getEventStart() {
        return eventStart;
    }

    public void setEventStart(Date eventStart) {
        this.eventStart = eventStart;
    }

    public Date getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Date eventEnd) {
        this.eventEnd = eventEnd;
    }
}
