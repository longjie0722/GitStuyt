package com.java1823.talkroom.server;

import com.java1823.talkroom.TalkRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server implements ServerClient.UserMessageListener,
        ServerInput.ServerInputListener, ServerClient.UserExceptionListener {

    private int port = TalkRoom.PORT;
    private String subject; // 聊天主题

    private List<ServerClient> listClients = null;

    private ServerSocket serverSocket = null;
    private Nicknames nicknames = null;         // 昵称集合

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss"); // 聊天的时间格式化工具

    private ServerInput serverInput = null;

    public Server(String subject) throws IOException {
        this.subject = subject;
        this.nicknames = new Nicknames();
        this.serverSocket = new ServerSocket(port);
        this.listClients = new ArrayList<>();
        this.serverInput = new ServerInput();
        this.serverInput.setInputListener(this);
    }

    // 开始监听用户的连接
    public void startListenr() throws IOException {
        System.out.println(this.subject + " 聊天室开启");
        this.serverInput.startServerInput(); // 开始记录服务端输入
        while (true) {
            // 表示有用户连接上来了
            Socket accept = serverSocket.accept();
            ServerClient serverClient = new ServerClient(nicknames.randomNickName(), accept);
            serverClient.setMsgListener(this);
            serverClient.setExceptionListener(this);
            listClients.add(serverClient);
            // 对客户端说欢迎
            welcomeToClient(serverClient);
            tellOtherClient(serverClient);
            // 开始监听客户端的输入信息
            new Thread(serverClient).start();
        }
    }

    // 告诉其他用户谁进入了聊天室
    private synchronized void tellOtherClient(ServerClient client) {
        String msg = client.getNickname() + "进入了聊天室\n";
        serverInput(msg);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < listClients.size(); i++) {
            if (i >= listClients.size() - 1) {
                sb.append(listClients.get(i).getNickname());
            } else {
                sb.append(listClients.get(i).getNickname() + "，");
            }
        }
        sb.append("在聊天室里，共有" + listClients.size() + "人在线");
        serverInput(sb.toString());
    }

    // 欢迎用户进入聊天室
    private void welcomeToClient(ServerClient client) {
        client.sendMsg("欢迎来到" + this.subject + "聊天室，你的昵称是" + client.getNickname() + "\n");
    }

    // 向用户发送消息
    @Override
    public synchronized void userMessage(String nickName, String msg) {
        // 这里做转发功能
        // 昵称 时间
        // 信息
        String userMsg = nickName + " " + getDate() + "\n" + msg;
        System.out.println(userMsg);
        for (ServerClient client : listClients) {
            if (client.getSocket().isConnected()) {
                client.sendMsg(userMsg);
            }
        }
    }

    // 获取时间
    private String getDate() {
        return sdf.format(new Date());
    }

    // 服务端的输入接口
    @Override
    public void serverInput(String msg) {
        this.userMessage("Admin", msg);
    }

    // 用户出现异常
    @Override
    public synchronized void userException(String nickName) {
        // 用户读取异常，表示这个用户下线
        System.out.println("用户异常：" + nickName);
        // 在list中移除这个用户 先移除用户，在通知其他用户
        for (ServerClient client : listClients) {
            if (client.getNickname().equals(nickName)) {
                listClients.remove(client);
                return;
            }
        }
        // 通知其他用户
        userMessage(nickName, nickName + "这个用户下线了");
    }
}
