package net.matiello.leituraafd.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ArquivoAfdClassifierReaderConfig {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	@StepScope
	public FlatFileItemReader arquivoAdfClassifierReader(
			@Value("#{jobParameters['arquivoAfd']}") Resource arquivoClientes, 
			LineMapper lineMapper) {
		return new FlatFileItemReaderBuilder()
				.name("processadorClassifierReader")
				.resource(arquivoClientes)
				.lineMapper(lineMapper).build();

	}

}