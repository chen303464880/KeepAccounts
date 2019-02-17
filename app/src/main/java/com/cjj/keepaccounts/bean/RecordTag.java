package com.cjj.keepaccounts.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.cjj.keepaccounts.base.BaseEntity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/21 16:31.
 */

@Entity(nameInDb = "record_tag", active = true)
public class RecordTag extends BaseEntity implements Parcelable, Cloneable {
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "mtime")
    private long mTime;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "createTime")
    private long createTime;
    @Property(nameInDb = "list_id")
    private long listId;
    @Property(nameInDb = "tag_name")
    private String tagName;
    @Property(nameInDb = "user_id")
    private String userId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeLong(this.mTime);
        dest.writeLong(this.uuid);
        dest.writeLong(this.createTime);
        dest.writeLong(this.listId);
        dest.writeString(this.tagName);
        dest.writeString(this.userId);
    }


    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getMTime() {
        return this.mTime;
    }

    public void setMTime(long mTime) {
        this.mTime = mTime;
    }

    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getListId() {
        return this.listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public RecordTag() {
    }

    protected RecordTag(Parcel in) {
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.uuid = in.readLong();
        this.createTime = in.readLong();
        this.listId = in.readLong();
        this.tagName = in.readString();
        this.userId = in.readString();
    }

    @Generated(hash = 741340469)
    public RecordTag(String deviceId, int isDeleted, long mTime, long uuid, long createTime,
                     long listId, String tagName, String userId) {
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.uuid = uuid;
        this.createTime = createTime;
        this.listId = listId;
        this.tagName = tagName;
        this.userId = userId;
    }


    public static final Creator<RecordTag> CREATOR = new Creator<RecordTag>() {
        @Override
        public RecordTag createFromParcel(Parcel source) {
            return new RecordTag(source);
        }

        @Override
        public RecordTag[] newArray(int size) {
            return new RecordTag[size];
        }
    };
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2019159419)
    private transient RecordTagDao myDao;

    @Override
    public RecordTag clone() throws CloneNotSupportedException {
        return (RecordTag) super.clone();
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 636309718)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordTagDao() : null;
    }
}

