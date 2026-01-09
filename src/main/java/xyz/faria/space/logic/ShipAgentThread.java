package xyz.faria.space.logic;

public class ShipAgentThread implements Runnable {

    private final ShipAgent agent;

    private Thread thread;

    public ShipAgentThread(ShipAgent agent) {
        this.agent = agent;
    }

    public void start() {
        this.thread = Thread.ofVirtual().start(this);
    }

    @Override
    public void run() {
        try {
            this.agent.updateTick();
            long nextTick = this.agent.getNextUpdateTick();
            long sleepDuration = Long.max(nextTick - System.currentTimeMillis(), 1);
            Thread.sleep(sleepDuration);
            run();
        } catch (Exception e) {
            e.printStackTrace();
            this.thread.interrupt();
            this.thread = null;
        }
    }
}
