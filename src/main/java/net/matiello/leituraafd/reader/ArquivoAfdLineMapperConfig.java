package net.matiello.leituraafd.reader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;

import net.matiello.leituraafd.dominio.CabecalhoAfd;
import net.matiello.leituraafd.dominio.InclusaoAlteracaoIndentificacaoEmpresa;
import net.matiello.leituraafd.dominio.RegistroAjusteRelogioTempoReal;
import net.matiello.leituraafd.dominio.RegistroCUDEmpregadoREP;
import net.matiello.leituraafd.dominio.RegistroMarcaoPonto;
import net.matiello.leituraafd.dominio.TipoEmpregador;
import net.matiello.leituraafd.dominio.TipoOperacao;
import net.matiello.leituraafd.dominio.Trailer;

@Configuration
public class ArquivoAfdLineMapperConfig {

	@Autowired
	FieldSetUtils fieldSetUtils;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public PatternMatchingCompositeLineMapper lineMapper() {
		// Esses transformam a linha em palavras
		Map<String, LineTokenizer> tokenizers = new HashMap<>();
		tokenizers.put("?????????1*", cabecalhoTokenizer());
		tokenizers.put("?????????2*", inclusaoAlteracaoIdentEmpTokenizer());
		tokenizers.put("?????????3*", marcaoPontoTokenizer());
		tokenizers.put("?????????4*", registroAjusteRelogioTokenizer());
		tokenizers.put("?????????5*", registroCUDEmpregadoREPTokenizer());
		tokenizers.put("999999999*", trailerTokenizer());
		// Esses mapeiam as palavras num objeto de domínio
		Map<String, FieldSetMapper> fieldSetMappers = new HashMap<>();
		fieldSetMappers.put("?????????1*", cabecalhoFieldSetMapper());
		fieldSetMappers.put("?????????2*", inclusaoAlteracaoIdentEmpFieldSetMapper());
		fieldSetMappers.put("?????????3*", marcaoPontoFieldSetMapper());
		fieldSetMappers.put("?????????4*", registroAjusteRelogioFieldSetMapper());
		fieldSetMappers.put("?????????5*", registroCUDEmpregadoREPFieldSetMapper());
		fieldSetMappers.put("999999999*", trailerFieldSetMapper());

		// Esse mapeador de linha do framework utiliza padrões para decidir qual lógica
		// de mapeamento será executada.
		PatternMatchingCompositeLineMapper lineMapper = new PatternMatchingCompositeLineMapper<>();
		lineMapper.setTokenizers(tokenizers);
		lineMapper.setFieldSetMappers(fieldSetMappers);
		return lineMapper;
	}


	private LineTokenizer marcaoPontoTokenizer() {
		FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
		lineTokenizer.setNames("nsr", "tipo", "data", "hora", "pis");
		lineTokenizer.setColumns( //
				new Range(1, 9), // 001-009 9 numérico NSR.
				new Range(10, 10), // 010-010 1 alfanumérico tipo do registro, “3”.
				new Range(11, 18), // 011-018 8 numérico Data da gravação, no formata “ddmmaaaa”.
				new Range(19, 22), // 019-022 4 numérico Horário da gravação, no formato “hhmm”
				new Range(23, 34) // 023-034 12 numérico Número do PIS do empregado.
		);
		return lineTokenizer;
	}

	private FieldSetMapper<RegistroMarcaoPonto> marcaoPontoFieldSetMapper() {
		return new FieldSetMapper<RegistroMarcaoPonto>() {

			@Override
			public RegistroMarcaoPonto mapFieldSet(FieldSet fieldSet) throws BindException {
				RegistroMarcaoPonto registroMarcaoPonto = new RegistroMarcaoPonto();
				registroMarcaoPonto.setNsr(fieldSet.readInt("nsr"));
				registroMarcaoPonto.setTipo(fieldSet.readByte("tipo"));
				registroMarcaoPonto.setData(fieldSetUtils.readData("data", fieldSet));
				registroMarcaoPonto.setHora(fieldSetUtils.readTime("hora", fieldSet));
				registroMarcaoPonto.setPis(fieldSet.readLong("pis"));
				return registroMarcaoPonto;
			}

		};
	}


