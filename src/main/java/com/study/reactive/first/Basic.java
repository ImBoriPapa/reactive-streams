package com.study.reactive.first;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
@Slf4j
public class Basic {
    /**
     * foreach 에는 Iterable 을 쓸 수 있다
     * Iterable <---> Observable (duality)
     * Pull           Push
     * <p>
     * 문제점
     * 1. Complete 가 없다.
     * 2. Error 처리는 어떻게??
     */
    public static void main(String[] args) {

        Observer observer = MyObserver.myObserver;

        MyObservable io = new MyObservable();

        io.addObserver(observer);

        ExecutorService es = Executors.newSingleThreadExecutor();

        es.execute(io);

        es.shutdown();

    }

    /**
     * Observable의 변경사항을 출력하는 update()를 구현 했습니다.
     */
    public static class MyObserver {
        //update() 구현
        public static Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println("Observer Thread name= " + Thread.currentThread().getName() + ", count= " + arg);
            }
        };
        //Lambda expression 으로 구현
        public static Observer myObserver = (Observable o, Object arg) ->
                System.out.println("Observer Thread name= " + Thread.currentThread().getName() + ", count= " + arg);
    }

    /**
     * Observable 을 상속 받아 Observer에게 변경 사항을 알려주는 기능을  Runnable의 Run()으로 구현
     */
    public static class MyObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }

    /**
     * Iterable
     * hasNext()가 참일 때까지 next()를 반환 합니다.
     */
    public static void myIterator() {
        Iterable<Integer> iter = () ->
                new Iterator<>() {
                    int i = 0;
                    final static int MAX = 5;

                    public boolean hasNext() {
                        return i < MAX;
                    }

                    public Integer next() {
                        return ++i;
                    }
                };

        for (Iterator<Integer> it = iter.iterator(); it.hasNext(); ) {
            String thread = Thread.currentThread().getName();
            Integer next = it.next();
            System.out.println("Iterator Thread name= " + thread + ", count= " + next);
        }

    }
}
