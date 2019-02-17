package com.cjj.keepaccounts.bean;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.cjj.keepaccounts.BR;
import com.cjj.keepaccounts.base.BaseEntity;
import com.cjj.keepaccounts.manager.DaoManager;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author CJJ
 * Created by CJJ on 2017/11/13 16:03.
 * Copyright © 2015-2017 CJJ. All rights reserved.
 */
@Entity(nameInDb = "account", active = true)
public class Account extends BaseEntity implements Parcelable, Cloneable, Observable {

    @Transient
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    @Property(nameInDb = "last_btime")
    private int lastBTime;
    @Id
    @NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "desc")
    private String desc;
    @Property(nameInDb = "bank_name")
    private String bankName;
    @Property(nameInDb = "money")
    private double money;
    @Property(nameInDb = "order_index")
    private long orderIndex;
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "mtime")
    private long mTime;
    @Property(nameInDb = "color")
    private String color;
    @Property(nameInDb = "returnDay")
    private int returnDay;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "credit_limit")
    private float creditLimit;
    @Property(nameInDb = "type_id")
    @Unique
    private int typeId;
    @Property(nameInDb = "user_id")
    private String userId;
    @Property(nameInDb = "billDay")
    private int billDay;
    @Property(nameInDb = "isShow")
    private int isShow;

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                ", last_btime=" + lastBTime +
                ", uuid=" + uuid +
                ", desc='" + desc + '\'' +
                ", bank_name='" + bankName + '\'' +
                ", money=" + money +
                ", order_index=" + orderIndex +
                ", device_id='" + deviceId + '\'' +
                ", is_deleted=" + isDeleted +
                ", mtime=" + mTime +
                ", color='" + color + '\'' +
                ", returnDay=" + returnDay +
                ", name='" + name + '\'' +
                ", credit_limit=" + creditLimit +
                ", typeId=" + typeId +
                ", user_id='" + userId + '\'' +
                ", isShow=" + isShow + "\n\t\t->" +
                ", account_types=" + accountType +
                '}';
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }

    @Transient
    private AccountType accountType;


    @Bindable
    public int getLastBTime() {
        return this.lastBTime;
    }

    public void setLastBTime(int lastBTime) {
        this.lastBTime = lastBTime;
        registry.notifyChange(this, BR.lastBTime);
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
    public String getDesc() {
        return this.desc;

    }

    public void setDesc(String desc) {
        this.desc = desc;
        registry.notifyChange(this, BR.desc);
    }

    @Bindable
    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
        registry.notifyChange(this, BR.bankName);
    }

    @Bindable
    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
        registry.notifyChange(this, BR.money);
    }

    @Bindable
    public long getOrderIndex() {
        return this.orderIndex;
    }

    public void setOrderIndex(long orderIndex) {
        this.orderIndex = orderIndex;
        registry.notifyChange(this, BR.orderIndex);
    }

    @Bindable
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        registry.notifyChange(this, BR.deviceId);
    }

    @Bindable
    public int getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
        registry.notifyChange(this, BR.isDeleted);
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
    public String getColor() {
        if (this.color.length() == 6) {
            return "#FF" + this.color;
        } else {
            return this.color;
        }
    }

    @Bindable
    public void setColor(String color) {
        this.color = color;
        registry.notifyChange(this, BR.color);
    }

    @Bindable
    public int getReturnDay() {
        return this.returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
        registry.notifyChange(this, BR.returnDay);
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        registry.notifyChange(this, BR.name);
    }

    @Bindable
    public float getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(float creditLimit) {
        this.creditLimit = creditLimit;
        registry.notifyChange(this, BR.creditLimit);
    }

    @Bindable
    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
        registry.notifyChange(this, BR.typeId);
    }

    @Bindable
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        registry.notifyChange(this, BR.userId);
    }

    @Bindable
    public int getIsShow() {
        return this.isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
        registry.notifyChange(this, BR.isShow);
    }

    @Bindable
    public int getBillDay() {
        return this.billDay;
    }

    public void setBillDay(int billDay) {
        this.billDay = billDay;
        registry.notifyChange(this, BR.billDay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.lastBTime);
        dest.writeLong(this.uuid);
        dest.writeString(this.desc);
        dest.writeString(this.bankName);
        dest.writeDouble(this.money);
        dest.writeLong(this.orderIndex);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeLong(this.mTime);
        dest.writeString(this.color);
        dest.writeInt(this.returnDay);
        dest.writeString(this.name);
        dest.writeFloat(this.creditLimit);
        dest.writeInt(this.typeId);
        dest.writeString(this.userId);
        dest.writeInt(this.billDay);
        dest.writeInt(this.isShow);
        dest.writeParcelable(this.accountType, flags);
    }


    public AccountType getAccountType() {
        if (accountType == null) {
            final DaoSession daoSession = DaoManager.daoSession;
            AccountTypeDao targetDao = daoSession.getAccountTypeDao();
            AccountType accountTypeNew = targetDao.queryBuilder()
                    .where(AccountTypeDao.Properties.TypeId.eq(this.typeId))
                    .unique();
            synchronized (this) {
                if (accountType == null) {
                    accountType = accountTypeNew;
                }
            }
        }
        return accountType;
    }


    protected Account(Parcel in) {
        this.lastBTime = in.readInt();
        this.uuid = in.readLong();
        this.desc = in.readString();
        this.bankName = in.readString();
        this.money = in.readDouble();
        this.orderIndex = in.readLong();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.color = in.readString();
        this.returnDay = in.readInt();
        this.name = in.readString();
        this.creditLimit = in.readFloat();
        this.typeId = in.readInt();
        this.userId = in.readString();
        this.billDay = in.readInt();
        this.isShow = in.readInt();
        this.accountType = in.readParcelable(AccountType.class.getClassLoader());
    }


    @Generated(hash = 882125521)
    public Account() {
    }

    @Generated(hash = 829791607)
    public Account(int lastBTime, long uuid, String desc, String bankName, double money,
                   long orderIndex, String deviceId, int isDeleted, long mTime, String color,
                   int returnDay, String name, float creditLimit, int typeId, String userId,
                   int billDay, int isShow) {
        this.lastBTime = lastBTime;
        this.uuid = uuid;
        this.desc = desc;
        this.bankName = bankName;
        this.money = money;
        this.orderIndex = orderIndex;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.color = color;
        this.returnDay = returnDay;
        this.name = name;
        this.creditLimit = creditLimit;
        this.typeId = typeId;
        this.userId = userId;
        this.billDay = billDay;
        this.isShow = isShow;
    }


    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
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
    @Generated(hash = 335469827)
    private transient AccountDao myDao;

    @Override
    public Account clone() throws CloneNotSupportedException {
        Account account;
        account = (Account) super.clone();
        account.accountType = null;
        return account;
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
    @Generated(hash = 1812283172)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountDao() : null;
    }
}
