package ninhn.app.girlxinh.model;

import java.io.Serializable;

/**
 * Created by NinHN on 8/21/16.
 */
public class PhotoReviewModel extends PhotoModel implements Serializable{
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
