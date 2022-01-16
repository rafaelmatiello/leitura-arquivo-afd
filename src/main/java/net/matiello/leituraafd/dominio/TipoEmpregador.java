package net.matiello.leituraafd.dominio;

public enum TipoEmpregador {

	CNPJ((byte) 1), CPF((byte) 2);

	private final byte codigo;

	TipoEmpregador(byte codigo) {
		this.codigo = codigo;
	}

	public static TipoEmpregador getByCode(byte code) {
		switch (code) {
		case 1:
			return CNPJ;
		case 2:
			return CPF;
		default:
			throw new IllegalArgumentException(
					String.format("%s is a invalid code for %s", code, TipoEmpregador.class.getSimpleName()));
		}
	}

	public byte getCodigo() {
		return codigo;
	}

}
