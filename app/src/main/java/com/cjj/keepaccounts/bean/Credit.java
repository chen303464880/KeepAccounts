package com.cjj.keepaccounts.bean;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cjj.keepaccounts.BR;
import com.cjj.keepaccounts.base.BaseEntity;
import com.cjj.keepaccounts.constants.Constants;
import com.cjj.keepaccounts.manager.DaoManager;
import com.cjj.keepaccounts.utils.TimeUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * @author chenjunjie
 * Created by CJJ on 2017/12/22 11:19.
 */
@Entity(nameInDb = "credit")
public class Credit extends BaseEntity implements Parcelable, Cloneable, Observable {
    @Transient
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

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
    @Property(nameInDb = "dc_uname")
    private String dcUName;
    @Property(nameInDb = "money")
    private double money;
    @Property(nameInDb = "ctime")
    private long cTime;
    @Property(nameInDb = "rate_name")
    private String rateName;
    @Property(nameInDb = "content")
    private String content;
    @Property(nameInDb = "user_id")
    private String userId;
    @Property(nameInDb = "settlementTime")
    private long settlementTime;
    @Property(nameInDb = "alertOffset")
    private String alertOffset;
    @Property(nameInDb = "isOpenRemind")
    private int isOpenRemind;
    @ToMany(joinProperties = {
            @JoinProperty(name = "uuid", referencedName = "creditID")
    })
    private List<Record> records;

    @Transient
    private Record borrowRecord;
    @Transient
    private Account borrowAccount;

    @Transient
    private List<Record> repayRecord;


    @Transient
    private double repayMoney = 0.0;

    @Transient
    private double interestMoney = 0.0;


    @Transient
    private boolean isNode;

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }

    public Record getBorrowRecord() {
        if (borrowRecord == null) {
            if (daoSession == null) {
                daoSession = DaoManager.daoSession;
            }
            Record temp = daoSession.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.CreditID.eq(uuid))
                    .where(RecordDao.Properties.RateMoney.notEq(0))
                    .where(RecordDao.Properties.IsDeleted.eq(0))
                    .list().get(0);
            synchronized (this) {
                if (borrowRecord == null) {
                    borrowRecord = temp;
                }
            }
        }
        return borrowRecord;
    }

    public void setBorrowRecord(Record borrowRecord) {
        this.borrowRecord = borrowRecord;
    }

    /**
     * true:借出 false:借入
     */
    public boolean isLend() {
        return getBorrowRecord().getTypeId() == Constants.LEND_MONEY_INCOME;
    }

    /**
     * 获取对应的还款资产账户的typeId
     *
     * @return -103L:别人还我的钱 -108L:我还别人的钱
     */
    public long getRepayPropertyId() {
        return isLend() ? Constants.LEND_MONEY_REPAY_INCOME : Constants.BORROW_MONEY_REPAY_EXPEND;
    }

    /**
     * 获取对应的还款的借入借出账户的typeId
     *
     * @return -104L:借出账户 -109L:借入账户
     */
    public long getRepayDebtId() {
        return isLend() ? Constants.LEND_MONEY_REPAY_EXPEND : Constants.BORROW_MONEY_REPAY_INCOME;
    }

    /**
     * 获取对应的利息的typeId
     *
     * @return -105L:别人给我的利息 -110L:我给别人的利息
     */
    public long getInterestId() {
        return isLend() ? Constants.LEND_MONEY_INTEREST : Constants.BORROW_MONEY_INTEREST;
    }

