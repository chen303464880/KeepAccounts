package com.cjj.keepaccounts.bean.http;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author CJJ
 * Created by CJJ on 2018/7/17 15:13.
 */
public class SavingBean implements Parcelable {

    /**
     * uuid : 1
     * type_id : 0
     * name :
     * amount : 0.00
     * is_customized : 0
     * start_time : 0
     * end_time : 0
     * has_popped_up : 0
     * img :
     * rate_name :
     * comment :
     * ctime : 0
     * mtime : 1530013799
     * is_deleted : 0
     */

    private String uuid;
    private int type_id;
    private String name;
    private String amount;
    private int is_customized;
    private String start_time;
    private String end_time;
    private int has_popped_up;
    private String img;
    private String rate_name;
    private String comment;
    private String ctime;
    private String mtime;
    private int is_deleted;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getIs_customized() {
        return is_customized;
    }

    public void setIs_customized(int is_customized) {
        this.is_customized = is_customized;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getHas_popped_up() {
        return has_popped_up;
    }

    public void setHas_popped_up(int has_popped_up) {
        this.has_popped_up = has_popped_up;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRate_name() {
        return rate_name;
    }

    public void setRate_name(String rate_name) {
        this.rate_name = rate_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeInt(this.type_id);
        dest.writeString(this.name);
        dest.writeString(this.amount);
        dest.writeInt(this.is_customized);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeInt(this.has_popped_up);
        dest.writeString(this.img);
        dest.writeString(this.rate_name);
        dest.writeString(this.comment);
        dest.writeString(this.ctime);
        dest.writeString(this.mtime);
        dest.writeInt(this.is_deleted);
    }

    public SavingBean() {
    }

    protected SavingBean(Parcel in) {
        this.uuid = in.readString();
        this.type_id = in.readInt();
        this.name = in.readString();
        this.amount = in.readString();
        this.is_customized = in.readInt();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.has_popped_up = in.readInt();
        this.img = in.readString();
        this.rate_name = in.readString();
        this.comment = in.readString();
        this.ctime = in.readString();
        this.mtime = in.readString();
        this.is_deleted = in.readInt();
    }

    public static final Creator<SavingBean> CREATOR = new Creator<SavingBean>() {
        @Override
        public SavingBean createFromParcel(Parcel source) {
            return new SavingBean(source);
        }

        @Override
        public SavingBean[] newArray(int size) {
            return new SavingBean[size];
        }
    };
}
