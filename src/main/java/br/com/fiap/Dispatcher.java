package br.com.fiap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Dispatcher {
	
	private static Dispatcher instance;
    private Set<Listener> listeners;
 
    private Dispatcher() {
        this.listeners = new HashSet<Listener>();
    }
 
    public static Dispatcher instancia() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }
 
    public void adicionar(Listener listener) {
        this.listeners.add(listener);
    }
 
    public void DispararEvento(Buscador busca) {
        Iterator<Listener> iterator = listeners.iterator();
 
        while (iterator.hasNext()) {
            Listener listener = (Listener) iterator.next();
            listener.buscaSolicitada(busca);
        }
    }

}
