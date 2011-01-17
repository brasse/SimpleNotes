package com.github.simplenotes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Note {
    private long id;
    private String key;
    private boolean deleted;
    private Date modifyDate;
    private Date createDate;
    private int syncNum;
    private int version;
    private int minVersion;
    private String shareKey;
    private String publishKey;
    private List<String> systemTags;
    private List<String> tags;
    private String content;

    public Note() {
        deleted = false;
        systemTags = new ArrayList<String>();
        tags = new ArrayList<String>();
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public int getSyncNum() {
        return syncNum;
    }
    public void setSyncNum(int syncNum) {
        this.syncNum = syncNum;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public int getMinVersion() {
        return minVersion;
    }
    public void setMinVersion(int minVersion) {
        this.minVersion = minVersion;
    }
    public String getShareKey() {
        return shareKey;
    }
    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }
    public String getPublishKey() {
        return publishKey;
    }
    public void setPublishKey(String publishKey) {
        this.publishKey = publishKey;
    }
    public List<String> getSystemTags() {
        return systemTags;
    }
    public void setSystemTags(List<String> systemTags) {
        this.systemTags = systemTags;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPinned() {
        return systemTags.contains("pinned");
    }
    public boolean isUnread() {
        return systemTags.contains("unread");
    }
}
