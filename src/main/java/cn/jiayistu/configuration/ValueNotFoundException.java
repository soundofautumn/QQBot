package cn.jiayistu.configuration;

public class ValueNotFoundException extends RuntimeException{
    public ValueNotFoundException() {
        super();
    }

    public ValueNotFoundException(String message) {
        super(message);
    }
}
