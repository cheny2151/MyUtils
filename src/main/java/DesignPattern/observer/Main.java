package DesignPattern.observer;

public class Main {
    public static void main(String[] args) {

        Student studenta = new Student("1", "a", "man");
        Student studentb = new Student("2", "b", "man");

        Teacher teacher = new Teacher();

        teacher.addStudent(studenta);
        teacher.addStudent(studentb);

        teacher.publishHomework("语文作业");
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            teacher.publishHomework("数学作业");
        teacher.comeBackHomework();
        }).start();



    }

}
