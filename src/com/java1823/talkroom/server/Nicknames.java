package com.java1823.talkroom.server;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

// 用户显示的昵称
public class Nicknames {

    // 记录数据中需要使用的名称
    private ArrayList<String> names;

    // 用来保存已经使用过的昵称
    private HashSet<String> usedName;

    public Nicknames() {
        usedName = new HashSet<>();
        names = new ArrayList<>();
        names.add("亚瑟");
        names.add("项羽");
        names.add("妲己");
        names.add("鲁班");
        names.add("后羿");
        names.add("程咬金");
        names.add("典韦");
        names.add("庄周");
        names.add("姜子牙");
        names.add("哪吒");
        names.add("花木兰");
        names.add("白起");
        names.add("刘备");
        names.add("张飞");
        names.add("关羽");
        names.add("墨子");
        names.add("杨玉环");
        names.add("盾山");
        names.add("马可波罗");
        names.add("孙尚香");
        names.add("干将莫邪");
        names.add("宫本武藏");
        names.add("夏侯惇");
        names.add("韩信");
        names.add("李白");
        names.add("武则天");
        names.add("小乔");
        names.add("大乔");
    }

    // 随机得到一个昵称
    public String randomNickName() {
        Random random = new Random();
        if (names.size() == usedName.size()) {
            return "unknow"; // 如果名称库里的名称已经使用完成，则返回 unknow
        }
        while (true) {
            int i = random.nextInt(names.size());
            String nickName = names.get(i);
            if (usedName.add(nickName)) {
                return nickName;
            }
        }
    }


}
