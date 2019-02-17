package com.cjj.keepaccounts.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.cjj.keepaccounts.base.BaseEntity;
import com.cjj.keepaccounts.dao.BudgetTool;
import com.cjj.keepaccounts.dao.GlobalUserTool;
import com.cjj.keepaccounts.manager.DaoManager;
import com.cjj.keepaccounts.utils.TimeUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.database.Database;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CJJ
 * Created by CJJ on 2018/7/4 14:59.
 */
@Entity(nameInDb = "samecity_main_type", active = true)
public class SameCityMainType extends BaseEntity implements Parcelable, Cloneable {

    @Property(nameInDb = "sameagespend")
    private float sameAgeSpend;
    @Property(nameInDb = "type_icon_id")
    private int typeIconId;
    @Property(nameInDb = "samesexspend")
    private float sameSexSpend;
    @Property(nameInDb = "device_id")
    private String deviceId;
    @Property(nameInDb = "is_deleted")
    private int isDeleted;
    @Property(nameInDb = "mtime")
    private long mTime;
    @Property(nameInDb = "samecityspend")
    private float sameCitySpend;
    @Id
    @org.greenrobot.greendao.annotation.NotNull
    @Index(unique = true)
    @Property(nameInDb = "uuid")
    private long uuid;
    @Property(nameInDb = "type_name")
    private String typeName;
    @Property(nameInDb = "type_id")
    private int typeId;
    @Property(nameInDb = "user_id")
    private String user_id;

    @Transient
    private Budget budget;
    @Transient
    private List<RecordType> recordTypes;

    @Transient
    private Double expend;

    @NotNull
    public Budget getBudget() {
        if (budget == null) {
            Budget tempBudget = DaoManager.INSTANCE.getBudgetDao().queryBuilder()
                    .where(BudgetDao.Properties.ListId.eq(1))
                    .where(BudgetDao.Properties.Type.eq(typeId - 1))
                    .unique();
            if (tempBudget == null) {
                tempBudget = new Budget();
                tempBudget.setAmount(0.0);
                tempBudget.setDeviceId("");
                tempBudget.setIsDeleted(0);
                tempBudget.setMTime(System.currentTimeMillis());
                tempBudget.setType(typeId - 1);
                tempBudget.setUuid(100 + tempBudget.getType());
                tempBudget.setName(typeName);
                tempBudget.setListId(1);
                tempBudget.setUserId(String.valueOf(GlobalUserTool.INSTANCE.getGlobalUser().getUserId()));
                BudgetTool.INSTANCE.insert(tempBudget, false);
            }
            synchronized (this) {
                if (budget == null) {
                    budget = tempBudget;
                }
            }
        }
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
        recordTypes = null;
        expend = null;
    }

    @NotNull
    public List<RecordType> getRecordTypes() {
        if (recordTypes == null) {
            List<RecordType> list = DaoManager.INSTANCE.getRecordTypeDao().queryBuilder()
                    .where(RecordTypeDao.Properties.BudgetType.eq(getBudget().getUuid()))
                    .where(RecordTypeDao.Properties.IsDeleted.eq(0))
                    .where(RecordTypeDao.Properties.ImgSrcId.ge(0))
                    .where(RecordTypeDao.Properties.IsIncoming.eq(0))
                    .orderAsc(RecordTypeDao.Properties.OrderIndex).list();
            synchronized (this) {
                if (recordTypes == null) {
                    recordTypes = list;
                }
            }
        }
        return recordTypes;
    }

    public void setRecordTypes(List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
    }

    public boolean removeRecordType(@NotNull RecordType recordType) {
        ArrayList<RecordType> recordTypes = (ArrayList<RecordType>) getRecordTypes();
        if (recordTypes.isEmpty()) {
            return false;
        }
        for (int i = 0, z = recordTypes.size(); i < z; i++) {
            if (recordTypes.get(i).getUuid() == recordType.getUuid()) {
                recordTypes.remove(i);
                if (expend != null) {
                    expend = null;
                }
                return true;
            }
        }
        return false;
    }


    public void addRecordType(@NotNull RecordType recordType) {
        ArrayList<RecordType> recordTypes = (ArrayList<RecordType>) getRecordTypes();
        if (expend != null) {
            expend = null;
        }
        for (int i = 0, z = recordTypes.size(); i < z; i++) {
            if (recordType.getOrderIndex() < recordTypes.get(i).getOrderIndex()) {
                recordTypes.add(i, recordType);
                return;
            }
        }
        recordTypes.add(recordType);
    }

    public double getChildBudget() {
        double b = 0.0;
        ArrayList<RecordType> recordTypes = (ArrayList<RecordType>) getRecordTypes();
        for (int i = 0, z = recordTypes.size(); i < z; i++) {
            b += recordTypes.get(i).getBudgetAmount();
        }
        return b;
    }

