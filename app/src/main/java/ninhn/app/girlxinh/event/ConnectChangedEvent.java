package ninhn.app.girlxinh.event;

/**
 * Created by NinHN on 5/29/16.
 */
public class ConnectChangedEvent {
    private boolean status;

    public ConnectChangedEvent(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
