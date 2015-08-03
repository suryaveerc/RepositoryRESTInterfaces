package com.presence.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by Suryaveer on 7/16/2015.
 */

@XmlRootElement

public class Presentity implements Serializable {
    private Integer id;
    private String username="";
    private String domain="";
    private String event="";
    private String etag="";
    private Integer expires=new Integer("-1");
    private Integer received_time=new Integer("-1");
    private String body="";
    private String extra_hdrs="";
    private String sender="";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Presentity)) return false;
        return (!id.equals(((Presentity) o).id));

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + etag.hashCode();
        result = 31 * result + received_time.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public Integer getReceived_time() {
        return received_time;
    }

    public void setReceived_time(Integer received_time) {
        this.received_time = received_time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtra_hdrs() {
        return extra_hdrs;
    }

    public void setExtra_hdrs(String extra_hdrs) {
        this.extra_hdrs = extra_hdrs;
    }

	@Override
	public String toString() {
		return "Presentity [" + (id != null ? "id=" + id + ", " : "")
				+ (username != null ? "username=" + username + ", " : "")
				+ (domain != null ? "domain=" + domain + ", " : "") + (event != null ? "event=" + event + ", " : "")
				+ (etag != null ? "etag=" + etag + ", " : "") + (expires != null ? "expires=" + expires + ", " : "")
				+ (received_time != null ? "received_time=" + received_time + ", " : "")
				+ (extra_hdrs != null ? "extra_hdrs=" + extra_hdrs + ", " : "")
				+ (sender != null ? "sender=" + sender : "") + "]";
	}


}
