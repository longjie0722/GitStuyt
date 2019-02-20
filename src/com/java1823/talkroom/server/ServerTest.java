package com.java1823.talkroom.server;

import java.io.IOException;

public class ServerTest {


    public static void main(String[] args) throws IOException {

        Server server = new Server("王者荣耀");
        server.startListenr();

    }


}
