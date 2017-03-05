package br.com.fiap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class Buscador {
    private static Logger logger = Logger.getLogger(Buscador.class);
    private String diretorioIndice = "J:\\indice";
    private String parametro;

    public Buscador(String parametro) {
		super();
		this.parametro = parametro;
	}

	public List<Object> procurar() {
		
		List<Object> resultados =  new ArrayList<>();
		List<String> nomes = new ArrayList<>();
		List<String> caminhos = new ArrayList<>();
		
        try {
            Directory diretorio = new SimpleFSDirectory(new File(diretorioIndice));
            IndexReader leitor = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(leitor);
            Analyzer analisador = new StandardAnalyzer(Version.LUCENE_48);
            QueryParser parser = new QueryParser(Version.LUCENE_48, "Texto", analisador);
            Query consulta = parser.parse(parametro);
            TopDocs resultado = buscador.search(consulta, 100);
            int totalDeOcorrencias = resultado.totalHits;          
            for (ScoreDoc sd : resultado.scoreDocs) {
                Document documento = buscador.doc(sd.doc);
                nomes.add(documento.get("Nome"));
                caminhos.add(documento.get("Caminho"));
            }
            resultados.add(totalDeOcorrencias); 
            resultados.add(nomes);
            resultados.add(caminhos);
            leitor.close();
            
        } catch (Exception e) {
            logger.error(e);
        }
        
        return resultados;
    }
}