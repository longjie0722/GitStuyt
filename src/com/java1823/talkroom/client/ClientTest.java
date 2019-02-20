package com.java1823.talkroom.client;

import java.io.IOException;

public class ClientTest {

    public static void main(String[] args) throws IOException {

        Client client = new Client("192.168.27.100");
        client.connectServer();

    }

}
