package ninhn.app.girlxinh.helper;

/**
 * Created by NinHN on 4/7/16.
 */
public class AppValue {

    private static AppValue instace;
    private int value1;

    public static AppValue getInstance() {
        if (instace == null) {
            instace = new AppValue();
        }
        return instace;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }
}
