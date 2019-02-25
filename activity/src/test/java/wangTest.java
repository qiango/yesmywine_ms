import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by hz on 12/19/16.
 */
public class wangTest {


    @Test
    public void get() throws ParseException {

        long time = System.currentTimeMillis();
        System.out.println(time);
        Date date = new Date();
        long a = date.getTime();
        System.out.print(a);
        System.out.print(date);
    }

    @Test
    public void getSalt() {
        int length = 6;
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(62)]);
        }
        System.out.print(buffer.toString());
    }

    @Test
    public void toDate() {
        String date = "1482804901619";
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            System.out.print(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getONe() {
        String s = "1482804901619";
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        System.out.print(date);
    }

    @Test
    public void getii() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = new Long("1483080636000");
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Format To String(Date):" + d);
        System.out.println("Format To Date:" + date);

    }

    @Test
    public void wangqian() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = "2016-12-30 14:50:36";
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.print("Format To times:" + date.getTime());
    }


}
