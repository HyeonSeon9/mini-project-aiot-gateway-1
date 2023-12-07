package com.nhnacademy.aiot.gateway;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        // int i = 51200;
        // System.out.println((i >> 8) & 0xFF);
        // System.out.println(i & 0xFF);
        // System.out.println(200 & 0xFF);
        // byte im = -56;
        // // System.out.println(o & 0xFF);
        // ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        // b.putInt((im) & 0x00FF);
        // System.out.println(Arrays.toString(b.array()));
        // byte o = b.get(2);
        // byte t = b.get(3);
        // // int test = 0xc8;
        // int trans = (o << 8) & 0xFF00 | t & 0x00FF;
        // System.out.println(trans);

        // while (true) {
        // BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        // String cmd;
        // if ((cmd = bf.readLine()).equals("exit")) {
        // break;
        // }
        // int i = Integer.parseInt(cmd);
        // int prev = ((i >> 8) & 0xFF);
        // int next = (i & 0xFF);



        // System.out.println(prev + " : " + next);
        // }

        // int i = 2314;
        // ByteBuffer b = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        // b.putInt(i);
        // byte[] buffer = new byte[b.array().length * 2];
        // System.out.println(Arrays.toString(b.array()));
        // for (int j = 0; j < b.array().length; j++) {
        // int current = b.get(j);
        // byte prev = (byte) (current >> 4);
        // byte next = (byte) (current & 0xF);

        // buffer[(j * 2)] = prev;
        // buffer[(j * 2) + 1] = next;
        // }
        // System.out.println(Arrays.toString(buffer));

        byte i = -56;
        byte j = 0;
        int k = ((i << 8) & 0xFF00 | j & 0x00FF);
        System.out.println(k);
    }
}
