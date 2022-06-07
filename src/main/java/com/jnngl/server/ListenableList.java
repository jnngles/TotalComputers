package com.jnngl.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListenableList<E> extends ArrayList<E> {

    public interface Listener<E> {
        default void onRemove(Object element) {}
        default void onAdd(E element) {}
    }

    private final List<Listener<E>> listeners;

    public ListenableList(int initialCapacity) {
        super(initialCapacity);

        listeners = new ArrayList<>();
    }

    public ListenableList() {
        super();

        listeners = new ArrayList<>();
    }

    public ListenableList(Collection<? extends E> c) {
        super(c);

        listeners = new ArrayList<>();
    }

    public void addListener(Listener<E> listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener<E> listener) {
        this.listeners.remove(listener);
    }

    public void removeListener(int idx){
        this.listeners.remove(idx);
    }

    public void clearListeners(){
        this.listeners.clear();
    }

    @Override
    public boolean add(E e) {
        for (Listener<E> listener : listeners) {
            listener.onAdd(e);
        }
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        for (Listener<E> listener : listeners) {
            listener.onRemove(o);
        }

        return super.remove(o);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E o : c) {
            for (Listener<E> listener : listeners) {
                listener.onAdd(o);
            }
        }
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E o : c) {
            for (Listener<E> listener : listeners) {
                listener.onAdd(o);
            }
        }
        return super.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            for (Listener<E> listener : listeners) {
                listener.onRemove(o);
            }
        }
        return super.removeAll(c);
    }

    @Override
    public void add(int index, E element) {
        for (Listener<E> listener : listeners) {
            listener.onAdd(element);
        }

        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        E element = super.remove(index);

        for (Listener<E> listener : listeners) {
            listener.onRemove(element);
        }

        return element;
    }

}