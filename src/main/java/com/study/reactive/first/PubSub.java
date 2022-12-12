package com.study.reactive.first;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class PubSub {
    public static void main(String[] args) throws InterruptedException {
        //Publisher  <- Observable
        //Subscriber <- Observer

        List<String> data1 = Arrays.asList("data1", "data2", "data3", "data4", "data5");
        List errorData = Arrays.asList("data1", "data2", 1, "data4", "data5");

        MyPublisher myPublisher = new MyPublisher(data1);

        MySubscriber mySubscriber = new MySubscriber();

        myPublisher.publisher.subscribe(mySubscriber.sub);

    }

    public static class MyPublisher {

        private List data;

        public MyPublisher(List data) {
            this.data = data;
        }

        /**
         * Publisher 구현
         * subscribe(Subscriber s)
         * request(long n)
         * cancel()
         */
        Publisher publisher = new Publisher() {

            @Override
            public void subscribe(Subscriber subscriber) {
                ExecutorService es = Executors.newSingleThreadExecutor();
                Iterator<String> it = data.iterator();

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        es.execute(() -> {
                            int i = 0;
                            try {
                                while (i++ < n) {
                                    if (it.hasNext()) {
                                        subscriber.onNext(it.next());
                                    } else {
                                        subscriber.onComplete();
                                        break;
                                    }
                                }
                            } catch (RuntimeException e) {
                                subscriber.onError(e);
                            }
                        });

                    }

                    @Override
                    public void cancel() {

                    }
                });
                try {
                    es.awaitTermination(10, TimeUnit.HOURS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                es.shutdown();
            }

        };
    }

    public static class MySubscriber {
        private Subscriber<String> sub = new Subscriber<String>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(String item) {
                System.out.println(Thread.currentThread().getName() + " onNext " + item);
                this.subscription.request(1);

            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
    }
}
