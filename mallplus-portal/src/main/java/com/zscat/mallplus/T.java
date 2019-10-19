package com.zscat.mallplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/7/3.
 */
public class T {
    public static void main(String[] args) {
        List<Person> persons = new ArrayList();
        List<Integer> ids = new ArrayList<>();//用来临时存储person的id
        persons.add(new Person(1, "name1", 10));
        persons.add(new Person(2, "name2", 21));
        persons.add(new Person(5, "name5", 55));
        persons.add(new Person(3, "name3", 34));
        persons.add(new Person(1, "name1", 101));

        List<Person> personList = persons.stream().filter(// 过滤去重
                v -> {
                    boolean flag = !ids.contains(v.getId());
                    ids.add(v.getId());
                    return flag;
                }
        ).collect(Collectors.toList());
        System.out.println(personList);
    }
    public static int numJewelsInStones(String J, String S) {
        return S.replaceAll("[^" + J + "]", "").length();
    }
    public static int numJewelsInStones1(String J, String S) {
        Map<Character, Integer> map = new HashMap<>();
        int count = 0;
        for(char s : S.toCharArray())
            map.put(s, map.getOrDefault(s, 0) + 1);

        for(int i = 0; i < J.length(); i++)
            count += map.getOrDefault(J.charAt(i), 0);

        return count;
    }


}
