# leitura-arquivo-afd


Leitura de Arquivo no formato AFD, conforme portaria 1510

Conforme: https://www.trt2.jus.br/geral/tribunal2/ORGAOS/MTE/Portaria/ANEXOI_PORT_1510_MTE.pdf
exemplo afd: https://github.com/convenia/afd-reader/blob/master/tests/afd_test.txt

Utilizado:

spring-batch

Para Inicializar:

arquivoAfd=file:files/exemplo_completo.txt

## invocar o serviço

url: 

POST localhost:8080/run

body:

{
    "name": "ArquivoAFDJob",// deve ser o nome do Bean do JOB
    "jobParameters": {
        "arquivoAfd": "file:D:\\repositorios\\leitura-arquivo-afd\\files\\exemplo_completo.txt" //como parâmetro passar o arquivo 
    }
}

Result:

{
    "exitCode": "COMPLETED",
    "exitDescription": "",
    "running": false
}