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
 * @author CJJ
 * Created by CJJ on 2017/11/13 16:03.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
@Entity(nameInDb = "account_type", active = true)
public class AccountType extends BaseEntity implements Parcelable, Cloneable {

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
    @Property(nameInDb = "desc")
    private String desc;
    @Property(nameInDb = "idImg")
    private int idImg;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "indexNum")
    private int indexNum;
    @Property(nameInDb = "typeId")
    @NotNull
    private int typeId;
    @Property(nameInDb = "user_id")
    private String userId;




    @Generated(hash = 551127513)
    public AccountType() {
    }


    @NonNull
    @Override
    public String toString() {
        return "AccountType{" +
                ", deviceId='" + deviceId + '\'' +
                ", is_deleted=" + isDeleted +
                ", mTime=" + mTime +
                ", uuid=" + uuid +
                ", desc='" + desc + '\'' +
                ", idImg=" + idImg +
                ", name='" + name + '\'' +
                ", indexNum=" + indexNum +
                ", typeId=" + typeId +
                ", userId='" + userId + '\'' +
                '}';
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


    public long getmTime() {
        return this.mTime;
    }


    public void setmTime(long mTime) {
        this.mTime = mTime;
    }


    public long getUuid() {
        return this.uuid;
    }


    public void setUuid(long uuid) {
        this.uuid = uuid;
    }


    public String getDesc() {
        return this.desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }


    public int getIdImg() {
        return this.idImg;
    }


    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getIndexNum() {
        return this.indexNum;
    }


    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }


    public int getTypeId() {
        return this.typeId;
    }


    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }


    public String getUserId() {
        return this.userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

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
        dest.writeString(this.desc);
        dest.writeInt(this.idImg);
        dest.writeString(this.name);
        dest.writeInt(this.indexNum);
        dest.writeInt(this.typeId);
        dest.writeString(this.userId);
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


    protected AccountType(Parcel in) {
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.uuid = in.readLong();
        this.desc = in.readString();
        this.idImg = in.readInt();
        this.name = in.readString();
        this.indexNum = in.readInt();
        this.typeId = in.readInt();
        this.userId = in.readString();
    }


    @Generated(hash = 1515917882)
    public AccountType(String deviceId, int isDeleted, long mTime, long uuid, String desc,
                       int idImg, String name, int indexNum, int typeId, String userId) {
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.uuid = uuid;
        this.desc = desc;
        this.idImg = idImg;
        this.name = name;
        this.indexNum = indexNum;
        this.typeId = typeId;
        this.userId = userId;
    }


    public static final Creator<AccountType> CREATOR = new Creator<AccountType>() {
        @Override
        public AccountType createFromParcel(Parcel source) {
            return new AccountType(source);
        }

        @Override
        public AccountType[] newArray(int size) {
            return new AccountType[size];
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
    @Generated(hash = 633711750)
    private transient AccountTypeDao myDao;

    @Override
    public AccountType clone() throws CloneNotSupportedException {
        AccountType accountType;
        accountType = (AccountType) super.clone();
        return accountType;
    }


    public long getMTime() {
        return this.mTime;
    }


    public void setMTime(long mTime) {
        this.mTime = mTime;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1534042648)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountTypeDao() : null;
    }
}
