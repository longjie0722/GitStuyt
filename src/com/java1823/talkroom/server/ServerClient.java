package com.java1823.talkroom.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerClient implements Runnable {

    private String nickname;    // 用户的随机昵称
    private Socket socket;      // 服务端得到的用户Socket

    private UserMessageListener msgListener;
    private UserExceptionListener exceptionListener;

    public ServerClient() {
    }

    public ServerClient(String nickname, Socket socket) {
        this.nickname = nickname;
        this.socket = socket;
        InetAddress inetAddress = socket.getInetAddress();
        // 打印一下ip及ip对应的昵称
        System.out.println(inetAddress.getHostAddress() + "|" + nickname);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    // 向客户端发送消息
    public void sendMsg(String msg) {
        try {
            if (socket.isConnected() && !socket.isOutputShutdown()) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(msg.getBytes());
                outputStream.flush();
            }
        } catch (IOException io) {
            io.printStackTrace();
            // 向这个用户输入信息，出现异常，表明这个用户已经下线
            if (exceptionListener != null) {
                exceptionListener.userException(this.nickname);
            }
        }
    }

    @Override
    public void run() {
        // 接受客户端发送过来的消息
        try {
            byte[] buf = new byte[1024];
            while (true) {
                if (socket.isConnected() && !socket.isInputShutdown()) {
                    InputStream inputStream = socket.getInputStream();
                    int read = inputStream.read(buf);
                    // 得到用户输入的信息，并将用户的信息转发给其他用户
                    String userMsg = new String(buf, 0, read);
                    if (this.msgListener != null) {
                        msgListener.userMessage(this.nickname, userMsg);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 用户输入异常，表示得不到这个用户了，将这个用户从服务端的列表移除
            if (exceptionListener != null) {
                exceptionListener.userException(this.nickname);
            }
        }
    }

    public UserMessageListener getMsgListener() {
        return msgListener;
    }

    public void setMsgListener(UserMessageListener msgListener) {
        this.msgListener = msgListener;
    }

    public UserExceptionListener getExceptionListener() {
        return exceptionListener;
    }

    public void setExceptionListener(UserExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    public interface UserMessageListener {
        void userMessage(String nickName, String msg);
    }

    public interface UserExceptionListener {
        void userException(String nickName);
    }

}
