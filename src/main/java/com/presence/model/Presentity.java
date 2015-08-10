package com.presence.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by Suryaveer on 7/16/2015.
 */

@XmlRootElement
@JsonPropertyOrder({ "username", "domain", "event", "etag", "expires","receivedTime","body","extra_hdrs","sender" })
@JsonInclude(Include.NON_NULL)
public class Presentity implements Serializable {
    private Integer id;
    private String username;
    private String domain;
    private String event;
    private String etag;
    private Integer expires;
    @JsonProperty("received_time")
    private Integer receivedTime;
    private String body;
    @JsonProperty("extra_hdrs")
    private String extraHeaders;
    private String sender;

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
        result = 31 * result + receivedTime.hashCode();
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

    public Integer getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Integer receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtraHeaders() {
        return extraHeaders;
    }

    public void setExtraHeaders(String extraHeaders) {
        this.extraHeaders = extraHeaders;
    }

	@Override
	public String toString() {
		return "Presentity [" + (id != null ? "id=" + id + ", " : "")
				+ (username != null ? "username=" + username + ", " : "")
				+ (domain != null ? "domain=" + domain + ", " : "") + (event != null ? "event=" + event + ", " : "")
				+ (etag != null ? "etag=" + etag + ", " : "") + (expires != null ? "expires=" + expires + ", " : "")
				+ (receivedTime != null ? "receivedTime=" + receivedTime + ", " : "")
				+ (extraHeaders != null ? "extraHeaders=" + extraHeaders + ", " : "")
				+ (sender != null ? "sender=" + sender : "") + "]";
	}


}
