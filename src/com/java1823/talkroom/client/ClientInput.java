package com.java1823.talkroom.client;

import com.java1823.talkroom.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

// 客户端输入程序
public class ClientInput implements Runnable, UserInput {

    private Client client;              //
    private OutputStream oos = null;    // 向服务端的输入流
    private BufferedReader bufReader;   // 键盘录入流

    public ClientInput(Client client) throws IOException {
        this.oos = client.getSocket().getOutputStream();

        InputStreamReader isr = new InputStreamReader(System.in);
        bufReader = new BufferedReader(isr);
    }

    @Override
    public void run() {
        input();
    }

    @Override
    public void input() {
        while (true) {
            try {
                // 获取用户输入的信息
                String s = bufReader.readLine();
                if (s.equals("over")) {
                    client.exit();
                    break;
                }
                if (oos != null) {
                    oos.write(s.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("获取用户输入信息异常");
            }
        }
    }
}
