package net.matiello.leituraafd.reader;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class FieldSetUtils {

	public LocalTime readTime(String name, FieldSet fieldSet) {
		Date horaDate = fieldSet.readDate(name, "HHmm");
		return horaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
	}

	public LocalDate readData(String name, FieldSet fieldSet) {
		Date readDate = fieldSet.readDate(name, "ddMMyyyy");
		return readDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
