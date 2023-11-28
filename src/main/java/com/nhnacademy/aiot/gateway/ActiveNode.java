package com.nhnacademy.aiot.gateway;

public abstract class ActiveNode extends Node implements Runnable {
    Thread thread;
    boolean running;
    long interval = 1000;

    ActiveNode() {
        super();
        thread = new Thread(this, getId());
        running = false;
    }

    ActiveNode(String name) {
        this();
        setName(name);
    }

    @Override
    public String getName() {
        return thread.getName();
    }

    @Override
    public void setName(String name) {
        thread.setName(name);
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        running = false;
        thread.interrupt();
    }

    void preprocess() {
        //
    }

    void process() {
        //
    }

    void postprocess() {
        //
    }

    @Override
    public void run() {
        preprocess();

        running = true;

        long startTime = System.currentTimeMillis();
        long previousTime = startTime;

        while (running) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - previousTime;

            if (elapsedTime < interval) {
                try {
                    process();
                    Thread.sleep(interval - elapsedTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            previousTime = startTime + (System.currentTimeMillis() - startTime) / interval * interval;
        }

        postprocess();
    } 
}
