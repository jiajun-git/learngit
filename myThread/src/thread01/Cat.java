package thread01;

public class Cat extends Thread{

    @Override
    public void run() {
        for(int i=2;i<100;i++){
            System.out.println("cat run "+i+" steps");
        }
    }
}
