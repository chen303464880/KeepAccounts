package com.cjj.keepaccounts.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author CJJ
 * Created by CJJ on 2018/6/26 17:03.
 */
@Entity(nameInDb = "global_config", active = true)
public class GlobalConfig implements Cloneable, Parcelable {

    @Property(nameInDb = "last_user_email")
    private String lastUserEmail;
    @Property(nameInDb = "average_cost")
    private double averageCost;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "gesturecode")
    private String gestureCode;
    @Property(nameInDb = "init_data_user_email")
    private String initDataUserEmail;
    @Property(nameInDb = "user_password")
    private String userPassword;
    @Property(nameInDb = "last_version")
    private String lastVersion;
    @Property(nameInDb = "auto_sync")
    private int autoSync;
    @Property(nameInDb = "refresh_token")
    private String refreshToken;
    @Property(nameInDb = "recorded")
    private int recorded;
    @Property(nameInDb = "fud_start_time")
    private long fudStartTime;
    @Property(nameInDb = "access_token")
    private String accessToken;

    @Property(nameInDb = "is_init_data")
    private long isInitData;
    @Property(nameInDb = "last_user_id")
    private long lastUserId;
    @Property(nameInDb = "is_first_time")
    private long isFirstTime;
    @Property(nameInDb = "wronggesturecodetime")
    private long wrongGestureCodeTime;
    @Property(nameInDb = "user_first_time")
    private long userFirstTime;
    @Property(nameInDb = "jiyibi_last_accountid")
    private long jiyibiLastAccountid;



    public String getLastUserEmail() {
        return this.lastUserEmail;
    }

    public void setLastUserEmail(String lastUserEmail) {
        this.lastUserEmail = lastUserEmail;
    }

    public double getAverageCost() {
        return this.averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getGestureCode() {
        return this.gestureCode;
    }

    public void setGestureCode(String gestureCode) {
        this.gestureCode = gestureCode;
    }

    public String getInitDataUserEmail() {
        return this.initDataUserEmail;
    }

    public void setInitDataUserEmail(String initDataUserEmail) {
        this.initDataUserEmail = initDataUserEmail;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getLastVersion() {
        return this.lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public int getAutoSync() {
        return this.autoSync;
    }

    public void setAutoSync(int autoSync) {
        this.autoSync = autoSync;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getRecorded() {
        return this.recorded;
    }

    public void setRecorded(int recorded) {
        this.recorded = recorded;
    }

    public long getFudStartTime() {
        return this.fudStartTime;
    }

    public void setFudStartTime(long fudStartTime) {
        this.fudStartTime = fudStartTime;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getIsInitData() {
        return this.isInitData;
    }

    public void setIsInitData(long isInitData) {
        this.isInitData = isInitData;
    }

    public long getLastUserId() {
        return this.lastUserId;
    }

    public void setLastUserId(long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public long getIsFirstTime() {
        return this.isFirstTime;
    }

    public void setIsFirstTime(long isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public long getWrongGestureCodeTime() {
        return this.wrongGestureCodeTime;
    }

    public void setWrongGestureCodeTime(long wrongGestureCodeTime) {
        this.wrongGestureCodeTime = wrongGestureCodeTime;
    }

    public long getUserFirstTime() {
        return this.userFirstTime;
    }

    public void setUserFirstTime(long userFirstTime) {
        this.userFirstTime = userFirstTime;
    }

    public long getJiyibiLastAccountid() {
        return this.jiyibiLastAccountid;
    }

    public void setJiyibiLastAccountid(long jiyibiLastAccountid) {
        this.jiyibiLastAccountid = jiyibiLastAccountid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastUserEmail);
        dest.writeDouble(this.averageCost);
        dest.writeLong(this.uuid);
        dest.writeString(this.gestureCode);
        dest.writeString(this.initDataUserEmail);
        dest.writeString(this.userPassword);
        dest.writeString(this.lastVersion);
        dest.writeInt(this.autoSync);
        dest.writeString(this.refreshToken);
        dest.writeInt(this.recorded);
        dest.writeLong(this.fudStartTime);
        dest.writeString(this.accessToken);
        dest.writeLong(this.isInitData);
        dest.writeLong(this.lastUserId);
        dest.writeLong(this.isFirstTime);
        dest.writeLong(this.wrongGestureCodeTime);
        dest.writeLong(this.userFirstTime);
        dest.writeLong(this.jiyibiLastAccountid);
    }

    protected GlobalConfig(Parcel in) {
        this.lastUserEmail = in.readString();
        this.averageCost = in.readDouble();
        this.uuid = in.readLong();
        this.gestureCode = in.readString();
        this.initDataUserEmail = in.readString();
        this.userPassword = in.readString();
        this.lastVersion = in.readString();
        this.autoSync = in.readInt();
        this.refreshToken = in.readString();
        this.recorded = in.readInt();
        this.fudStartTime = in.readLong();
        this.accessToken = in.readString();
        this.isInitData = in.readLong();
        this.lastUserId = in.readLong();
        this.isFirstTime = in.readLong();
        this.wrongGestureCodeTime = in.readLong();
        this.userFirstTime = in.readLong();
        this.jiyibiLastAccountid = in.readLong();
    }

    @Generated(hash = 2007393948)
    public GlobalConfig(String lastUserEmail, double averageCost, long uuid,
                        String gestureCode, String initDataUserEmail, String userPassword,
                        String lastVersion, int autoSync, String refreshToken, int recorded,
                        long fudStartTime, String accessToken, long isInitData, long lastUserId,
                        long isFirstTime, long wrongGestureCodeTime, long userFirstTime,
                        long jiyibiLastAccountid) {
        this.lastUserEmail = lastUserEmail;
        this.averageCost = averageCost;
        this.uuid = uuid;
        this.gestureCode = gestureCode;
        this.initDataUserEmail = initDataUserEmail;
        this.userPassword = userPassword;
        this.lastVersion = lastVersion;
        this.autoSync = autoSync;
        this.refreshToken = refreshToken;
        this.recorded = recorded;
        this.fudStartTime = fudStartTime;
        this.accessToken = accessToken;
        this.isInitData = isInitData;
        this.lastUserId = lastUserId;
        this.isFirstTime = isFirstTime;
        this.wrongGestureCodeTime = wrongGestureCodeTime;
        this.userFirstTime = userFirstTime;
        this.jiyibiLastAccountid = jiyibiLastAccountid;
    }

    @Generated(hash = 1728622926)
    public GlobalConfig() {
    }

    public static final Creator<GlobalConfig> CREATOR = new Creator<GlobalConfig>() {
        @Override
        public GlobalConfig createFromParcel(Parcel source) {
            return new GlobalConfig(source);
        }

        @Override
        public GlobalConfig[] newArray(int size) {
            return new GlobalConfig[size];
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
    @Generated(hash = 1094485814)
    private transient GlobalConfigDao myDao;

    @Override
    public GlobalConfig clone() throws CloneNotSupportedException {
        return (GlobalConfig) super.clone();
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1316011500)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGlobalConfigDao() : null;
    }
}
