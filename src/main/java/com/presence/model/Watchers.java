package com.presence.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Suryaveer on 7/16/2015.
 */
public class Watchers implements Serializable {
    private Integer id;
    private String presentityUri;
    private String watcherUsername;
    private String watcherDomain;
    private String event;
    private Integer status;
    private String reason;
    private Integer insertedTime;

    public Integer getInsertedTime() {
        return insertedTime;
    }

    public void setInsertedTime(Integer insertedTime) {
        this.insertedTime = insertedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Watchers)) return false;
        return Objects.equals(id, ((Watchers) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPresentityUri() {
        return presentityUri;
    }

    public void setPresentityUri(String presentityUri) {
        this.presentityUri = presentityUri;
    }

    public String getWatcherUsername() {
        return watcherUsername;
    }

    public void setWatcherUsername(String watcherUsername) {
        this.watcherUsername = watcherUsername;
    }

    public String getWatcherDomain() {
        return watcherDomain;
    }

    public void setWatcherDomain(String watcherDomain) {
        this.watcherDomain = watcherDomain;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
