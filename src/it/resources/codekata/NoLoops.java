package codekata;

public class NoLoops {

    public void main() { // NOPMD
        for (int i = 0; i < 3; i++) {

        }

        int i = 0;
        while (i < 3) {
            i++;
        }

        do {

        } while (false);
    }
}
