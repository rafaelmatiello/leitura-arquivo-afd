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
public class RegistroCUDEmpregadoREP {

	long nsr;
	byte tipo;
	LocalDate dataGravacao;
	LocalTime horarioGravacao;
	TipoOperacao operacao;
	long pis;
	String nome;
}
