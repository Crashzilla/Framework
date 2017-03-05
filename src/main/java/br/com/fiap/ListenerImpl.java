package br.com.fiap;

import java.util.ArrayList;
import java.util.List;

public class ListenerImpl implements Listener{

	@SuppressWarnings("unchecked")
	@Override
	public void buscaSolicitada(Buscador busca) {
		
		//Criando a lista de resultados
		List<Object> resultados = new ArrayList<>();
		
		//indexando a pasta com os documentos
		Indexador indice = new Indexador();
		indice.indexarArquivos();
		
		//buscando e populando a lista de resultados
		resultados = busca.procurar();
		
		//printando resultados
		List<String> nomes = (List<String>) resultados.get(1);
		List<String> caminhos = (List<String>) resultados.get(2);
		System.out.println("Numero de Resultados: " + resultados.get(0));
		for(int n = 0; n < nomes.size(); n++){
			
			System.out.println("Nome: " + nomes.get(n));
			System.out.println("Caminhos: " + caminhos.get(n));
			
		}
		
	}

}
