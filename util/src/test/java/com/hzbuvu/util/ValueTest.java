package com.hzbuvu.util;

import com.yesmywine.util.date.DateUtil;
import org.junit.Test;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by WANG, RUIQING on 12/13/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class ValueTest {


    @Test
    public void test() {
//        System.out.println(DateUtil.toString());
//        System.out.println(DateUtil.getNowTime());
//        System.out.println(DateUtil.toString());
        Date str = new Date();
        String ss= DateUtil.toString(str,"yyyy-MM-dd HH:mm:ss");
        System.out.println(str);
        System.out.println(ss);

        String s1="a";
        String s2="a";
        String s3=new String("a");
        String s4=new String("a");

        System.out.println(s1==s2);//t
        System.out.println(s1.compareTo(s2));//0
        System.out.println(s1.equals(s2));//t
        System.out.println(s2==s3);//f
        System.out.println(s2.equals(s3));//t
        System.out.println(s3==s4);//f
        System.out.println(s3.equals(s4));//t

    }

    @Test
    public void gerr(){
        Map<String, Integer> map = new HashMap<>();
        map.put("c", 3);
        map.put("a", 5);
        map.put("b", 1);
        map.put("d", 4);

//        List<Map.Entry<String,Integer>> list = new ArrayList<>(map.entrySet());
//        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        //forEach用法
        List<Integer> l=new ArrayList<>();
//        l.forEach(a ->{
//            System.out.print(a);
//        });
        Collections.sort(l, (o1, o2) -> o2-o1);//Collections的sort方法默认是升序排列,若要倒序则使用Collections的sort方法，并且重写compare方法

        List<Map.Entry<String,Integer>> list=new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue()-o1.getValue();
            }
        });

        for(Map.Entry<String,Integer> mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }
}
    @Test
    public void get() {
//        List<List<String>> l=new LinkedList<>();
//        List<String> m=new ArrayList<>();
//        m.add("wang");
//        l.add(m);
//        m.add("qian");
//        System.out.println(l);
//        System.out.println(m);
//        m.add("111");
//        System.out.println(l);

//        String  a="wang";
//        String b="qian";
//        a=b;
//        b=a+b;

        StringBuffer a = new StringBuffer("wang");
        StringBuffer b = new StringBuffer("qian");
        a = b;
        b.append(a);
        System.out.println(a + "---" + b);

        List<Integer> l = new ArrayList<>();
    }

    @Test
    public void tessss() {
        int[] arr = {1, 2, 6, 8, 4, 3, 7};
        Arrays.sort(arr);
        System.out.print(Arrays.toString(arr));


    }

    @Test
    public void sss(){
        List<Integer> list=new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(1);
        //顺序
        Collections.sort(list);
        //倒序
//        Collections.sort(list, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o2-o1;
//            }
//        });
        for (Integer l:list){
            System.out.println(l);
        }
    }

    @Test
    public void tesss(){
            int [] arr={1,2,6,8,4,3,7};

        //map遍历
//        quickSort(arr,0,arr.length-1);
        Map<String,Integer> map=new HashMap<>();
        for(Map.Entry entry:map.entrySet()){
            String key = entry.getKey().toString();
            int value = Integer.parseInt(entry.getValue().toString());

        }
//
//        for(String a:map.keySet()){
//            System.out.print(a);
//        }
//
//        for (Integer a:map.values()) {
//            System.out.print(a);
//        }
        //选择排序
        int temp;
//        for(int i=0;i<arr.length-1;i++){
//            for(int j=i+1;j<arr.length;j++){
//                if(arr[i]>arr[j]){
//                    temp=arr[i];
//                    arr[i]=arr[j];
//                    arr[j]=temp;
//                }
//            }
//        }

//选择排序
        int size=arr.length;
        for(int i=0;i<size;i++){
            int k=i;
            for(int j=size-1;j>i;j--){
                if(arr[j]<arr[k]){
                    k=j;
                }
            }
            temp=arr[i];
            arr[i]=arr[k];
            arr[k]=temp;
        }

        System.out.print(Arrays.toString(arr));



    }

    @Test
    public void tess(){
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        b) 建立连接
//        Connection conn = DriverManager.getConnection(dbURL,username,password);
//        c) 创建语句对象
//        Statement stmt = conn.createStatement()
//        90. 如何使用sql语句操作数据库？
//        a) 得到语句对象之后，使用语句对象调用executeUpdate(sql)方法和executeQuery(sql)
        String sql="select goodsName from goods where id=20";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://47.89.18.26:3306/mall_goods","root","admin");
            Statement statement=con.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();
            List<Map<String, Object>> list = new ArrayList<>();
            while (resultSet.next()) {
                HashMap row = new HashMap(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), resultSet.getObject(i));
                }
                list.add(row);
            }
            System.out.println(list.size());
            System.out.println(list.get(0).toString());
            String goods=list.get(0).toString();
//            String goodss=goods.replace('=',':');
//            JSONObject jsonObject= JSON.parseObject(goodss);
//            Object goodsOriginalName = jsonObject.get("goodsOriginalName");
            System.out.println(goods);
            resultSet.close();
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//根据经纬度算两点之间的距离
//121.335718,31.235132  /121.480956,31.232538
    //121.38092,31.222101 /114.296811,30.613949
    @Test
    public  void distanceOfTwoPoints( ) {
        double lat1=121.480956;
        double lng1=31.232538;
        double lat2=114.296811;
        double lng2=30.613949;
        // 地球半径
        final double EARTH_RADIUS = 6370996.81;
        double radLat1 = radian(lat1);
        double radLat2 = radian(lat2);
        double a = radLat1 - radLat2;
        double b = radian(lng1) - radian(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        double ss = s * 1.0936132983377;
        System.out.println("两点间的距离是：" + s + "米" + "," + (int) ss + "码,"+s/1000+"公里");
    }

    private  double radian(double d) {
        return d * Math.PI / 180.0;
    }
}
