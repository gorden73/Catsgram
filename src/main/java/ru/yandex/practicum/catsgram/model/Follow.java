package ru.yandex.practicum.catsgram.model;

import java.util.List;

public class Follow {
    private Integer id;
    private String userId;
    private String followId;

    public Follow(Integer id, String userId, String followId) {
        this.id = id;
        this.userId = userId;
        this.followId = followId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }
}