//    public long getInterestId(){
//        return isLend()?Constants.LEND_MONEY_REPAY_INCOME:Constants.BORROW_MONEY_REPAY_EXPEND;
//    }


    public List<Record> getRepayRecord() {
        if (repayRecord == null) {
            if (daoSession == null) {
                daoSession = DaoManager.daoSession;
            }
            List<Record> temp = daoSession.getRecordDao().queryBuilder()
                    .where(RecordDao.Properties.CreditID.eq(uuid))
                    .whereOr(RecordDao.Properties.TypeId.eq(getRepayPropertyId())
                            , RecordDao.Properties.TypeId.eq(getInterestId()))
                    .where(RecordDao.Properties.RateMoney.notEq(0))
                    .where(RecordDao.Properties.IsDeleted.eq(0))
                    .orderDesc(RecordDao.Properties.RTime)
                    .orderAsc(RecordDao.Properties.Uuid).list();
            synchronized (this) {
                if (repayRecord == null) {
                    repayRecord = temp;
                }
            }

        }
        return repayRecord;
    }

    public void setRepayRecord(List<Record> repayRecord) {
        this.repayRecord = repayRecord;
    }

    public double getRepayMoney() {
        if (repayMoney == 0.0) {
            List<Record> repayRecord = getRepayRecord();
            for (int i = 0, z = repayRecord.size(); i < z; i++) {
                Record record = repayRecord.get(i);
                if (record.getTypeId() == (getRepayPropertyId())) {//计算已还的钱
                    repayMoney += Math.abs(record.getRateMoney());
                }
            }
        }
        return repayMoney;
    }

    public void setRepayMoney(double repayMoney) {
        this.repayMoney = repayMoney;
    }

    public double getInterestMoney() {
        if (interestMoney == 0.0) {
            List<Record> repayRecord = getRepayRecord();
            for (int i = 0, z = repayRecord.size(); i < z; i++) {
                Record record = repayRecord.get(i);
                if (record.getTypeId() == (getInterestId())) {
                    interestMoney += record.getRateMoney();
                }
            }
        }
        return interestMoney;
    }

    public void setInterestMoney(double interestMoney) {
        this.interestMoney = interestMoney;
    }

    public boolean isNode() {
        return isNode;
    }

    public void setNode(boolean node) {
        isNode = node;
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

    @Bindable
    public long getMTime() {
        return this.mTime;
    }

    public void setMTime(long mTime) {
        this.mTime = mTime;
        registry.notifyChange(this, BR.mTime);
    }

    @Bindable
    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
        registry.notifyChange(this, BR.uuid);
    }
    @Bindable
    public String getDcUName() {
        return this.dcUName;
    }

    public void setDcUName(String dcUName) {
        this.dcUName = dcUName;
        registry.notifyChange(this,BR.dcUName);
    }
    @Bindable
    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
        registry.notifyChange(this,BR.money);
    }
    @Bindable
    public long getcTime() {
        return this.cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
        registry.notifyChange(this,BR.cTime);
    }
    @Bindable
    public String getRateName() {
        return this.rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
        registry.notifyChange(this,BR.rateName);
    }
    @Bindable
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
        registry.notifyChange(this,BR.content);
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Bindable
    public long getSettlementTime() {
        return this.settlementTime;
    }

    public void setSettlementTime(long settlementTime) {
        this.settlementTime = settlementTime;
        registry.notifyChange(this,BR.settlementTime);
    }
    @Bindable
    public String getAlertOffset() {
        return this.alertOffset;
    }

    public void setAlertOffset(String alertOffset) {
        this.alertOffset = alertOffset;
        registry.notifyChange(this,BR.alertOffset);
    }
    @Bindable
    public int getIsOpenRemind() {
        return this.isOpenRemind;
    }

    public void setIsOpenRemind(int isOpenRemind) {
        this.isOpenRemind = isOpenRemind;
        registry.notifyChange(this,BR.isOpenRemind);
    }


    public Credit() {
    }

    public Credit(long uuid) {
        this.uuid = uuid;
        this.isDeleted = 0;
        this.deviceId = "";
        this.cTime = TimeUtils.getTimeOfSecond();
        this.dcUName = "";
        this.money = 0.0;
        this.rateName = "";
        this.userId = "";
        this.isOpenRemind = 0;
        this.alertOffset = "";
        this.content = "";
        this.mTime = System.currentTimeMillis();
    }


    @Override
    public Credit clone() throws CloneNotSupportedException {
        Credit credit = (Credit) super.clone();
        credit.resetRecords();
        return credit;
    }

    @NonNull
    @Override
    public String toString() {
        return "Credit{" +
                "deviceId='" + deviceId + '\'' +
                ", is_deleted=" + isDeleted +
                ", mTime=" + mTime +
                ", uuid=" + uuid +
                ", dc_uname='" + dcUName + '\'' +
                ", money=" + money +
                ", ctime=" + cTime +
                ", rate_name='" + rateName + '\'' +
                ", content='" + content + '\'' +
                ", user_id='" + userId + '\'' +
                ", settlementTime=" + settlementTime +
                ", alertOffset='" + alertOffset + '\'' +
                ", isOpenRemind=" + isOpenRemind +
                '}';
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
        dest.writeString(this.dcUName);
        dest.writeDouble(this.money);
        dest.writeLong(this.cTime);
        dest.writeString(this.rateName);
        dest.writeString(this.content);
        dest.writeString(this.userId);
        dest.writeLong(this.settlementTime);
        dest.writeString(this.alertOffset);
        dest.writeInt(this.isOpenRemind);
        dest.writeTypedList(this.records);
        dest.writeParcelable(this.borrowRecord, flags);
        dest.writeTypedList(this.repayRecord);
        dest.writeDouble(this.repayMoney);
        dest.writeDouble(this.interestMoney);
        dest.writeByte(this.isNode ? (byte) 1 : (byte) 0);
    }

    public long getCTime() {
        return this.cTime;
    }

    public void setCTime(long cTime) {
        this.cTime = cTime;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 670685020)
    public List<Record> getRecords() {
        if (records == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecordDao targetDao = daoSession.getRecordDao();
            List<Record> recordsNew = targetDao._queryCredit_Records(uuid);
            synchronized (this) {
                if (records == null) {
                    records = recordsNew;
                }
            }
        }
        return records;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 3024737)
    public synchronized void resetRecords() {
        records = null;
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
    @Generated(hash = 880145964)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCreditDao() : null;
    }

    protected Credit(Parcel in) {
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.uuid = in.readLong();
        this.dcUName = in.readString();
        this.money = in.readDouble();
        this.cTime = in.readLong();
        this.rateName = in.readString();
        this.content = in.readString();
        this.userId = in.readString();
        this.settlementTime = in.readLong();
        this.alertOffset = in.readString();
        this.isOpenRemind = in.readInt();
        this.records = in.createTypedArrayList(Record.CREATOR);
        this.borrowRecord = in.readParcelable(Record.class.getClassLoader());
        this.repayRecord = in.createTypedArrayList(Record.CREATOR);
        this.repayMoney = in.readDouble();
        this.interestMoney = in.readDouble();
        this.isNode = in.readByte() != 0;
    }

    @Generated(hash = 342722826)
    public Credit(String deviceId, int isDeleted, long mTime, long uuid, String dcUName, double money,
                  long cTime, String rateName, String content, String userId, long settlementTime,
                  String alertOffset, int isOpenRemind) {
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.uuid = uuid;
        this.dcUName = dcUName;
        this.money = money;
        this.cTime = cTime;
        this.rateName = rateName;
        this.content = content;
        this.userId = userId;
        this.settlementTime = settlementTime;
        this.alertOffset = alertOffset;
        this.isOpenRemind = isOpenRemind;
    }


    public static final Creator<Credit> CREATOR = new Creator<Credit>() {
        @Override
        public Credit createFromParcel(Parcel source) {
            return new Credit(source);
        }

        @Override
        public Credit[] newArray(int size) {
            return new Credit[size];
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
    @Generated(hash = 952244632)
    private transient CreditDao myDao;
}