	private LineTokenizer inclusaoAlteracaoIdentEmpTokenizer() {
		FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
		lineTokenizer.setNames("nsr", "tipo", "data", "hora", "tipoEmpregador", "ceiEmpregador", "razaoSocial",
				"local");
		lineTokenizer.setColumns( //
				new Range(1, 9), // 001-009 9 numérico NSR.
				new Range(10, 10), // 010-010 1 numérico Tipo do registro, “2”.
				new Range(11, 18), // 011-018 8 numérico Data da gravação, no formata “ddmmaaaa”.
				new Range(19, 22), // 019-022 4 numérico Horário da gravação, no formato “hhmm”
				new Range(23, 23), // 023-023 1 numérico Tipo de identificador do empregador, “1”, para CNPJ ou
									// “2” para CPF.
				new Range(24, 37), // 024-037 14 numérico CNPJ ou CPF do empregador.
				new Range(38, 49), // 038-049 12 numérico CEI do empregador, quando existir.
				new Range(50, 199), // 050-199 150 alfanumérico Razão social ou nome do empregador.
				new Range(200, 299)// 200-299 100 alfanumérico Local de prestação de serviços
		);
		lineTokenizer.setStrict(false);
		return lineTokenizer;
	}

	private FieldSetMapper<InclusaoAlteracaoIndentificacaoEmpresa> inclusaoAlteracaoIdentEmpFieldSetMapper() {
		return new FieldSetMapper<InclusaoAlteracaoIndentificacaoEmpresa>() {

			@Override
			public InclusaoAlteracaoIndentificacaoEmpresa mapFieldSet(FieldSet fieldSet) throws BindException {
				InclusaoAlteracaoIndentificacaoEmpresa registroMarcaoPonto = new InclusaoAlteracaoIndentificacaoEmpresa();
				registroMarcaoPonto.setNsr(fieldSet.readInt("nsr"));
				registroMarcaoPonto.setTipo(fieldSet.readByte("tipo"));
				registroMarcaoPonto.setData(fieldSetUtils.readData("data", fieldSet));
				registroMarcaoPonto.setHora(fieldSetUtils.readTime("hora", fieldSet));
				byte tipoEmpregador = fieldSet.readByte("tipoEmpregador");
				registroMarcaoPonto.setTipoEmpregador(TipoEmpregador.getByCode(tipoEmpregador));
				registroMarcaoPonto.setCeiIEmpregador(fieldSet.readInt("ceiEmpregador"));
				registroMarcaoPonto.setRazaoSocial(fieldSet.readString("razaoSocial"));
				registroMarcaoPonto.setLocal(fieldSet.readString("local"));

				return registroMarcaoPonto;
			}
		};
	}

	private LineTokenizer cabecalhoTokenizer() {
		FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
		lineTokenizer.setNames("nsr", "tipo", "tipoEmpregador", "cpfCnpj", "ceiEmpregador", "razaoSocial",
				"numeroFabricacao", //
				"dataInicialRegistro", "dataFinalRegistro", "dataGeracao", "horaGeracao");
		lineTokenizer.setColumns( //
				new Range(1, 9), // 001-009 9 numérico “000000000”.
				new Range(10, 10), // 011-011 Tipo de identificador do empregador, “1”
				new Range(11, 11), // 011-011 Tipo de identificador do empregador, “1”
				new Range(12, 25), // 012-025 14 numérico CNPJ ou CPF do empregador.
				new Range(26, 37), // 026-037 12 numérico CEI do empregador, quando existir.
				new Range(38, 187), // 038-187 150 alfanumérico Razão social ou nome do empregador.
				new Range(188, 204), // 188-204 17 numérico Número de fabricação do REP.
				new Range(205, 212), // 205-212 8 numérico Data inicial dos registros no arquivo, no formato
										// “ddmmaaaa”.
				new Range(213, 220), // 213-220 Data final dos registros no arquivo, no formato “ddmmaaaa”.
				new Range(221, 228), // 221-228 Data de geração do arquivo, no formato "ddmmaaaa"
				new Range(229, 232)// 229-232 Horário da geração do arquivo, no formato “hhmm”.
		);
		return lineTokenizer;
	}

