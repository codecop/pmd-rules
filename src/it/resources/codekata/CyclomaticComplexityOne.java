package codekata;

public class CyclomaticComplexityOne {

    public void okNoComplexity() {

    }

    public void failingWithComplexityTwo() {
        boolean a = false;
        if (a) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
        }
    }
}
