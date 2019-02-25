package com.yesmywine.orders.service.impl;

import com.yesmywine.orders.service.FIFOService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by light on 2017/3/8.
 */
public class FIFOImpl<T> extends LinkedList<T> implements FIFOService<T> {
    private int maxSize = Integer.MAX_VALUE;
    private final Object synObj = new Object();

    public FIFOImpl() {
        super();
    }

    public boolean add(T t){
        if(this.size()>= this.maxSize){
            return false;
        }
        return super.add(t);
    }

    public FIFOImpl(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public T addLastSafe(T addLast) {
        synchronized (synObj) {
            T head = null;
            while (size() >= maxSize) {
                head = poll();
            }
            addLast(addLast);
            return head;
        }
    }

    @Override
    public T pollSafe() {
        synchronized (synObj) {
            return poll();
        }
    }

    @Override
    public List<T> setMaxSize(int maxSize) {
        List<T> list = null;
        if (maxSize < this.maxSize) {
            list = new ArrayList<T>();
            synchronized (synObj) {
                while (size() > maxSize) {
                    list.add(poll());
                }
            }
        }
        this.maxSize = maxSize;
        return list;
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }
}
