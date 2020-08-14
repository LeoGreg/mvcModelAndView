package am.basic.springTest.model.exceptions;

public class TestNotFoundException extends RuntimeException {

    public TestNotFoundException() {
        super("Could not find employee ");
    }
}

