package com.example.asm_adnc.models;

public class AppEnroll {
    private Integer id, courseId, userId;

    public AppEnroll() {
    }

    public AppEnroll(Integer id, Integer courseId, Integer userId) {
        this.id = id;
        this.courseId = courseId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
