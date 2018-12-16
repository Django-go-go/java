package com.jkingone.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ResultTask<V> implements RunnableFuture<V> {

    private volatile AtomicInteger state = new AtomicInteger(-1);
    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;
    private static final int EXCEPTIONAL = 3;
    private static final int CANCELLED = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED = 6;

    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition condition = reentrantLock.newCondition();

    private Callable<V> callable;
    private Object outcome;
    private volatile Thread runner;
    private volatile WaitNode waiters;

    public ResultTask(Callable<V> callable) {
        if (callable == null)
            throw new NullPointerException();
        this.callable = callable;
        this.state.set(NEW);
    }

    public ResultTask(Runnable runnable, V result) {
        this.callable = Executors.callable(runnable, result);
        this.state.set(NEW);
    }

    @Override
    public void run() {
        if (state.get() != NEW) {
            synchronized (ResultTask.class) {
                if (runner == null) {
                    runner = Thread.currentThread();
                } else {
                    return;
                }
            }
            return;
        }

        try {
            Callable<V> c = callable;

            if (c != null && state.get() == NEW) {
                V result;
                boolean ran;

                try {
                    result = c.call();
                    ran = true;
                } catch (Throwable e) {
                    ran = false;
                    result = null;
                    setException(e);
                }
                if (ran) {
                    setResult(result);
                }
            }
        } finally {
            runner = null;
            int s = state.get();
            if (s >= INTERRUPTING) {
                while (s == INTERRUPTING) {
                    Thread.yield();
                }
            }
        }
    }

    private void setResult(V o) {
        if (state.compareAndSet(NEW, COMPLETING)) {
            outcome = o;
            state.set(NORMAL);
            condition.signalAll();
//            finishCompletion();
        }
    }

    private void setException(Throwable e) {
        if (state.compareAndSet(NEW, COMPLETING)) {
            outcome = e;
            state.set(EXCEPTIONAL);
            condition.signalAll();
//            finishCompletion();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!(state.get() == NEW && state.compareAndSet(NEW, mayInterruptIfRunning ? INTERRUPTING : CANCELLED))) {
            return false;
        }

        try {
            if (mayInterruptIfRunning) {
                try {
                    Thread thread = runner;
                    if (thread != null) {
                        thread.interrupt();
                    }
                } finally {
                    state.set(INTERRUPTED);
                }

            }
        } finally {
            condition.signalAll();
//            finishCompletion();
        }

        return true;
    }

    @Override
    public boolean isCancelled() {
        return state.get() >= CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state.get() != NEW;
    }

    private V report(int s) throws ExecutionException {
        Object x = outcome;
        System.out.println(Thread.currentThread() + " " + s);

        if (s == NORMAL) {
            return (V) x;
        }
        if (s >= CANCELLED) {
            throw new CancellationException();
        }

        throw new ExecutionException((Throwable) x);
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        int s = state.get();
        if (s <= COMPLETING) {
            s = awaitDone(false, 0L);
        }
        return report(s);
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (unit == null) {
            throw new NullPointerException();
        }
        int s = state.get();
        if (s <= COMPLETING && (s = awaitDone(true, unit.toNanos(timeout))) <= COMPLETING) {
            throw new TimeoutException();
        }
        return report(s);
    }

    private int awaitDone(boolean timed, long nanos) throws InterruptedException {
        final long deadline = timed ? System.nanoTime() + nanos : 0L;
        WaitNode node = null;
        boolean queued = false;

        reentrantLock.lock();

        try {
            for (; ; ) {
                if (Thread.interrupted()) {
//                removeWaiter(node);
                    print("Thread.interrupted()");
                    throw new InterruptedException();
                }

                int s = state.get();

                if (s > COMPLETING) {
                    if (node != null) {
                        node.thread = null;
                    }
                    print("s > COMPLETING");
                    return s;
                } else if (s == COMPLETING) {
                    Thread.yield();
                    print("s == COMPLETING");
                }
//            else if (node == null) {
//                node = new WaitNode();
//                print("node == null");
//            } else if (!queued) {
//                synchronized (ResultTask.class) {
//                    if (waiters == null) {
//                        waiters = node;
//                    } else {
//                        node.next = waiters;
//                        waiters = node;
//                    }
//                    queued = true;
//                    System.out.println("queue");
//                }
//            } else if (timed) {
//                nanos = deadline - System.nanoTime();
//                if (nanos <= 0L) {
//                    removeWaiter(node);
//                    return state.get();
//                }
//                LockSupport.parkNanos(this, nanos);
//            }
                else {
                    condition.await();
//                LockSupport.park(this);
                    print("park");
                }
            }
        } finally {
            reentrantLock.unlock();
        }



    }

    private void finishCompletion() {
        // assert state > COMPLETING;
        for (WaitNode q; (q = waiters) != null; ) {
            synchronized (ResultTask.class) {
                if (q == waiters) {
                    waiters = null;
                }
            }
            for (; ; ) {
                Thread t = q.thread;
                if (t != null) {
                    q.thread = null;
                    LockSupport.unpark(t);
                }
                WaitNode next = q.next;
                if (next == null)
                    break;
                q.next = null; // unlink to help gc
                q = next;
            }
            break;
        }

        callable = null;        // to reduce footprint
    }

    static final class WaitNode {
        volatile Thread thread;
        volatile WaitNode next;

        WaitNode() {
            thread = Thread.currentThread();
        }
    }

    private void removeWaiter(WaitNode node) {
        if (node != null) {
            node.thread = null;
            retry:
            for (; ; ) {          // restart on removeWaiter race
                for (WaitNode pred = null, q = waiters, s; q != null; q = s) {
                    s = q.next;
                    if (q.thread != null)
                        pred = q;
                    else if (pred != null) {
                        pred.next = s;
                        if (pred.thread == null) // check for race
                            continue retry;
                    } else {
                        synchronized (ResultTask.class) {
                            if (waiters == q) {
                                waiters = s;
                            } else {
                                continue retry;
                            }
                        }
                    }

                }
                break;
            }
        }
    }

    private static void print(String info) {
        System.out.println(Thread.currentThread() + " " + info);
    }

}
