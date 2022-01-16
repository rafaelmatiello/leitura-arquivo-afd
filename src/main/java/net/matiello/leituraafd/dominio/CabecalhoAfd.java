package net.matiello.leituraafd.dominio;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CabecalhoAfd {

	Integer numerico;
	byte tipo;
	TipoEmpregador tipoEmpregador;
	long cpfOuCnpj;
	long ceiEmpregador;
	String razaoSocial;
	long numeroFabricacaoRep;
	LocalDate dataInicialRegistrosArquivo;
	LocalDate dataFinalRegistrosArquivo;
	LocalDate dataGeracaoArquivo;
	LocalTime horarioGeracaoArquivo;

}

