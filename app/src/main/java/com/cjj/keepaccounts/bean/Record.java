package com.cjj.keepaccounts.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cjj.keepaccounts.base.BaseEntity;
import com.cjj.keepaccounts.dao.GlobalUserTool;
import com.cjj.keepaccounts.dao.RecordTypeTool;
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
 * Created by CJJ on 2017/12/18 14:48.
 */
@Entity(nameInDb = "record")
public class Record extends BaseEntity implements Parcelable, Cloneable {

    @Property(nameInDb = "source")
    private String source;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "typeId")
    private long typeId;
    @Property(nameInDb = "content")
    private String content;

    @Property(nameInDb = "synced1")
    private int synced1;
    @Property(nameInDb = "rate_money")
    private double rateMoney;
    @Property(nameInDb = "money")
    private double money;
    @Property(nameInDb = "mtime")
    private long mTime;
    @Property(nameInDb = "list_id")
    private long listId;
    @Property(nameInDb = "photo1")
    private String photo1;
    @Property(nameInDb = "photo2")
    private String photo2;
    @Property(nameInDb = "photo3")
    private String photo3;
    @Property(nameInDb = "rate_name")
    private String rateName;
    @Property(nameInDb = "year")
    private int year;
    @Property(nameInDb = "month")
    private int month;
    @Property(nameInDb = "day")
    private int day;
    @Property(nameInDb = "the_date")
    private int theDate;
    @Property(nameInDb = "notice_id")
    private long noticeId;
    @Property(nameInDb = "creditID")
    private long creditID;
    @Property(nameInDb = "is_impulse")
    private int isImpulse;
    @Property(nameInDb = "the_hour")
    private int theHour;
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "action_id")
    private long actionId;
    @Property(nameInDb = "accountId")
    private long accountId;
    @Property(nameInDb = "createTime")
    private long createTime;
    @Property(nameInDb = "rtime")
    private long rTime;
    @Property(nameInDb = "user_id")
    private String userId;

    @Transient
    private RecordType recordType;
    @Transient
    private Account account;


    @ToMany(joinProperties = {
            @JoinProperty(name = "creditID", referencedName = "uuid")
    })
    private List<Credit> credits;

    @Transient
    private List<RecordToTag> members;

    @Transient
    private Long memberCount;

    @Transient
    private boolean isNode = false;
    @Transient
    private double income;
    @Transient
    private double expend;

    public Record(long uuid) {
        this.uuid = uuid;
        this.source = "";
        this.content = "";
        this.synced1 = 0;
        this.listId = 1L;
        this.photo1 = "";
        this.photo2 = "";
        this.photo3 = "";
        this.rateName = "";
        this.noticeId = 0L;
        this.isImpulse = 0;

        this.rateMoney = 0.0;
        this.money = 0.0;

        this.year = TimeUtils.getYear();
        this.month = TimeUtils.getMonth() - 1;
        this.day = TimeUtils.getDay();
        this.theDate = year * 10000 + month * 100 + day;

        this.accountId = 0L;
        this.creditID = 0L;

        this.createTime = TimeUtils.getTimeOfSecond();
        this.rTime = TimeUtils.getTimeOfSecond();
        this.userId = String.valueOf(GlobalUserTool.INSTANCE.getGlobalUser().getUserId());
    }

    public boolean isNode() {
        return isNode;
    }

    public void setNode(boolean node) {
        isNode = node;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpend() {
        return expend;
    }

    public void setExpend(double expend) {
        this.expend = expend;
    }


    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTheDate() {
        return this.theDate;
    }

    public void setTheDate(int theDate) {
        this.theDate = theDate;
    }

    public int getSynced1() {
        return this.synced1;
    }

    public void setSynced1(int synced1) {
        this.synced1 = synced1;
    }

    public double getRateMoney() {
        return this.rateMoney;
    }

    public void setRateMoney(double rateMoney) {
        this.rateMoney = rateMoney;
        this.money = rateMoney;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public long getMTime() {
        return this.mTime;
    }

    public void setMTime(long mTime) {
        this.mTime = mTime;
    }

    public long getListId() {
        return this.listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getPhoto1() {
        return this.photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return this.photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return this.photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getRateName() {
        return this.rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.theDate = year * 10000 + month * 100 + day;
    }

    public long getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public long getCreditID() {
        return this.creditID;
    }

    public void setCreditID(long creditID) {
        this.creditID = creditID;
    }

    public int getIsImpulse() {
        return this.isImpulse;
    }

    public void setIsImpulse(int isImpulse) {
        this.isImpulse = isImpulse;
    }

    public int getTheHour() {
        return this.theHour;
    }

    public void setTheHour(int theHour) {
        this.theHour = theHour;
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

    public long getActionId() {
        return this.actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getrTime() {
        return this.rTime;
    }

    public void setrTime(long rTime) {
        this.rTime = rTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    @Generated(hash = 477726293)
    public Record() {
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 765166123)
    private transient RecordDao myDao;

    @NonNull
    @Override
    public String toString() {
        return "Record{" +
                ", uuid=" + uuid +
                ", typeId=" + typeId +
                ", isDeleted=" + isDeleted +
                ", content='" + content + '\'' +
                ", theDate=" + theDate +
                ", rateMoney=" + rateMoney +
                ", mTime=" + mTime +
                ", rateName='" + rateName + '\'' +
                ", creditID=" + creditID +
                ", accountId=" + accountId +
                ", rtime=" + rTime +
                ", userId='" + userId + '\'' +
                '}';
    }

    public synchronized void resetMembers() {
        members = null;
    }

    public RecordType getRecordType() {
        if (typeId == 0) {
            return null;
        }
        if (recordType == null) {
            RecordType unique = RecordTypeTool.INSTANCE.getRecordType(typeId);
            synchronized (this) {
                if (recordType == null) {
                    recordType = unique;
                }
            }
        }
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public Account getAccount() {
        if (account == null) {

            AccountDao targetDao = DaoManager.daoSession.getAccountDao();
            Account accountsNew = targetDao.queryBuilder()
                    .where(AccountDao.Properties.Uuid.eq(accountId)).unique();
            synchronized (this) {
                if (account == null) {
                    account = accountsNew;
                }
            }
        }
        return account;
    }


    public void setMembers(List<RecordToTag> members) {
        this.members = members;
    }

    public List<RecordToTag> getMembers() {
        if (members == null) {
            List<RecordToTag> membersNew = DaoManager.INSTANCE.getRecordToTagDao()
                    .queryBuilder()
                    .where(RecordToTagDao.Properties.RecordId.eq(uuid))
                    .where(RecordToTagDao.Properties.IsDeleted.eq(0))
                    .list();
            synchronized (this) {
                if (members == null) {
                    members = membersNew;
                }
            }
        }
        return members;
    }

    public int getMemberCount() {
        return getMembers().size();
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 317047040)
    public List<Credit> getCredits() {
        if (credits == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CreditDao targetDao = daoSession.getCreditDao();
            List<Credit> creditsNew = targetDao._queryRecord_Credits(creditID);
            synchronized (this) {
                if (credits == null) {
                    credits = creditsNew;
                }
            }
        }
        return credits;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1420576259)
    public synchronized void resetCredits() {
        credits = null;
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
    public Record clone() throws CloneNotSupportedException {
        Record record = (Record) super.clone();
        record.recordType = null;
        record.account = null;
        record.resetCredits();
        record.resetMembers();
        return record;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.source);
        dest.writeLong(this.uuid);
        dest.writeLong(this.typeId);
        dest.writeString(this.content);
        dest.writeInt(this.synced1);
        dest.writeDouble(this.rateMoney);
        dest.writeDouble(this.money);
        dest.writeLong(this.mTime);
        dest.writeLong(this.listId);
        dest.writeString(this.photo1);
        dest.writeString(this.photo2);
        dest.writeString(this.photo3);
        dest.writeString(this.rateName);
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.theDate);
        dest.writeLong(this.noticeId);
        dest.writeLong(this.creditID);
        dest.writeInt(this.isImpulse);
        dest.writeInt(this.theHour);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeLong(this.actionId);
        dest.writeLong(this.accountId);
        dest.writeLong(this.createTime);
        dest.writeLong(this.rTime);
        dest.writeString(this.userId);
        dest.writeParcelable(this.recordType, flags);
        dest.writeParcelable(this.account, flags);
        dest.writeTypedList(this.credits);
        dest.writeTypedList(this.members);
        dest.writeByte(this.isNode ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.income);
        dest.writeDouble(this.expend);
    }

    public long getRTime() {
        return this.rTime;
    }

    public void setRTime(long rTime) {
        this.rTime = rTime;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1505145191)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordDao() : null;
    }

    protected Record(Parcel in) {
        this.source = in.readString();
        this.uuid = in.readLong();
        this.typeId = in.readLong();
        this.content = in.readString();
        this.synced1 = in.readInt();
        this.rateMoney = in.readDouble();
        this.money = in.readDouble();
        this.mTime = in.readLong();
        this.listId = in.readLong();
        this.photo1 = in.readString();
        this.photo2 = in.readString();
        this.photo3 = in.readString();
        this.rateName = in.readString();
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.theDate = in.readInt();
        this.noticeId = in.readLong();
        this.creditID = in.readLong();
        this.isImpulse = in.readInt();
        this.theHour = in.readInt();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.actionId = in.readLong();
        this.accountId = in.readLong();
        this.createTime = in.readLong();
        this.rTime = in.readLong();
        this.userId = in.readString();
        this.recordType = in.readParcelable(RecordType.class.getClassLoader());
        this.account = in.readParcelable(Account.class.getClassLoader());
        this.credits = in.createTypedArrayList(Credit.CREATOR);
        this.members = in.createTypedArrayList(RecordToTag.CREATOR);
        this.isNode = in.readByte() != 0;
        this.income = in.readDouble();
        this.expend = in.readDouble();
    }

    @Generated(hash = 410648719)
    public Record(String source, long uuid, long typeId, String content, int synced1,
                  double rateMoney, double money, long mTime, long listId, String photo1,
                  String photo2, String photo3, String rateName, int year, int month, int day,
                  int theDate, long noticeId, long creditID, int isImpulse, int theHour,
                  String deviceId, int isDeleted, long actionId, long accountId, long createTime,
                  long rTime, String userId) {
        this.source = source;
        this.uuid = uuid;
        this.typeId = typeId;
        this.content = content;
        this.synced1 = synced1;
        this.rateMoney = rateMoney;
        this.money = money;
        this.mTime = mTime;
        this.listId = listId;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.rateName = rateName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.theDate = theDate;
        this.noticeId = noticeId;
        this.creditID = creditID;
        this.isImpulse = isImpulse;
        this.theHour = theHour;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.actionId = actionId;
        this.accountId = accountId;
        this.createTime = createTime;
        this.rTime = rTime;
        this.userId = userId;
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}
