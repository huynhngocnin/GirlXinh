package ninhn.app.girlxinh.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.constant.AppConstant;

/**
 * Created by ninhn on 4/7/2016.
 */
public class PhotoModel extends Model implements Serializable{
    private String uploadAvatar = AppConstant.BLANK;
    private String name = AppConstant.BLANK;
    private String description = AppConstant.BLANK;
    private String url = AppConstant.BLANK;
    private String uploadId = AppConstant.BLANK;
    private String uploadName = AppConstant.BLANK;
    private long view = 0;
    private List<String> love = new ArrayList<>();


    public String getUploadAvatar() {
        return uploadAvatar;
    }

    public void setUploadAvatar(String uploadAvatar) {
        this.uploadAvatar = uploadAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public List<String> getLove() {
        return love;
    }

    public void setLove(List<String> love) {
        this.love = love;
    }

}
