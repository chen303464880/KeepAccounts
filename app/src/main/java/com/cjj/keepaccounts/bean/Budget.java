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
 * Created by CJJ on 2018/7/4 15:05.
 */
@Entity(nameInDb = "budget", active = true)
public class Budget extends BaseEntity implements Parcelable, Cloneable {

    @Property(nameInDb = "amount")
    private double amount;
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "mtime")
    private long mTime;
    @Property(nameInDb = "ctime")
    private long cTime;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "list_id")
    private long listId;
    @Property(nameInDb = "type")
    private int type;
    @Property(nameInDb = "user_id")
    private String userId;



    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getListId() {
        return this.listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1883832454)
    private transient BudgetDao myDao;

    @Override
    public Budget clone() throws CloneNotSupportedException {
        return (Budget) super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.amount);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeLong(this.mTime);
        dest.writeLong(this.cTime);
        dest.writeLong(this.uuid);
        dest.writeString(this.name);
        dest.writeLong(this.listId);
        dest.writeInt(this.type);
        dest.writeString(this.userId);
    }

    public long getCTime() {
        return this.cTime;
    }

    public void setCTime(long cTime) {
        this.cTime = cTime;
    }

    public Budget() {
    }

    protected Budget(Parcel in) {
        this.amount = in.readDouble();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.cTime = in.readLong();
        this.uuid = in.readLong();
        this.name = in.readString();
        this.listId = in.readLong();
        this.type = in.readInt();
        this.userId = in.readString();
    }

    @Generated(hash = 972025900)
    public Budget(double amount, String deviceId, int isDeleted, long mTime, long cTime,
                  long uuid, String name, long listId, int type, String userId) {
        this.amount = amount;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.cTime = cTime;
        this.uuid = uuid;
        this.name = name;
        this.listId = listId;
        this.type = type;
        this.userId = userId;
    }

    public static final Creator<Budget> CREATOR = new Creator<Budget>() {
        @Override
        public Budget createFromParcel(Parcel source) {
            return new Budget(source);
        }

        @Override
        public Budget[] newArray(int size) {
            return new Budget[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "Budget{" +
                ", amount=" + amount +
                ", deviceId='" + deviceId + '\'' +
                ", isDeleted=" + isDeleted +
                ", mTime=" + mTime +
                ", cTime=" + cTime +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", listId=" + listId +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 571609092)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBudgetDao() : null;
    }
}
