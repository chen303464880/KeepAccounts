package com.cjj.keepaccounts.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cjj.keepaccounts.base.BaseEntity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by CJJ on 2018/5/16 20:31.
 * * Copyright © 2015-2019 CJJ All rights reserved.
 */
@Entity(nameInDb = "app_config", active = true)
public class AppConfig extends BaseEntity implements Parcelable, Cloneable {

    @Property(nameInDb = "conf_val")
    private String confVal;
    @Property(nameInDb = "conf_val2")
    private String confVal2;
    @Property(nameInDb = "conf_val3")
    private String confVal3;
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
    @Property(nameInDb = "ctime")
    private long cTime;
    @Property(nameInDb = "conf_key")
    private String confKey;
    @Property(nameInDb = "user_id")
    private String userId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.confVal);
        dest.writeString(this.confVal2);
        dest.writeString(this.confVal3);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeLong(this.mTime);
        dest.writeLong(this.uuid);
        dest.writeLong(this.cTime);
        dest.writeString(this.confKey);
        dest.writeString(this.userId);
    }

    public AppConfig() {
    }

    protected AppConfig(Parcel in) {
        this.confVal = in.readString();
        this.confVal2 = in.readString();
        this.confVal3 = in.readString();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.uuid = in.readLong();
        this.cTime = in.readLong();
        this.confKey = in.readString();
        this.userId = in.readString();
    }

    @Generated(hash = 1588702370)
    public AppConfig(String confVal, String confVal2, String confVal3, String deviceId,
                     int isDeleted, long mTime, long uuid, long cTime, String confKey, String userId) {
        this.confVal = confVal;
        this.confVal2 = confVal2;
        this.confVal3 = confVal3;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.uuid = uuid;
        this.cTime = cTime;
        this.confKey = confKey;
        this.userId = userId;
    }


    public static final Creator<AppConfig> CREATOR = new Creator<AppConfig>() {
        @Override
        public AppConfig createFromParcel(Parcel source) {
            return new AppConfig(source);
        }

        @Override
        public AppConfig[] newArray(int size) {
            return new AppConfig[size];
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
    @Generated(hash = 471009766)
    private transient AppConfigDao myDao;

    @NonNull
    @Override
    public String toString() {
        return "AppConfig{" +
                ", confVal='" + confVal + '\'' +
                ", confVal2='" + confVal2 + '\'' +
                ", confVal3='" + confVal3 + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", isDeleted=" + isDeleted +
                ", mTime=" + mTime +
                ", uuid=" + uuid +
                ", cTime=" + cTime +
                ", confKey='" + confKey + '\'' +
                ", user_id='" + userId + '\'' +
                '}';
    }


    public String getConfVal() {
        return this.confVal;
    }

    public void setConfVal(String confVal) {
        this.confVal = confVal;
    }

    public String getConfVal2() {
        return this.confVal2;
    }

    public void setConfVal2(String confVal2) {
        this.confVal2 = confVal2;
    }

    public String getConfVal3() {
        return this.confVal3;
    }

    public void setConfVal3(String confVal3) {
        this.confVal3 = confVal3;
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

    public long getCTime() {
        return this.cTime;
    }

    public void setCTime(long cTime) {
        this.cTime = cTime;
    }

    public String getConfKey() {
        return this.confKey;
    }

    public void setConfKey(String confKey) {
        this.confKey = confKey;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeId() {
        String typeId = confKey.substring(confKey.lastIndexOf("=") + 1);
        if (typeId.length() > 5) {
            typeId = typeId + "0000";
        }
        return typeId;
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

    @Override
    public AppConfig clone() throws CloneNotSupportedException {
        return (AppConfig) super.clone();
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1869153177)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAppConfigDao() : null;
    }


}
