package net.matiello.leituraafd.dominio;

public enum TipoOperacao {

	INCLUSAO('I'), ALTERCAO('A'), EXCLUSAO('E');

	private final char tipo;

	TipoOperacao(char tipo) {
		this.tipo = tipo;
	}

	public static TipoOperacao getByTipo(char value){
		switch (value) {
		case 'I':
			return INCLUSAO;
		case 'A':
			return ALTERCAO;
		case 'E':
			return EXCLUSAO;	
		default:
			throw new IllegalArgumentException(
					String.format("%s é um valor invalido para %s", value, TipoOperacao.class.getSimpleName()));
		}
	}

	public char getTipo() {
		return tipo;
	}

}
