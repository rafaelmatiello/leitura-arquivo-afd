package net.matiello.leituraafd.dominio;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InclusaoAlteracaoIndentificacaoEmpresa {

	Integer nsr;
	Byte tipo;
	LocalDate data;
	LocalTime hora;
	TipoEmpregador tipoEmpregador;
	Integer ceiIEmpregador;
	String razaoSocial;
	String local;

}
