package net.matiello.leituraafd.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trailer {

	long numerico;
	long registroTipo2;
	long registroTipo3;
	long registroTipo4;
	long registroTipo5;
	byte tipoRegistro;
}
