package ninhn.app.girlxinh.event;

/**
 * Created by NinHN on 5/27/16.
 */
public class LoginChangedEvent {
    private int status;

    public LoginChangedEvent(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
