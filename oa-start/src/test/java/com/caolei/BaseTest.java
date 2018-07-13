package com.caolei;

import com.caolei.system.pojo.User;
import com.caolei.system.utils.EncryptUtils;
import com.caolei.system.utils.ReflectUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class BaseTest {


    @Test
    public void test01() {
        Set<Class<?>> classSet = ReflectUtils.getClasses("com.caolei.common.pojo");

        System.out.println(classSet);

    }

    @Test
    public void test02() {
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            set.add((int) (Math.random() * 1000));
        }

        System.out.println(new TreeSet<>(set));
    }

    @Test
    public void test03() {

    }

    @Test
    public void guessGame() {
        System.out.println("欢迎进行猜谜小游戏~~！\n");
        Scanner in = new Scanner(System.in);
        int times = 0;

        System.out.println("开始！\n");
        String bingo = generator(5);

        while (true) {
            times++;
            System.out.println("请输入5个不同的英文字母...");
            String input = in.next().toUpperCase();
            int result = check(bingo, input);
            if (result == 55) {
                System.out.println("bingo! 恭喜你，获胜了~！您的挑战次数为:" + times);
                break;
            } else if (result == -1) {
                System.out.println("下次加油哦~！正确答案是:" + bingo);
                break;
            } else if (result == -2) {
                System.out.println("请输入5位英文字母！");
            } else {
                System.out.println("位置正确的个数为:" + result % 10 + "\t正确数量为:" + result / 10);
            }
        }
    }

    private String generator(int length) {
        StringBuilder key = new StringBuilder("QWERTYUIOPLKJHGFDSAZXCVBNM");
        String result = "";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * key.length());
            result += key.charAt(index);
            key.deleteCharAt(index);
        }
        return result;
    }

    private int check(String src, String input) {
        if (input.toLowerCase().equals("exit")) {
            return -1;
        } else if (input.length() != 5) {
            return -2;
        }
        int result = 0;
        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == input.charAt(i)) {
                result += 1;
            }
            if (src.contains(String.valueOf(input.charAt(i)))) {
                result += 10;
            }
        }
        return result;
    }

    @Test
    public void test10() {
        System.out.println(false ? 99.9 : 9);
        System.out.println(true ? 9 : 99.9);
    }

    @Test
    public void test11() {
        User user = new User();
        user.setPassword("admin");
        System.out.println(EncryptUtils.encrypt(user).getPassword());

    }

    @Test
    public void test12() {
        String password = new Md5Hash("admin", "8d78869f470951332959580424d4bf4f", 2).toString();

        System.out.println(password);
    }

    @Test
    public void test13() {
        String account = "account";
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User(account + i, Integer.toString(i), account + i, null, false);
            users.add(user);
        }

        List<Map> result = users.stream()
                .peek(user -> user.setPassword(user.getPassword() + 0))
                .filter(user -> Integer.valueOf(user.getPassword()) > 50)
                .peek(user -> new User(EncryptUtils.UUID32()))
                .map(user -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("account", user.getAccount());
                    m.put("name", user.getName());
                    m.put("password", user.getPassword());
                    return m;
                }).collect(toList());

        System.out.println(result);
    }

}
