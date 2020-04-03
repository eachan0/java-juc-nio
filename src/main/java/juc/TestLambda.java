package juc;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.*;
import java.util.function.*;

/**
 * @author <a href="zyc199777@gmail.com">Zhu yc</a>
 * @version 1.0
 * @date 2020年04月02日
 * @desc juc.TestLambda
 */
public class TestLambda {

    @Test
    public void test1(){
        Comparator<Integer> com = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        TreeSet<Integer> ts = new TreeSet<>(com);
    }

    @Test
    public void test2(){
        Comparator<Integer> com = Integer::compare;
        TreeSet<Integer> ts = new TreeSet<>(com);
    }

    @Test
    public void test3(){
       /* List<String> list = Arrays.asList("123", "456");
//        list.stream()
        list.stream().filter(x->!x.isBlank()).forEach(System.out::println);*/
        /*long epochSecond = Instant.now().toEpochMilli();
        System.out.println(epochSecond);
        LocalDateTime.now().with((ld)->{
            return null;
        });
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.format(dtf));
        System.out.println(dtf.format(now));
        System.out.println(now.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(now.format(DateTimeFormatter.ISO_DATE_TIME));*/

        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        availableZoneIds.stream().filter(x->{
            String[] split = x.split("/");
            return "Asia".equals(split[0]);
        }).forEach(System.out::println);

        /*Consumer<String> consumer = System.out::println;
        consumer.andThen(consumer).andThen(consumer).accept("h");

        Supplier<String> supplier = () -> "test";

        Predicate<String> predicate = supplier.get()::equals;
        consumer.accept(String.valueOf(predicate.test(supplier.get())));

        Function<String, String> function = String::toLowerCase;
        consumer.accept(function.apply("HEllo"));
        UnaryOperator<String> unaryOperator = String::toUpperCase;
        consumer.accept(unaryOperator.apply("HEllo"));
        BinaryOperator<Integer> binaryOperator = Integer::sum;
        consumer.accept(binaryOperator.apply(50, 50).toString());*/
    }
}
