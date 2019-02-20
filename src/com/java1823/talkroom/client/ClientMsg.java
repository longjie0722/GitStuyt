package com.java1823.talkroom.client;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.IOException;
import java.io.InputStream;

// 用户端信息展示类
public class ClientMsg implements Runnable {

    private InputStream inputStream;

    public ClientMsg(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        byte[] buf = new byte[1024];
        while (true) {
            try {
                int read = inputStream.read(buf);
                String msg = new String(buf, 0, read);
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("用户显示出现异常");
                break; // 出现异常，跳出循环
            }
        }
    }
}
