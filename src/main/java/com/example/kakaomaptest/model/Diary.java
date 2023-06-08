package com.example.kakaomaptest.model;

public class Diary {
    private static int nextId = 1; // 다음 일련번호를 저장
    private int id; // 다이어리의 일련번호
    private String date; //날짜
    private String title; //제목
    private double latitude; //위도
    private double longitude; //경도
    private String placeName; //장소 이름
    private String description; //설명


    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public static void setNextId(int nextId) {
        Diary.nextId = nextId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Diary createDiary(String date, String title, String description, double latitude, double longitude, String placeName) {
        Diary diary = new Diary();
        diary.setId(nextId++);
        diary.setDate(date);
        diary.setTitle(title);
        diary.setDescription(description);
        diary.setLatitude(latitude);
        diary.setLongitude(longitude);
        diary.setPlaceName(placeName);
        return diary;
    }
}
