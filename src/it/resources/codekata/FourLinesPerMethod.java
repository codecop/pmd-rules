package codekata;

public class FourLinesPerMethod {

    void okShortMethod() {
        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
        System.out.println(4);
    }

    void failingLongMethod() {
        int i = 0;
        System.out.println(i);
        i++;
        System.out.println(i);
        System.out.println(++i);
    }
}