	private FieldSetMapper<CabecalhoAfd> cabecalhoFieldSetMapper() {
		return new FieldSetMapper<CabecalhoAfd>() {

			@Override
			public CabecalhoAfd mapFieldSet(FieldSet fieldSet) throws BindException {
				CabecalhoAfd cabecaolho = new CabecalhoAfd();
				cabecaolho.setNumerico(fieldSet.readInt("nsr"));
				cabecaolho.setTipo(fieldSet.readByte("tipo"));
				cabecaolho.setTipoEmpregador(TipoEmpregador.getByCode(fieldSet.readByte("tipoEmpregador")));
				cabecaolho.setCpfOuCnpj(fieldSet.readLong("cpfCnpj"));
				cabecaolho.setCeiEmpregador(fieldSet.readLong("ceiEmpregador"));
				cabecaolho.setRazaoSocial(fieldSet.readString("razaoSocial"));
				cabecaolho.setNumeroFabricacaoRep(fieldSet.readLong("numeroFabricacao"));
				cabecaolho.setDataInicialRegistrosArquivo(fieldSetUtils.readData("dataInicialRegistro", fieldSet));
				cabecaolho.setDataFinalRegistrosArquivo(fieldSetUtils.readData("dataFinalRegistro", fieldSet));
				cabecaolho.setDataGeracaoArquivo(fieldSetUtils.readData("dataGeracao", fieldSet));
				cabecaolho.setHorarioGeracaoArquivo(fieldSetUtils.readTime("horaGeracao", fieldSet));

				return cabecaolho;
			}

		};
	}

	private LineTokenizer registroAjusteRelogioTokenizer() {
		FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
		lineTokenizer.setNames("nsr", "tipo", "dataAntesAjuste", "horarioAntesAjuste", "dataAjustada",
				"horarioAjustado");
		lineTokenizer.setColumns( //
				new Range(1, 9), // 001-009 9 numérico NSR.
				new Range(10, 10), // 010-010 1 alfanumérico tipo do registro, “4”.
				new Range(11, 18), // 011-018 Data antes do ajuste, no formato “ddmmaaaa”.
				new Range(19, 22), // 019-022 Horário antes do ajuste, no formato
				new Range(23, 30), // 023-030 Data ajustada, no formato “ddmmaaaa”.
				new Range(31, 34) // 031-034 Horário ajustado, no formato “hhmm”.
		);
		return lineTokenizer;
	}

	private FieldSetMapper<RegistroAjusteRelogioTempoReal> registroAjusteRelogioFieldSetMapper() {
		return new FieldSetMapper<RegistroAjusteRelogioTempoReal>() {

			@Override
			public RegistroAjusteRelogioTempoReal mapFieldSet(FieldSet fieldSet) throws BindException {
				RegistroAjusteRelogioTempoReal registroAjusteRelogioTempoReal = new RegistroAjusteRelogioTempoReal();
				registroAjusteRelogioTempoReal.setNsr(fieldSet.readInt("nsr"));
				registroAjusteRelogioTempoReal.setTipo(fieldSet.readByte("tipo"));
				registroAjusteRelogioTempoReal.setDataAntesAjuste(fieldSetUtils.readData("dataAntesAjuste", fieldSet));
				registroAjusteRelogioTempoReal
						.setHorarioAntesAjuste(fieldSetUtils.readTime("horarioAntesAjuste", fieldSet));
				registroAjusteRelogioTempoReal.setDataAjustada(fieldSetUtils.readData("dataAjustada", fieldSet));
				registroAjusteRelogioTempoReal.setHorarioAjustado(fieldSetUtils.readTime("horarioAjustado", fieldSet));
				return registroAjusteRelogioTempoReal;
			}

		};
	}

