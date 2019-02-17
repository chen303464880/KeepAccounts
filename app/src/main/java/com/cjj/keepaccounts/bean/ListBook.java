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
 * @author chenjunjie
 * Created by CJJ on 2018/4/10 9:46.
 */
@Entity(nameInDb = "listbook", active = true)
public class ListBook extends BaseEntity implements Parcelable, Cloneable {

    @Property(nameInDb = "is_default")
    private int isDefault;
    @Property(nameInDb = "list_text_color")
    private String listTextColor;
    @Property(nameInDb = "list_budget_ishide")
    private int listBudgetIshide;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "is_bg_img_custom_synced")
    private int isBgImgCustomSynced;
    @Property(nameInDb = "bg_img_mtime")
    private long bgImgMtime;
    @Property(nameInDb = "list_type")
    private long listType;
    @Property(nameInDb = "is_activated")
    private int isActivated;
    @Property(nameInDb = "list_name")
    private String listName;
    @Property(nameInDb = "list_budget")
    private double listBudget;
    @Property(nameInDb = "list_order")
    private int listOrder;
    @Property(nameInDb = "mTime")
    private long mTime;
    @Property(nameInDb = "rate_name")
    private String rateName;
    @Property(nameInDb = "list_budget_show")
    private String listBudgetShow;
    @Property(nameInDb = "list_rate_budget")
    private double listRateBudget;
    @Property(nameInDb = "rate_list")
    private String rateList;
    @Property(nameInDb = "list_color")
    private String listColor;
    @Property(nameInDb = "ctime")
    private long cTime;
    @Property(nameInDb = "list_bg_img")
    private String listBgImg;
    @Property(nameInDb = "list_bg_cimg")
    private String listBgCimg;
    @Property(nameInDb = "list_budget_ratename")
    private String listBudgetRatename;
    @Property(nameInDb = "unselectAccountIds")
    private String unselectAccountIds;
    @Property(nameInDb = "list_cover")
    private String listCover;
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "user_id")
    private String userId;
    @Property(nameInDb = "date_start")
    private int dateStart;


    @NonNull
    @Override
    public String toString() {
        return "ListBook{" +
                ", isDefault=" + isDefault +
                ", listTextColor='" + listTextColor + '\'' +
                ", uuid=" + uuid +
                ", listType=" + listType +
                ", isActivated=" + isActivated +
                ", listName=" + listName +
                ", listBudget=" + listBudget +
                ", listBudgetShow='" + listBudgetShow + '\'' +
                ", listRateBudget=" + listRateBudget +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isDefault);
        dest.writeString(this.listTextColor);
        dest.writeInt(this.listBudgetIshide);
        dest.writeLong(this.uuid);
        dest.writeInt(this.isBgImgCustomSynced);
        dest.writeLong(this.bgImgMtime);
        dest.writeLong(this.listType);
        dest.writeInt(this.isActivated);
        dest.writeString(this.listName);
        dest.writeDouble(this.listBudget);
        dest.writeInt(this.listOrder);
        dest.writeLong(this.mTime);
        dest.writeString(this.rateName);
        dest.writeString(this.listBudgetShow);
        dest.writeDouble(this.listRateBudget);
        dest.writeString(this.rateList);
        dest.writeString(this.listColor);
        dest.writeLong(this.cTime);
        dest.writeString(this.listBgImg);
        dest.writeString(this.listBgCimg);
        dest.writeString(this.listBudgetRatename);
        dest.writeString(this.unselectAccountIds);
        dest.writeString(this.listCover);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeString(this.userId);
        dest.writeInt(this.dateStart);
    }



    public int getIsDefault() {
        return this.isDefault;
    }


    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }


    public String getListTextColor() {
        return this.listTextColor;
    }


    public void setListTextColor(String listTextColor) {
        this.listTextColor = listTextColor;
    }


    public int getListBudgetIshide() {
        return this.listBudgetIshide;
    }


    public void setListBudgetIshide(int listBudgetIshide) {
        this.listBudgetIshide = listBudgetIshide;
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


    public long getBgImgMtime() {
        return this.bgImgMtime;
    }


    public void setBgImgMtime(long bgImgMtime) {
        this.bgImgMtime = bgImgMtime;
    }


    public long getListType() {
        return this.listType;
    }


    public void setListType(long listType) {
        this.listType = listType;
    }


    public int getIsActivated() {
        return this.isActivated;
    }


    public void setIsActivated(int isActivated) {
        this.isActivated = isActivated;
    }


    public String getListName() {
        return this.listName;
    }


    public void setListName(String listName) {
        this.listName = listName;
    }


    public double getListBudget() {
        return this.listBudget;
    }


    public void setListBudget(double listBudget) {
        this.listBudget = listBudget;
    }


    public int getListOrder() {
        return this.listOrder;
    }


    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }


    public long getMTime() {
        return this.mTime;
    }


    public void setMTime(long mTime) {
        this.mTime = mTime;
    }


    public String getRateName() {
        return this.rateName;
    }


    public void setRateName(String rateName) {
        this.rateName = rateName;
    }


    public String getListBudgetShow() {
        return this.listBudgetShow;
    }


    public void setListBudgetShow(String listBudgetShow) {
        this.listBudgetShow = listBudgetShow;
    }


    public double getListRateBudget() {
        return this.listRateBudget;
    }


    public void setListRateBudget(double listRateBudget) {
        this.listRateBudget = listRateBudget;
    }


    public String getRateList() {
        return this.rateList;
    }


    public void setRateList(String rateList) {
        this.rateList = rateList;
    }


    public String getListColor() {
        return this.listColor;
    }


    public void setListColor(String listColor) {
        this.listColor = listColor;
    }


    public long getCTime() {
        return this.cTime;
    }


    public void setCTime(long cTime) {
        this.cTime = cTime;
    }


    public String getListBgImg() {
        return this.listBgImg;
    }


    public void setListBgImg(String listBgImg) {
        this.listBgImg = listBgImg;
    }


    public String getListBgCimg() {
        return this.listBgCimg;
    }


    public void setListBgCimg(String listBgCimg) {
        this.listBgCimg = listBgCimg;
    }


    public String getListBudgetRatename() {
        return this.listBudgetRatename;
    }


    public void setListBudgetRatename(String listBudgetRatename) {
        this.listBudgetRatename = listBudgetRatename;
    }


    public String getUnselectAccountIds() {
        return this.unselectAccountIds;
    }


    public void setUnselectAccountIds(String unselectAccountIds) {
        this.unselectAccountIds = unselectAccountIds;
    }


    public String getListCover() {
        return this.listCover;
    }


    public void setListCover(String listCover) {
        this.listCover = listCover;
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


    public String getUserId() {
        return this.userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getDateStart() {
        return this.dateStart;
    }


    public void setDateStart(int dateStart) {
        this.dateStart = dateStart;
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


    public ListBook() {
    }

    protected ListBook(Parcel in) {
        this.isDefault = in.readInt();
        this.listTextColor = in.readString();
        this.listBudgetIshide = in.readInt();
        this.uuid = in.readLong();
        this.isBgImgCustomSynced = in.readInt();
        this.bgImgMtime = in.readLong();
        this.listType = in.readLong();
        this.isActivated = in.readInt();
        this.listName = in.readString();
        this.listBudget = in.readDouble();
        this.listOrder = in.readInt();
        this.mTime = in.readLong();
        this.rateName = in.readString();
        this.listBudgetShow = in.readString();
        this.listRateBudget = in.readDouble();
        this.rateList = in.readString();
        this.listColor = in.readString();
        this.cTime = in.readLong();
        this.listBgImg = in.readString();
        this.listBgCimg = in.readString();
        this.listBudgetRatename = in.readString();
        this.unselectAccountIds = in.readString();
        this.listCover = in.readString();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.userId = in.readString();
        this.dateStart = in.readInt();
    }


    @Generated(hash = 1746674906)
    public ListBook(int isDefault, String listTextColor, int listBudgetIshide, long uuid, int isBgImgCustomSynced,
                    long bgImgMtime, long listType, int isActivated, String listName, double listBudget, int listOrder,
                    long mTime, String rateName, String listBudgetShow, double listRateBudget, String rateList,
                    String listColor, long cTime, String listBgImg, String listBgCimg, String listBudgetRatename,
                    String unselectAccountIds, String listCover, String deviceId, int isDeleted, String userId,
                    int dateStart) {
        this.isDefault = isDefault;
        this.listTextColor = listTextColor;
        this.listBudgetIshide = listBudgetIshide;
        this.uuid = uuid;
        this.isBgImgCustomSynced = isBgImgCustomSynced;
        this.bgImgMtime = bgImgMtime;
        this.listType = listType;
        this.isActivated = isActivated;
        this.listName = listName;
        this.listBudget = listBudget;
        this.listOrder = listOrder;
        this.mTime = mTime;
        this.rateName = rateName;
        this.listBudgetShow = listBudgetShow;
        this.listRateBudget = listRateBudget;
        this.rateList = rateList;
        this.listColor = listColor;
        this.cTime = cTime;
        this.listBgImg = listBgImg;
        this.listBgCimg = listBgCimg;
        this.listBudgetRatename = listBudgetRatename;
        this.unselectAccountIds = unselectAccountIds;
        this.listCover = listCover;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.userId = userId;
        this.dateStart = dateStart;
    }


    public static final Creator<ListBook> CREATOR = new Creator<ListBook>() {
        @Override
        public ListBook createFromParcel(Parcel source) {
            return new ListBook(source);
        }

        @Override
        public ListBook[] newArray(int size) {
            return new ListBook[size];
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
    @Generated(hash = 1835912212)
    private transient ListBookDao myDao;


    @Override
    public ListBook clone() throws CloneNotSupportedException {
        ListBook listBook = (ListBook) super.clone();
        return listBook;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1936922023)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getListBookDao() : null;
    }
}
