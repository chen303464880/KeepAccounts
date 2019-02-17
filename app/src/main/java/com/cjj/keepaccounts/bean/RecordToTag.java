package com.cjj.keepaccounts.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.cjj.keepaccounts.base.BaseEntity;
import com.cjj.keepaccounts.manager.DaoManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/21 16:31.
 */

@Entity(nameInDb = "record_to_tag")
public class RecordToTag extends BaseEntity implements Parcelable, Cloneable {

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
    @Property(nameInDb = "tag_id")
    private long tagId;
    @Property(nameInDb = "record_id")
    private long recordId;
    @Property(nameInDb = "user_id")
    private String userId;
    @Transient
    private RecordTag member;

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
        dest.writeLong(this.tagId);
        dest.writeLong(this.recordId);
        dest.writeString(this.userId);
        dest.writeParcelable(this.member, flags);
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

    public long getTagId() {
        return this.tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public RecordTag getMember() {
        if (member == null) {
            RecordTagDao targetDao = DaoManager.INSTANCE.getRecordTagDao();
            RecordTag memberNew = targetDao.queryBuilder()
                    .where(RecordTagDao.Properties.Uuid.eq(tagId)).unique();
            synchronized (this) {
                if (member == null) {
                    member = memberNew;
                }
            }
        }
        return member;
    }

    public RecordToTag() {
    }

    protected RecordToTag(Parcel in) {
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.uuid = in.readLong();
        this.createTime = in.readLong();
        this.listId = in.readLong();
        this.tagId = in.readLong();
        this.recordId = in.readLong();
        this.userId = in.readString();
        this.member = in.readParcelable(RecordTag.class.getClassLoader());
    }

    @Generated(hash = 854495164)
    public RecordToTag(String deviceId, int isDeleted, long mTime, long uuid,
                       long createTime, long listId, long tagId, long recordId,
                       String userId) {
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.uuid = uuid;
        this.createTime = createTime;
        this.listId = listId;
        this.tagId = tagId;
        this.recordId = recordId;
        this.userId = userId;
    }


    public static final Creator<RecordToTag> CREATOR = new Creator<RecordToTag>() {
        @Override
        public RecordToTag createFromParcel(Parcel source) {
            return new RecordToTag(source);
        }

        @Override
        public RecordToTag[] newArray(int size) {
            return new RecordToTag[size];
        }
    };
    @Override
    public RecordToTag clone() throws CloneNotSupportedException {
        return (RecordToTag) super.clone();
    }
}

