package br.com.fiap;

public class Main {

	public static void main(String[] args) {
		
		Listener listener = new ListenerImpl();
		
		Dispatcher.instancia().adicionar(listener);
		
		Dispatcher.instancia().DispararEvento(new Buscador("ActiveMQ"));

	}

}
