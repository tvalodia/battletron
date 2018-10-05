package com.alltimeslucky.battletron.server.api.game;

public class GameDto {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "id=" + id +
                '}';
    }
}
