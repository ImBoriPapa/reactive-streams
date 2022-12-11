package com.study.reactive.first;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.study.reactive.first.Basic.myIterator;
import static org.junit.jupiter.api.Assertions.*;


class BasicTest {



    @Test
    @DisplayName("Iterator 구현")
    void iter() throws Exception{
        //given
        myIterator();
    }

    @Test
    @DisplayName("Observer Test")
    void observer() throws Exception{
        //given
        Basic.MyObservable observable = new Basic.MyObservable();
        //when
        observable.addObserver(Basic.MyObserver.myObserver);
        //then
        observable.run();
    }

    @Test
    @DisplayName("Iterator와 Observer")
    void iteratorAndObserver() throws Exception{
        //given
        Basic.MyObservable observable = new Basic.MyObservable();
        //when
        observable.addObserver(Basic.MyObserver.myObserver);
        //then
        myIterator();
        observable.run();
    }

    @Test
    @DisplayName("옵져버")
    void manyObserver() throws Exception{
        //given
        Basic.MyObservable observable1 = new Basic.MyObservable();
        Basic.MyObservable observable2 = new Basic.MyObservable();
        Basic.MyObservable observable3 = new Basic.MyObservable();

        observable1.addObserver(Basic.MyObserver.myObserver);
        observable2.addObserver(Basic.MyObserver.myObserver);
        observable3.addObserver(Basic.MyObserver.myObserver);
        //when
        //then
        System.out.println("====[observable1]===");
        observable1.run();
        System.out.println("====[observable2]===");
        observable2.run();
        System.out.println("====[observable3]===");
        observable3.run();


    }
}