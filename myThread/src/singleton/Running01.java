package singleton;

public class Running01 {
    public static void main(String[] args){
        for(int i=0;i<5;i++){
            new Thread(new Singleton01()).start();
        }
    }
}
