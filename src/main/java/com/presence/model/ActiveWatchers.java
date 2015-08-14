package com.presence.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

/**
 * Created by Suryaveer on 7/16/2015.
 */
@JsonPropertyOrder({"presentityURI", "watcherUsername", "watcherDomain", "toUser", "toDomain", "event", "eventId", "toTag", "fromTag", "callId", "localCseq", "remoteCseq", "contact", "recordRoute", "expires", "status", "reason", "version", "socketInfo", "localContact"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActiveWatchers implements Serializable {

    private Integer id;
    @JsonProperty("presentity_uri")
    private String presentityURI;

    @Override
    public String toString() {
        return "ActiveWatchers{" + "presentityURI=" + presentityURI + ", watcherUsername=" + watcherUsername + ", watcherDomain=" + watcherDomain + ", event=" + event + ", toTag=" + toTag + ", fromTag=" + fromTag + ", callId=" + callId + '}';
    }
    @JsonProperty("watcher_username")
    private String watcherUsername;
    @JsonProperty("watcher_domain")
    private String watcherDomain;
    @JsonProperty("to_user")
    private String toUser;
    @JsonProperty("to_domain")
    private String toDomain;
    private String event;
    @JsonProperty("event_id")
    private String eventId;
    @JsonProperty("to_tag")
    private String toTag;
    @JsonProperty("from_tag")
    private String fromTag;
    @JsonProperty("callid")
    private String callId;
    @JsonProperty("local_cseq")
    private Integer localCseq;
    @JsonProperty("remote_cseq")
    private Integer remoteCseq;
    private String contact;
    @JsonProperty("record_route")
    private String recordRoute;
    private Integer expires;
    private Integer status;
    private String reason;
    private Integer version;
    @JsonProperty("socket_info")
    private String socketInfo;
    @JsonProperty("local_contact")
    private String localContact;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActiveWatchers)) {
            return false;
        }
        return id.equals(((ActiveWatchers) o).id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getPresentityURI() {
        return presentityURI;
    }

    public void setPresentityURI(String presentityURI) {
        this.presentityURI = presentityURI;
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

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToDomain() {
        return toDomain;
    }

    public void setToDomain(String toDomain) {
        this.toDomain = toDomain;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public String getFromTag() {
        return fromTag;
    }

    public void setFromTag(String fromTag) {
        this.fromTag = fromTag;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Integer getLocalCseq() {
        return localCseq;
    }

    public void setLocalCseq(Integer localCseq) {
        this.localCseq = localCseq;
    }

    public Integer getRemoteCseq() {
        return remoteCseq;
    }

    public void setRemoteCseq(Integer remoteCseq) {
        this.remoteCseq = remoteCseq;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRecordRoute() {
        return recordRoute;
    }

    public void setRecordRoute(String recordRoute) {
        this.recordRoute = recordRoute;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSocketInfo() {
        return socketInfo;
    }

    public void setSocketInfo(String socketInfo) {
        this.socketInfo = socketInfo;
    }

    public String getLocalContact() {
        return localContact;
    }

    public void setLocalContact(String localContact) {
        this.localContact = localContact;
    }

}
