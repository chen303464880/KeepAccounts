package com.cjj.keepaccounts.bean.http;

/**
 * @author CJJ
 * Created by CJJ on 2018/7/17 15:01.
 */
public class NoticeBean {

    /**
     * user_id : 2403705
     * notice_id : 1
     * is_deleted : 0
     * ctime : 0
     * mtime : 1530013793
     * notice_name : 记账提醒
     * notice_content : 亲，该记帐了哦！
     * notice_icon : 2130838076
     * start_time : 1489663839
     * event_next : 1489663839
     * alert_offset : no
     * is_repeat : 1
     * repeat_type :
     * repeat_month :
     * repeat_day :
     * repeat_dayofweek : 1,2,3,4,5,6,7
     * is_auto_record : 0
     * auto_record_tpl :
     * note :
     */

    private String user_id;
    private String notice_id;
    private int is_deleted;
    private String ctime;
    private String mtime;
    private String notice_name;
    private String notice_content;
    private String notice_icon;
    private String start_time;
    private String event_next;
    private String alert_offset;
    private int is_repeat;
    private String repeat_type;
    private String repeat_month;
    private String repeat_day;
    private String repeat_dayofweek;
    private int is_auto_record;
    private String auto_record_tpl;
    private String note;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
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

    public String getNotice_name() {
        return notice_name;
    }

    public void setNotice_name(String notice_name) {
        this.notice_name = notice_name;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public String getNotice_icon() {
        return notice_icon;
    }

    public void setNotice_icon(String notice_icon) {
        this.notice_icon = notice_icon;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEvent_next() {
        return event_next;
    }

    public void setEvent_next(String event_next) {
        this.event_next = event_next;
    }

    public String getAlert_offset() {
        return alert_offset;
    }

    public void setAlert_offset(String alert_offset) {
        this.alert_offset = alert_offset;
    }

    public int getIs_repeat() {
        return is_repeat;
    }

    public void setIs_repeat(int is_repeat) {
        this.is_repeat = is_repeat;
    }

    public String getRepeat_type() {
        return repeat_type;
    }

    public void setRepeat_type(String repeat_type) {
        this.repeat_type = repeat_type;
    }

    public String getRepeat_month() {
        return repeat_month;
    }

    public void setRepeat_month(String repeat_month) {
        this.repeat_month = repeat_month;
    }

    public String getRepeat_day() {
        return repeat_day;
    }

    public void setRepeat_day(String repeat_day) {
        this.repeat_day = repeat_day;
    }

    public String getRepeat_dayofweek() {
        return repeat_dayofweek;
    }

    public void setRepeat_dayofweek(String repeat_dayofweek) {
        this.repeat_dayofweek = repeat_dayofweek;
    }

    public int getIs_auto_record() {
        return is_auto_record;
    }

    public void setIs_auto_record(int is_auto_record) {
        this.is_auto_record = is_auto_record;
    }

    public String getAuto_record_tpl() {
        return auto_record_tpl;
    }

    public void setAuto_record_tpl(String auto_record_tpl) {
        this.auto_record_tpl = auto_record_tpl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
