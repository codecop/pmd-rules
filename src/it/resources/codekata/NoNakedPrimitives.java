package codekata;

import java.util.Date;

@SuppressWarnings("unused")
public class NoNakedPrimitives {

    // private constructors and methods can do anything.
    private NoNakedPrimitives(String a, String b) {
    }

    private void hidden(int a, int b) {
    }

    public int getNumber() {
        return 0;
    }

    @Override
    public String toString() {
        return "0";
    }

    public void methodWithPrimitives(byte[] buf, Date when) {
    }

}
