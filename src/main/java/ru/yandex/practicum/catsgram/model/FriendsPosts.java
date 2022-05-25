package ru.yandex.practicum.catsgram.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FriendsPosts {
    private String sort;
    private Integer size;
    private List<String> friends;

    @JsonCreator
    public FriendsPosts(@JsonProperty("sort") String sort, @JsonProperty("size") Integer size, @JsonProperty("friends") List<String> friends) {
        this.sort = sort;
        this.size = size;
        this.friends = friends;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> emailList) {
        this.friends = emailList;
    }
}
