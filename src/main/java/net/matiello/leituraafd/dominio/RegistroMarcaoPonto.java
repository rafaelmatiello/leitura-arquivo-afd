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
public class RegistroMarcaoPonto {

	Integer nsr;
	Byte tipo;
	LocalDate data;
	LocalTime hora;
	Long pis;

}