    public double getExpend() {
        if (expend == null && !getRecordTypes().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("select sum(rate_money) from record where is_deleted = 0 and year = ? and month = ? and typeId in (");
            Database database = DaoManager.INSTANCE.getDaoSession().getDatabase();
            for (int i = 0, z = getRecordTypes().size(); i < z; i++) {
                stringBuilder.append(getRecordTypes().get(i).getUuid());
                if (i != z - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(")");
            Cursor cursor = database.rawQuery(stringBuilder.toString(),
                    new String[]{
                            String.valueOf(TimeUtils.getYear()),
                            String.valueOf(TimeUtils.getMonth() - 1)});
            if (cursor.moveToNext()) {
                expend = cursor.getDouble(0);
            } else {
                expend = 0.0;
            }
            cursor.close();


        }
        return expend;
    }

    public void setExpend(double expend) {
        this.expend = expend;
    }


    public float getSameAgeSpend() {
        return this.sameAgeSpend;
    }

    public void setSameAgeSpend(float sameAgeSpend) {
        this.sameAgeSpend = sameAgeSpend;
    }

    public int getTypeIconId() {
        return this.typeIconId;
    }

    public void setTypeIconId(int typeIconId) {
        this.typeIconId = typeIconId;
    }

    public float getSameSexSpend() {
        return this.sameSexSpend;
    }

    public void setSameSexSpend(float sameSexSpend) {
        this.sameSexSpend = sameSexSpend;
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

    public float getSameCitySpend() {
        return this.sameCitySpend;
    }

    public void setSameCitySpend(float sameCitySpend) {
        this.sameCitySpend = sameCitySpend;
    }

    public long getUuid() {
        return this.uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public SameCityMainType() {
    }


    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1682990409)
    private transient SameCityMainTypeDao myDao;

    @Override
    protected SameCityMainType clone() throws CloneNotSupportedException {
        return (SameCityMainType) super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.sameAgeSpend);
        dest.writeInt(this.typeIconId);
        dest.writeFloat(this.sameSexSpend);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isDeleted);
        dest.writeLong(this.mTime);
        dest.writeFloat(this.sameCitySpend);
        dest.writeLong(this.uuid);
        dest.writeString(this.typeName);
        dest.writeInt(this.typeId);
        dest.writeString(this.user_id);
        dest.writeParcelable(this.budget, flags);
        dest.writeTypedList(this.recordTypes);
        dest.writeDouble(this.expend == null ? -1.0 : this.expend);
    }

    protected SameCityMainType(Parcel in) {
        this.sameAgeSpend = in.readFloat();
        this.typeIconId = in.readInt();
        this.sameSexSpend = in.readFloat();
        this.deviceId = in.readString();
        this.isDeleted = in.readInt();
        this.mTime = in.readLong();
        this.sameCitySpend = in.readFloat();
        this.uuid = in.readLong();
        this.typeName = in.readString();
        this.typeId = in.readInt();
        this.user_id = in.readString();
        this.budget = in.readParcelable(Budget.class.getClassLoader());
        this.recordTypes = in.createTypedArrayList(RecordType.CREATOR);
        this.expend = in.readDouble();
        if (this.expend == -1.0) {
            this.expend = null;
        }
    }

    @Generated(hash = 1672986030)
    public SameCityMainType(float sameAgeSpend, int typeIconId, float sameSexSpend, String deviceId, int isDeleted, long mTime, float sameCitySpend, long uuid,
                            String typeName, int typeId, String user_id) {
        this.sameAgeSpend = sameAgeSpend;
        this.typeIconId = typeIconId;
        this.sameSexSpend = sameSexSpend;
        this.deviceId = deviceId;
        this.isDeleted = isDeleted;
        this.mTime = mTime;
        this.sameCitySpend = sameCitySpend;
        this.uuid = uuid;
        this.typeName = typeName;
        this.typeId = typeId;
        this.user_id = user_id;
    }

    public static final Creator<SameCityMainType> CREATOR = new Creator<SameCityMainType>() {
        @Override
        public SameCityMainType createFromParcel(Parcel source) {
            return new SameCityMainType(source);
        }

        @Override
        public SameCityMainType[] newArray(int size) {
            return new SameCityMainType[size];
        }
    };

    @Override
    public String toString() {
        return "SameCityMainType{" +
                "sameAgeSpend=" + sameAgeSpend +
                ", typeIconId=" + typeIconId +
                ", sameSexSpend=" + sameSexSpend +
                ", deviceId='" + deviceId + '\'' +
                ", isDeleted=" + isDeleted +
                ", mTime=" + mTime +
                ", sameCitySpend=" + sameCitySpend +
                ", uuid=" + uuid +
                ", typeName='" + typeName + '\'' +
                ", typeId=" + typeId +
                ", user_id='" + user_id + '\'' +
                ", budget=" + budget +
                ", expend=" + expend +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 735728374)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSameCityMainTypeDao() : null;
    }
}
