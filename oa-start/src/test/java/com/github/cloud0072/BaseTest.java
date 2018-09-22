package com.github.cloud0072;

import com.github.cloud0072.common.util.ReflectUtils;
import com.github.cloud0072.common.util.StringUtils;
import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.util.UserUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BaseTest {

    @Test
    public void test01() {
        Set<Class<?>> classSet = ReflectUtils.getClasses("com.github.cloud0072.common.model");

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
        System.out.println(UserUtils.encrypt(user).getPassword());

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
                .peek(user -> new User(StringUtils.UUID32()))
                .map(user -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("account", user.getAccount());
                    m.put("name", user.getName());
                    m.put("password", user.getPassword());
                    return m;
                }).collect(toList());

        System.out.println(result);
    }

    @Test
    public void test14() {
        String target = "opt,top,pot,hi,ih,max,hide,pot";
        //分组
        String[] stringArr = target.split(",");
        //结果集
        Map<String, Set<String>> result = new HashMap<>();
        Stream.of(stringArr).forEach(s -> {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            result.computeIfAbsent(key, v -> new HashSet<>()).add(s);
        });
        //输出
        result.forEach((key, set) -> System.out.println(set));
    }

    @Test
    public void test16() {
        Map map1 = new HashMap(); // pageIt 系统查出来的
        Map map2 = null;    //手动改的

        System.out.println(map2.get(""));

    }

    @Test
    public void test17() {
        String modelPath = "system";
        String entityName = "user";
        int times = 1000 * 1000 * 100;
        String template = "/{0}/{1}/{2}_edit";
        String template2 = "/%s/%s/%s_edit";
        long startTime = System.currentTimeMillis();


        //执行200,000,000次,耗时4,399毫秒
//        for (int i = 0; i < times; i++) {
        //执行100,000,000次,耗时5,311毫秒
//            String temp = "/" + modelPath + "/" + entityName + "/" + i + "_edit";
//        }

        //执行10,000,000次,耗时6,345毫秒   MessageFormat 效率好低啊
//        for (int i = 0; i < times; i++) {
//            执行100,000,000次,耗时57,248毫秒
//            String index = "" + i;
//            new String(MessageFormat.format(template, modelPath,entityName, index));
//        }
//
        //执行10,000,000次,耗时13,068毫秒
//        for (int i = 0; i < times; i++) {
//            执行100,000,000次,耗时128,889毫秒
//            int index = i;
//            new String(String.format(template2, modelPath,entityName, index));
//        }

        //执行100,000,000次,耗时4,105毫秒
//        for (int i = 0; i < times; i++) {
//            StringBuilder builder = new StringBuilder().append("/").append(modelPath).append("/").append(entityName)
//                    .append("/").append(i).append("_edit");
//            builder.toString();
//        }


        System.out.println(MessageFormat.format("执行{0}次,耗时{1}毫秒", times, (System.currentTimeMillis() - startTime)));

//        System.out.println(MessageFormat.format("现在时间是 {0}",new Date()));

    }

    @Test
    public void test19() throws IOException {
        BufferedImage img = ImageIO.read(new FileInputStream(new File("d:\\123\\test.bmp")));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "bmp", os);

        File file = new File("D:\\123\\out.bmp");

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        byte[] bytes = os.toByteArray();
        bos.write(bytes);
        bos.flush();
        bos.close();
    }

    @Test
    public void testMultiValueMap(){
        MultiValuedMap<String,String> map = new ArrayListValuedHashMap<>();
        map.put("name","1");
        map.put("name","2");
        map.put("name","3");

        System.out.println(map.get("name"));
    }
}
