import com.yesmywine.activity.ifttt.Action;
import com.yesmywine.activity.ifttt.ThisThenThat;
import com.yesmywine.activity.ifttt.action.GetDiscount;
import org.junit.Test;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class IfTTTTest {

    @Test
    public void ifttt() {

        ThisThenThat ttt = new ThisThenThat();
        try {
            Class triggerClz = Class.forName("DiscountOne");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Action action = new GetDiscount();


    }
}
