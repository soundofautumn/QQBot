package cn.jiayistu.configuration;

/***
 * 当在配置文件中无法找到指定的配置时抛出此异常
 *
 * @author Qjming
 * @since 1.3
 */
public class ValueNotFoundException extends RuntimeException{

    /***
     *构造一个不带详细信息的<code>ValueNotFoundException</code>
     */
    public ValueNotFoundException() {
        super();
    }

    /***
     * 构造一个<code>ClassCastException</code>带指定详细信息。
     * @param message 详细信息
     */
    public ValueNotFoundException(String message) {
        super(message);
    }
}
