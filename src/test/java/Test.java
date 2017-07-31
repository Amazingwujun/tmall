import org.springframework.util.DigestUtils;

public class Test {

    @org.junit.Test
    public void hash() {
        String s = DigestUtils.md5DigestAsHex("Wujun1223".getBytes());
        System.out.println(s);
    }
}
