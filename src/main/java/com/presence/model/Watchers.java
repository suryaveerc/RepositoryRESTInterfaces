package com.presence.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Suryaveer on 7/16/2015.
 */

@JsonPropertyOrder({ "presentityUri", "watcherUsername", "watcherDomain", "event", "status","reason","insertedTime"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Watchers implements Serializable {
    private Integer id;
    @JsonProperty("presentity_uri")
    private String presentityUri;
     @JsonProperty("watcher_username")
    private String watcherUsername;
      @JsonProperty("watcher_domain")
    private String watcherDomain;
    private String event;
    private Integer status;
    private String reason;
     @JsonProperty("inserted_time")
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
