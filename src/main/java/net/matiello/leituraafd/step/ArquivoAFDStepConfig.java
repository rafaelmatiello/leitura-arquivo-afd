package net.matiello.leituraafd.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArquivoAFDStepConfig {
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public Step leituraArquivoAFDStep(
			@Qualifier("arquivoAdfClassifierReader") ItemReader arquivoAdfClassifierReader,
			ItemWriter leituraArquivoAFDRegistroMarcacaoPontoWriter) {
		return stepBuilderFactory
				.get("leituraArquivoAFDRegistroMarcacaoPontoReader")//
				.<Object, Object>chunk(1000)//
				.reader(arquivoAdfClassifierReader)//
				.writer(leituraArquivoAFDRegistroMarcacaoPontoWriter)//
				.build();
	}
}