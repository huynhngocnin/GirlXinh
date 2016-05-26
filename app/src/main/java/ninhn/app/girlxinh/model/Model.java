package ninhn.app.girlxinh.model;

import java.util.Date;

import ninhn.app.girlxinh.constant.AppConstant;

/**
 * Created by NinHN on 5/23/16.
 */
public class Model {
    private String id = AppConstant.BLANK;
    private Date lastUpTime;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastUpTime() {
        return lastUpTime;
    }

    public void setLastUpTime(Date lastUpTime) {
        this.lastUpTime = lastUpTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
