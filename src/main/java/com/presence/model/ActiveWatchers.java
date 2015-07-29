package com.presence.model;

import java.io.Serializable;

/**
 * Created by Suryaveer on 7/16/2015.
 */
public class ActiveWatchers implements Serializable {
    private Integer id;
    private String presentityURI;
    private String watcherUsername;
    private String watcherDomain;
    private String toUser;
    private String toDomain;
    private String event;
    private String eventId;
    private String toTag;
    private String fromTag;
    private String callId;
    private Integer localCseq;
    private Integer remoteCseq;
    private String contact;
    private String recordRoute;
    private Integer expires;
    private Integer status;
    private String reason;
    private Integer version;
    private String socketInfo;
    private String localContact;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActiveWatchers)) return false;
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
