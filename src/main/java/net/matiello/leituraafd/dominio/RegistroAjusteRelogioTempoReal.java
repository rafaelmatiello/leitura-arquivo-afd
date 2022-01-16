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
public class RegistroAjusteRelogioTempoReal {

	private long nsr;
	private byte tipo;
	private LocalDate dataAntesAjuste;
	private LocalTime horarioAntesAjuste;
	private LocalDate dataAjustada;
	private LocalTime horarioAjustado;

}
