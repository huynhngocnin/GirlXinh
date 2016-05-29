package ninhn.app.girlxinh.event;

/**
 * Created by NinHN on 5/27/16.
 */
public class LoginChangedEvent {
    private boolean status;

    public LoginChangedEvent(boolean status){
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
