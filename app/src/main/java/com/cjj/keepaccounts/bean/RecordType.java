package com.cjj.keepaccounts.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cjj.keepaccounts.base.BaseEntity;
import com.cjj.keepaccounts.manager.DaoManager;
import com.cjj.keepaccounts.utils.TimeUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.database.Database;

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/19 11:51.
 */
@Entity(nameInDb = "com_qeeniao_mobile_kdjz_Models_RecordType", active = true)
public class RecordType extends BaseEntity implements Parcelable, Cloneable {

    @Property(nameInDb = "imgSrcId")
    private int imgSrcId;
    @Property(nameInDb = "typeDesc")
    private String typeDesc;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "budget_amount")
    private float budgetAmount;
    @Property(nameInDb = "orderIndex")
    private int orderIndex;
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "color")
    private int color;
    @Property(nameInDb = "mtime")
    private long mTime;
    @Property(nameInDb = "isIncoming")
    private int isIncoming;
    @Property(nameInDb = "list_id")
    private long listId;
    @Property(nameInDb = "budget_type")
    private long budgetType;
    @Property(nameInDb = "user_id")
    private String userId;

    @Transient
    private boolean isCheck;
    @Transient
    private long showId;

    @Transient
    private Double moneyExpend;

    public Double getMoneyExpend() {
        if (moneyExpend == null) {
            String sql = "select sum(rate_money) from record where is_deleted = 0 and year = ? and month = ? and typeId = ?";
            Database database = DaoManager.INSTANCE.getDaoSession().getDatabase();
            Cursor cursor = database.rawQuery(sql,
                    new String[]{
                            String.valueOf(TimeUtils.getYear()),
                            String.valueOf(TimeUtils.getMonth() - 1),
                            String.valueOf(uuid)});
            if (cursor.moveToNext()) {
                moneyExpend = cursor.getDouble(0);
            } else {
                moneyExpend = 0.0;
            }
            cursor.close();
        }
        return moneyExpend;
    }

    public void setMoneyExpend(Double moneyExpend) {
        this.moneyExpend = moneyExpend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imgSrcId);
        dest.writeString(this.typeDesc);
        dest.writeLong(this.uuid);
        dest.writeFloat(this.budgetAmount);
        dest.writeInt(this.orderIndex);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeInt(this.color);
        dest.writeLong(this.mTime);
        dest.writeInt(this.isIncoming);
        dest.writeLong(this.listId);
        dest.writeLong(this.budgetType);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeString(this.userId);
    }


    public int getImgSrcId() {
        return this.imgSrcId;
    }

    public void setImgSrcId(int imgSrcId) {
        this.imgSrcId = imgSrcId;
    }

    public String getTypeDesc() {
        return this.typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public float getBudgetAmount() {
        return this.budgetAmount;
    }

    public void setBudgetAmount(float budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public int getOrderIndex() {
        return this.orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
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

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getmTime() {
        return this.mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getIsIncoming() {
        return this.isIncoming;
    }

    public void setIsIncoming(int isIncoming) {
        this.isIncoming = isIncoming;
    }

    public long getListId() {
        return this.listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public long getBudgetType() {
        return this.budgetType;
    }

    public void setBudgetType(long budgetType) {
        this.budgetType = budgetType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public long getShowId() {
        if (showId == 0) {
            if (uuid > 100000) {
                showId = uuid / 10000;
            } else {
                showId = uuid;
            }
        }
        return showId;
    }

    public void setShowId(long showId) {
        this.showId = showId;
    }

    public RecordType() {
    }

    protected RecordType(Parcel in) {
        this.imgSrcId = in.readInt();
        this.typeDesc = in.readString();
        this.uuid = in.readLong();
        this.budgetAmount = in.readFloat();
        this.orderIndex = in.readInt();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.color = in.readInt();
        this.mTime = in.readLong();
        this.isIncoming = in.readInt();
        this.listId = in.readLong();
        this.budgetType = in.readLong();
        this.isCheck = in.readByte() != 0;
        this.userId = in.readString();
    }

    @Generated(hash = 957946896)
    public RecordType(int imgSrcId, String typeDesc, long uuid, float budgetAmount, int orderIndex, String deviceId, int isDeleted,
                      int color, long mTime, int isIncoming, long listId, long budgetType, String userId) {
        this.imgSrcId = imgSrcId;
        this.typeDesc = typeDesc;
        this.uuid = uuid;
        this.budgetAmount = budgetAmount;
        this.orderIndex = orderIndex;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.color = color;
        this.mTime = mTime;
        this.isIncoming = isIncoming;
        this.listId = listId;
        this.budgetType = budgetType;
        this.userId = userId;
    }


    public static final Creator<RecordType> CREATOR = new Creator<RecordType>() {
        @Override
        public RecordType createFromParcel(Parcel source) {
            return new RecordType(source);
        }

        @Override
        public RecordType[] newArray(int size) {
            return new RecordType[size];
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
    @Generated(hash = 1987238338)
    private transient RecordTypeDao myDao;

    @Override
    public RecordType clone() throws CloneNotSupportedException {
        return (RecordType) super.clone();
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

    @NonNull
    @Override
    public String toString() {
        return "RecordType{" +
                "imgSrcId=" + imgSrcId +
                ", typeDesc='" + typeDesc + '\'' +
                ", uuid=" + uuid +
                ", budgetAmount=" + budgetAmount +
                ", orderIndex=" + orderIndex +
                ", isDeleted=" + isDeleted +
                ", color=" + color +
                ", mtime=" + mTime +
                ", isIncoming=" + isIncoming +
                ", budgetType=" + budgetType +
                ", deviceId=" + deviceId +
                '}';
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getMTime() {
        return this.mTime;
    }

    public void setMTime(long mTime) {
        this.mTime = mTime;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1190671309)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordTypeDao() : null;
    }
}