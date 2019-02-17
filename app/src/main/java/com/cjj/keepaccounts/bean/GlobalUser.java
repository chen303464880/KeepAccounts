package com.cjj.keepaccounts.bean;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.cjj.keepaccounts.bean.http.UserInfoBean;

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
@Entity(nameInDb = "global_user", active = true)
public class GlobalUser implements Cloneable, Parcelable {

    @Property(nameInDb = "db_version")
    private String dbVersion;
    @Property(nameInDb = "headimg_annex_url")
    private String headimgAnnexUrl;
    @Property(nameInDb = "follower_cnt")
    private int followerCnt;
    @Property(nameInDb = "bg_img_custom")
    private String bgImgCustom;
    @Property(nameInDb = "bind_phone")
    private String bindPhone;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "is_bg_img_custom_synced")
    private int isBgImgCustomSynced;
    @Property(nameInDb = "constellation")
    private String constellation;
    @Property(nameInDb = "gender")
    private int gender;
    @Property(nameInDb = "birthday")
    private String birthday;
    @Property(nameInDb = "is_img_head_synced")
    private int isImgHeadSynced;
    @Property(nameInDb = "nick")
    private String nick;
    @Property(nameInDb = "color")
    private int color;
    @Property(nameInDb = "net_module")
    private int netModule;
    @Property(nameInDb = "follow_cnt")
    private int followCnt;
    @Property(nameInDb = "headimg_type")
    private int headimgType;
    @Property(nameInDb = "budget_sum")
    private double budgetSum;
    @Property(nameInDb = "password")
    private String password;
    @Property(nameInDb = "imgHead")
    private String imgHead;
    @Property(nameInDb = "theme_id")
    private int themeId;
    @Property(nameInDb = "signature")
    private String signature;
    @Property(nameInDb = "average_cost")
    private double averageCost;
    @Property(nameInDb = "cur_book_uuid")
    private long curBookUuid;
    @Property(nameInDb = "sns_background_img")
    private String snsBackgroundImg;
    @Property(nameInDb = "email")
    private String email;
    @Property(nameInDb = "ctime")
    private long ctime;
    @Property(nameInDb = "is_login")
    private int is_login;
    @Property(nameInDb = "username")
    private String userName;
    @Property(nameInDb = "location")
    private String location;
    @Property(nameInDb = "bg_img")
    private String bgImg;
    @Property(nameInDb = "like_cnt")
    private String likeCnt;
    @Property(nameInDb = "user_id")
    private long UserId;

    @Generated(hash = 1629574980)
    public GlobalUser(String dbVersion, String headimgAnnexUrl, int followerCnt,
                      String bgImgCustom, String bindPhone, long uuid, int isBgImgCustomSynced,
                      String constellation, int gender, String birthday, int isImgHeadSynced,
                      String nick, int color, int netModule, int followCnt, int headimgType,
                      double budgetSum, String password, String imgHead, int themeId, String signature,
                      double averageCost, long curBookUuid, String snsBackgroundImg, String email,
                      long ctime, int is_login, String userName, String location, String bgImg,
                      String likeCnt, long UserId) {
        this.dbVersion = dbVersion;
        this.headimgAnnexUrl = headimgAnnexUrl;
        this.followerCnt = followerCnt;
        this.bgImgCustom = bgImgCustom;
        this.bindPhone = bindPhone;
        this.uuid = uuid;
        this.isBgImgCustomSynced = isBgImgCustomSynced;
        this.constellation = constellation;
        this.gender = gender;
        this.birthday = birthday;
        this.isImgHeadSynced = isImgHeadSynced;
        this.nick = nick;
        this.color = color;
        this.netModule = netModule;
        this.followCnt = followCnt;
        this.headimgType = headimgType;
        this.budgetSum = budgetSum;
        this.password = password;
        this.imgHead = imgHead;
        this.themeId = themeId;
        this.signature = signature;
        this.averageCost = averageCost;
        this.curBookUuid = curBookUuid;
        this.snsBackgroundImg = snsBackgroundImg;
        this.email = email;
        this.ctime = ctime;
        this.is_login = is_login;
        this.userName = userName;
        this.location = location;
        this.bgImg = bgImg;
        this.likeCnt = likeCnt;
        this.UserId = UserId;
    }

    @Generated(hash = 294685092)
    public GlobalUser() {
    }


    public String getDbVersion() {
        return this.dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getHeadimgAnnexUrl() {
        return this.headimgAnnexUrl;
    }

    public void setHeadimgAnnexUrl(String headimgAnnexUrl) {
        this.headimgAnnexUrl = headimgAnnexUrl;
    }

    public int getFollowerCnt() {
        return this.followerCnt;
    }

    public void setFollowerCnt(int followerCnt) {
        this.followerCnt = followerCnt;
    }

    public String getBgImgCustom() {
        return this.bgImgCustom;
    }

    public void setBgImgCustom(String bgImgCustom) {
        this.bgImgCustom = bgImgCustom;
    }

    public String getBindPhone() {
        return this.bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public int getIsBgImgCustomSynced() {
        return this.isBgImgCustomSynced;
    }

    public void setIsBgImgCustomSynced(int isBgImgCustomSynced) {
        this.isBgImgCustomSynced = isBgImgCustomSynced;
    }

    public String getConstellation() {
        return this.constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIsImgHeadSynced() {
        return this.isImgHeadSynced;
    }

    public void setIsImgHeadSynced(int isImgHeadSynced) {
        this.isImgHeadSynced = isImgHeadSynced;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNetModule() {
        return this.netModule;
    }

    public void setNetModule(int netModule) {
        this.netModule = netModule;
    }

    public int getFollowCnt() {
        return this.followCnt;
    }

    public void setFollowCnt(int followCnt) {
        this.followCnt = followCnt;
    }

    public int getHeadimgType() {
        return this.headimgType;
    }

    public void setHeadimgType(int headimgType) {
        this.headimgType = headimgType;
    }

    public double getBudgetSum() {
        return this.budgetSum;
    }

    public void setBudgetSum(double budgetSum) {
        this.budgetSum = budgetSum;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgHead() {
        return this.imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
    }

    public int getThemeId() {
        return this.themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public double getAverageCost() {
        return this.averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public long getCurBookUuid() {
        return this.curBookUuid;
    }

    public void setCurBookUuid(long curBookUuid) {
        this.curBookUuid = curBookUuid;
    }

    public String getSnsBackgroundImg() {
        return this.snsBackgroundImg;
    }

    public void setSnsBackgroundImg(String snsBackgroundImg) {
        this.snsBackgroundImg = snsBackgroundImg;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCtime() {
        return this.ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public int getIs_login() {
        return this.is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBgImg() {
        return this.bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String getLikeCnt() {
        return this.likeCnt;
    }

    public void setLikeCnt(String likeCnt) {
        this.likeCnt = likeCnt;
    }

    public long getUserId() {
        return this.UserId;
    }

    public void setUserId(long UserId) {
        this.UserId = UserId;
    }


    public GlobalUser(UserInfoBean userInfo) {
        this.UserId = userInfo.getUser_id();
        this.userName = userInfo.getUsername();
        this.headimgAnnexUrl = userInfo.getHeadimg_annex_url();
        this.bgImgCustom = userInfo.getBg_img_custom();
        this.bindPhone = userInfo.getMobilephone();
        this.constellation = userInfo.getConstellation();
        this.gender = userInfo.getGender();
        this.birthday = String.valueOf(userInfo.getBirthday());
        this.color = Color.parseColor("#" + userInfo.getColor());
        this.headimgType = userInfo.getHeadimg_type();
        this.budgetSum = userInfo.getBudget_sum();
        this.imgHead = userInfo.getHeadimg();
        this.signature = userInfo.getSignature();
        this.averageCost = Double.parseDouble(userInfo.getAvg_cost());
        this.email = userInfo.getEmail();
        this.ctime = userInfo.getCtime();
        this.is_login = 1;
        this.userName = userInfo.getUsername();
        this.location = userInfo.getLocation();
        this.bgImg = userInfo.getBg_img();
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.UserId = userInfo.getUser_id();
        this.userName = userInfo.getUsername();
        this.nick = userInfo.getNick();
        this.headimgAnnexUrl = userInfo.getHeadimg_annex_url();
        this.bgImgCustom = userInfo.getBg_img_custom();
        this.bindPhone = userInfo.getMobilephone();
        this.constellation = userInfo.getConstellation();
        this.gender = userInfo.getGender();
        this.birthday = String.valueOf(userInfo.getBirthday());
        this.color = Color.parseColor("#" + userInfo.getColor());
        this.headimgType = userInfo.getHeadimg_type();
        this.budgetSum = userInfo.getBudget_sum();
        this.imgHead = userInfo.getHeadimg();
        this.signature = userInfo.getSignature();
        this.averageCost = Double.parseDouble(userInfo.getAvg_cost());
        this.email = userInfo.getEmail();
        this.ctime = userInfo.getCtime();
        this.is_login = 1;
        this.userName = userInfo.getUsername();
        this.location = userInfo.getLocation();
        this.bgImg = userInfo.getBg_img();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dbVersion);
        dest.writeString(this.headimgAnnexUrl);
        dest.writeInt(this.followerCnt);
        dest.writeString(this.bgImgCustom);
        dest.writeString(this.bindPhone);
        dest.writeLong(this.uuid);
        dest.writeInt(this.isBgImgCustomSynced);
        dest.writeString(this.constellation);
        dest.writeInt(this.gender);
        dest.writeString(this.birthday);
        dest.writeInt(this.isImgHeadSynced);
        dest.writeString(this.nick);
        dest.writeInt(this.color);
        dest.writeInt(this.netModule);
        dest.writeInt(this.followCnt);
        dest.writeInt(this.headimgType);
        dest.writeDouble(this.budgetSum);
        dest.writeString(this.password);
        dest.writeString(this.imgHead);
        dest.writeInt(this.themeId);
        dest.writeString(this.signature);
        dest.writeDouble(this.averageCost);
        dest.writeLong(this.curBookUuid);
        dest.writeString(this.snsBackgroundImg);
        dest.writeString(this.email);
        dest.writeLong(this.ctime);
        dest.writeInt(this.is_login);
        dest.writeString(this.userName);
        dest.writeString(this.location);
        dest.writeString(this.bgImg);
        dest.writeString(this.likeCnt);
        dest.writeLong(this.UserId);
    }

    protected GlobalUser(Parcel in) {
        this.dbVersion = in.readString();
        this.headimgAnnexUrl = in.readString();
        this.followerCnt = in.readInt();
        this.bgImgCustom = in.readString();
        this.bindPhone = in.readString();
        this.uuid = in.readLong();
        this.isBgImgCustomSynced = in.readInt();
        this.constellation = in.readString();
        this.gender = in.readInt();
        this.birthday = in.readString();
        this.isImgHeadSynced = in.readInt();
        this.nick = in.readString();
        this.color = in.readInt();
        this.netModule = in.readInt();
        this.followCnt = in.readInt();
        this.headimgType = in.readInt();
        this.budgetSum = in.readDouble();
        this.password = in.readString();
        this.imgHead = in.readString();
        this.themeId = in.readInt();
        this.signature = in.readString();
        this.averageCost = in.readDouble();
        this.curBookUuid = in.readLong();
        this.snsBackgroundImg = in.readString();
        this.email = in.readString();
        this.ctime = in.readLong();
        this.is_login = in.readInt();
        this.userName = in.readString();
        this.location = in.readString();
        this.bgImg = in.readString();
        this.likeCnt = in.readString();
        this.UserId = in.readLong();
    }

    public static final Creator<GlobalUser> CREATOR = new Creator<GlobalUser>() {
        @Override
        public GlobalUser createFromParcel(Parcel source) {
            return new GlobalUser(source);
        }

        @Override
        public GlobalUser[] newArray(int size) {
            return new GlobalUser[size];
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
    @Generated(hash = 1301273730)
    private transient GlobalUserDao myDao;

    @Override
    public GlobalUser clone() throws CloneNotSupportedException {
        return (GlobalUser) super.clone();
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
    @Generated(hash = 1696953748)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGlobalUserDao() : null;
    }
}
