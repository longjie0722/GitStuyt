package com.java1823.talkroom.server;

import com.java1823.talkroom.UserInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerInput implements UserInput, Runnable {

    private BufferedReader br = null;
    private ServerInputListener inputListener;

    public ServerInput() {
        InputStream is = System.in;
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
    }

    public void startServerInput() {
        new Thread(this).start();
    }

    @Override
    public void input() {
        try {
            while (true) {
                String s = br.readLine();
                if (this.inputListener != null) {
                    this.inputListener.serverInput(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        this.input();
    }

    public ServerInputListener getInputListener() {
        return inputListener;
    }

    public void setInputListener(ServerInputListener inputListener) {
        this.inputListener = inputListener;
    }

    public interface ServerInputListener {
        void serverInput(String msg);
    }

}
