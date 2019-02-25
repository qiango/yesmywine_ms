public class ThreadDemo1 implements Runnable{

    private String wa;
    private String qi;
    private Integer j;

    public ThreadDemo1(){}

    public ThreadDemo1(String wa,String qi,Integer j){
        this.wa=wa;
        this.qi=qi;
        this.j=j;
    }
    @Override
    public void run() {
        System.out.println("２２２２２２２２"+"这是第"+j);
//        System.out.println(qi+"是第一,"+"他是最"+wa+"的!");

    }
}