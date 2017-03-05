package br.com.fiap;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;

public class Indexador {
	private static Logger logger = Logger.getLogger(Indexador.class);
	private String diretorioDosIndices = "J:\\indice";
	private String diretorioParaIndexar = "J:\\teste";
	private IndexWriter writer;
	private Tika tika;

	public static void main(String[] args) {
		Indexador indexador = new Indexador();
		indexador.indexarArquivos();
	}

	public void indexarArquivos() {
		try {
			File diretorio = new File(diretorioDosIndices);
			apagarIndices(diretorio);
			Directory d = new SimpleFSDirectory(diretorio);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48,
					analyzer);
			writer = new IndexWriter(d, config);
			indexarArquivos(new File(diretorioParaIndexar));
			writer.commit();
			writer.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	private void apagarIndices(File diretorio) {
		if (diretorio.exists()) {
			File arquivos[] = diretorio.listFiles();
			for (File arquivo : arquivos) {
				arquivo.delete();
			}
		}
	}

	public void indexarArquivos(File raiz) {
		FilenameFilter filtro = new FilenameFilter() {
			public boolean accept(File arquivo, String nome) {
				if (nome.toLowerCase().endsWith(".doc") 
						|| nome.toLowerCase().endsWith(".docx")
						|| nome.toLowerCase().endsWith(".pdf")
						|| nome.toLowerCase().endsWith(".ppt")
						|| nome.toLowerCase().endsWith(".pptx")
						|| nome.toLowerCase().endsWith(".odt")
						|| nome.toLowerCase().endsWith(".xls")
						|| nome.toLowerCase().endsWith(".txt")) {
					return true;
				}
				return false;
			}
		};
		for (File arquivo : raiz.listFiles(filtro)) {
			if (arquivo.isFile()) {
				try {
					String textoExtraido = getTika().parseToString(arquivo);
					indexarArquivo(arquivo, textoExtraido);
				} catch (Exception e) {
					logger.error(e);
				}
			} else {
				indexarArquivos(arquivo);
			}
		}
	}

	private void indexarArquivo(File arquivo, String textoExtraido) {
		Document documento = new Document();
		String nome = arquivo.getName();
		int extensao = nome.lastIndexOf(".");
		nome = nome.substring(0, extensao);
		
		documento.add(new TextField("Caminho", arquivo.getAbsolutePath(),
				Store.YES));
		documento.add(new TextField("Texto", textoExtraido,
				Store.YES));
		documento.add(new TextField("Nome", nome,
				Store.YES));
		try {
			getWriter().addDocument(documento);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public Tika getTika() {
		if (tika == null) {
			tika = new Tika();
		}
		return tika;
	}

	public IndexWriter getWriter() {
		return writer;
	}
}