	private LineTokenizer registroCUDEmpregadoREPTokenizer() {
		FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
		lineTokenizer.setNames("nsr", "tipo", "dataGravacao", "horarioGravacao", "operacao", "pis", "nome");
		lineTokenizer.setColumns( //
				new Range(1, 9), // 001-009 9 numérico NSR.
				new Range(10, 10), // 010-010 1 alfanumérico tipo do registro, “3”.
				new Range(11, 18), // 011-018 8 numérico Data da gravação, no formata “ddmmaaaa”.
				new Range(19, 22), // 019-022 4 numérico Horário da gravação, no formato “hhmm”
				new Range(23, 23), // 023-023 Tipo de operação, “I” para inclusão, “A” para alteração e “E” para
									// exclusão.
				new Range(24, 35), // 024-035 12 numérico Número do PIS do empregado.
				new Range(36, 87) // 036-087 52 alfanumérico Nome do empregado.
		);
		lineTokenizer.setStrict(false);
		return lineTokenizer;
	}

	private FieldSetMapper<RegistroCUDEmpregadoREP> registroCUDEmpregadoREPFieldSetMapper() {
		return new FieldSetMapper<RegistroCUDEmpregadoREP>() {

			@Override
			public RegistroCUDEmpregadoREP mapFieldSet(FieldSet fieldSet) throws BindException {
				RegistroCUDEmpregadoREP registroCUDEmpregadoREP = new RegistroCUDEmpregadoREP();
				registroCUDEmpregadoREP.setNsr(fieldSet.readInt("nsr"));
				registroCUDEmpregadoREP.setTipo(fieldSet.readByte("tipo"));
				registroCUDEmpregadoREP.setDataGravacao(fieldSetUtils.readData("dataGravacao", fieldSet));
				registroCUDEmpregadoREP.setHorarioGravacao(fieldSetUtils.readTime("horarioGravacao", fieldSet));
				registroCUDEmpregadoREP.setOperacao(TipoOperacao.getByTipo(fieldSet.readChar("operacao")));
				registroCUDEmpregadoREP.setPis(fieldSet.readLong("pis"));
				registroCUDEmpregadoREP.setNome(fieldSet.readString("nome"));
				return registroCUDEmpregadoREP;
			}

		};
	}

	private LineTokenizer trailerTokenizer() {
		FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
		lineTokenizer.setNames("numerico", "quantidadeTipo2", "quantidadeTipo3", "quantidadeTipo4", "quantidadeTipo5",
				"tipo");
		lineTokenizer.setColumns( //
				new Range(1, 9), // 001-009 9 numérico “999999999”.
				new Range(10, 18), // 010-018 9 numérico Quantidade de registros tipo “2” no arquivo.
				new Range(19, 27), // 019-027 9 numérico Quantidade de registros tipo “3” no arquivo.
				new Range(28, 36), // 028-036 9 numérico Quantidade de registros tipo “4” no arquivo.
				new Range(37, 45), // 037-045 9 numérico Quantidade de registros tipo “5” no arquivo.
				new Range(46, 46) // 046-046 1 numérico Tipo do registro, “9”.
		);
		return lineTokenizer;
	}

	private FieldSetMapper<Trailer> trailerFieldSetMapper() {
		return new FieldSetMapper<Trailer>() {

			@Override
			public Trailer mapFieldSet(FieldSet fieldSet) throws BindException {
				Trailer trailer = new Trailer();
				trailer.setNumerico(fieldSet.readLong("numerico"));
				trailer.setRegistroTipo2(fieldSet.readLong("quantidadeTipo2"));
				trailer.setRegistroTipo3(fieldSet.readLong("quantidadeTipo3"));
				trailer.setRegistroTipo4(fieldSet.readLong("quantidadeTipo4"));
				trailer.setRegistroTipo5(fieldSet.readLong("quantidadeTipo5"));
				trailer.setTipoRegistro(fieldSet.readByte("tipo"));
				return trailer;
			}

		};
	}

}