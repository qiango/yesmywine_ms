import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ${shuang} on 2017/6/9.
 */
public class test {
//
//        private Font mFont = new Font("Times New Roman", Font.PLAIN, 17);
//
//        Color getRandColor ( int fc, int bc){
//            Random random = new Random();
//            if (fc > 255) fc = 255;
//            if (bc > 255) bc = 255;
//            int r = fc + random.nextInt(bc - fc);
//            int g = fc + random.nextInt(bc - fc);
//            int b = fc + random.nextInt(bc - fc);
//            return new Color(r, g, b);
//        }
//        @Test
//        public void getGraphics () {
//
//            int width = 100, height = 18;
//            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
//            Graphics g = image.getGraphics();
//            Random random = new Random();
//            g.setColor(getRandColor(200, 250));
//            g.fillRect(1, 1, width - 1, height - 1);
//            g.setColor(new Color(102, 102, 102));
//            g.drawRect(0, 0, width - 1, height - 1);
//            g.setFont(mFont);
//            g.setColor(getRandColor(160, 200));
//            //画随机线
//            for (int i = 0; i < 155; i++) {
//                int x = random.nextInt(width - 1);
//                int y = random.nextInt(height - 1);
//                int xl = random.nextInt(6) + 1;
//                int yl = random.nextInt(12) + 1;
//                g.drawLine(x, y, x + xl, y + yl);
//            }
//
//            //从另一方向画随机线
//            for (int i = 0; i < 70; i++) {
//                int x = random.nextInt(width - 1);
//                int y = random.nextInt(height - 1);
//                int xl = random.nextInt(12) + 1;
//                int yl = random.nextInt(6) + 1;
//                g.drawLine(x, y, x - xl, y - yl);
//            }
//
//            //生成随机数,并将随机数字转换为字母
//            String sRand = "";
//            for (int i = 0; i < 6; i++) {
//                int itmp = random.nextInt(26) + 65;
//                char ctmp = (char) itmp;
//                sRand += String.valueOf(ctmp);
//                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
//                g.drawString(String.valueOf(ctmp), 15 * i + 10, 16);
//            }
//
//            g.dispose();
//
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("rand", sRand);
//            map.put("image", image);
//            System.out.print(map);
//
//    }
//


    @Test
    public void get(){

        int j = 0,k=0;
        for (int i=0;i<100;i++) {
            ThreadDemo threadDemo = new ThreadDemo("帅", "王迁",++j);
            ThreadDemo1 threadDemo1 = new ThreadDemo1("高", "王迁",++k);
            Thread thread = new Thread(threadDemo);
            Thread thread1 = new Thread(threadDemo1);
            thread.start();
            thread1.start();
        }
    }

    public class ThreadDemo implements Runnable{

        private String wa;
        private String qi;
        private Integer k;

        public ThreadDemo(){}

        public ThreadDemo(String wa,String qi,Integer k){
            this.wa=wa;
            this.qi=qi;
            this.k=k;
        }

        @Override
        public void run() {
        System.out.println("１１１１１１"+"这是第"+k);
//            System.out.println(qi+"是第一,"+"他是最"+wa+"的!");

        }
    }
}
