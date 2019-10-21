package thread01;

public class Running {
    public static void main(String[] args){
        Cat cat = new Cat();
        Dog dog = new Dog();
        dog.start();
        cat.start();
        for(int i=2;i<100;i++){
            System.out.println("main run " + i + " steps");
        }
    }
}
