package com.java1823.talkroom.client;

import com.java1823.talkroom.TalkRoom;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

// 客户端程序
public class Client {

    private String serverHost;      // 服务端IP地址
    private int serverPort = TalkRoom.PORT; // 服务端端口号
    private Socket socket;          // 客户端Socket

    public Client() {
    }

    public Client(String serverHost) {
        this.serverHost = serverHost;
        this.serverPort = TalkRoom.PORT;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    // 连接服务器的方法
    public void connectServer() throws IOException {
        if (socket == null) {
            socket = new Socket(serverHost, serverPort);
        }
        startInput();
        startShowMsg();
    }

    // 退出服务器
    public void exit() {
        System.out.println("退出聊天程序");
    }

    // 开始输入
    public void startInput() {
        // 监听开始输入
        if (socket.isConnected()) {
            try {
                new Thread(new ClientInput(this)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 开始显示消息
    public void startShowMsg() {
        if (socket.isConnected()) {
            try {
                new Thread(new ClientMsg(socket.getInputStream())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
