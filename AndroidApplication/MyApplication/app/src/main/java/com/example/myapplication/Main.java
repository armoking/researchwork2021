package com.example.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public String sendMessage(final String msg) throws InterruptedException {
        AtomicReference<String> result = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try {
                Socket s = new Socket(MainActivity.HOST, MainActivity.PORT);
                OutputStream out = s.getOutputStream();
                PrintWriter output = new PrintWriter(out);
                output.println(msg);
                output.flush();
                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                result.set(input.readLine());
                output.close();
                out.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        while (thread.isAlive()) {
            Thread.sleep(1000);
        }

        return result.get();
    }

}
