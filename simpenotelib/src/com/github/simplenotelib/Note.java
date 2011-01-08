package com.github.simplenotelib;

import java.util.Date;
/*
 * This POJO is based on the JSON document in the API2.
 * {
		key	:	(string,	note	identifier,	created	by	server), 
		deleted	:	(bool,	whether	or	not	note	is	in	trash), 
		modifydate	:	(last	modified	date,	in	seconds	since	epoch), 
		createdate	:	(note	created	date,	in	seconds	since	epoch), 
		syncnum	:	(integer,	number	set	by	server,	track	note	changes), 
		version	:	(integer,	number	set	by	server,	track	note	content	changes), 
		minversion	:	(integer,	number	set	by	server,	minimum	version	available	for	note), 
		sharekey	:	(string,	shared	note	identifier), 
		publishkey	:	(string,	published	note	identifier), 
		systemtags	:	[	(Array	of	strings,	some	set	by	server)	], 
		tags	:	[	(Array	of	strings)	], 
		content	:	(string,	data	content)
	}
*/
import java.util.List;

public class Note {
	private String key;
	private boolean deleted;
	private Date modifydate;
	private Date createdate;
	private int syncnum;
	private int minversion;
	private String sharekey;
	private String publishkey;
	private List<String> systemtags;
	private List<String> tags;
	private String content;
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}
	public Date getModifydate() {
		return modifydate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setSyncnum(int syncnum) {
		this.syncnum = syncnum;
	}
	public int getSyncnum() {
		return syncnum;
	}
	public void setMinversion(int minversion) {
		this.minversion = minversion;
	}
	public int getMinversion() {
		return minversion;
	}
	public void setSharekey(String sharekey) {
		this.sharekey = sharekey;
	}
	public String getSharekey() {
		return sharekey;
	}
	public void setPublishkey(String publishkey) {
		this.publishkey = publishkey;
	}
	public String getPublishkey() {
		return publishkey;
	}
	public void setSystemtags(List<String> systemtags) {
		this.systemtags = systemtags;
	}
	public List<String> getSystemtags() {
		return systemtags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}

}
