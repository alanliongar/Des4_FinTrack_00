package com.example.fintrack

class anotacoes {
    //primeiro dia: eu criei o item do rv de cada dado, parei na criação do item da rv da categoria (horizontal).
//segundo dia: terminei a parte BEMM BASICA dos layouts, e travei na parte da construção do dado: recuperar a cor pro objeto
//terceiro dia: terminei de tirar as dúvidas relacionadas a criação dos objetos quando precisa do contexto (pra cor), e criei o adapter da monylist.
//quarto dia: implementei somente o adapter da categoria, assim como listei 4 categorias. Falta ajustar os itens para ficarem bonitinhos.
//quinto dia: eu fui criar a bottom sheet, mas me embananei tôdo, preciso revisar o raciocínio do roque e olhar o arquivo do whatsapp
    /*
SEXTO DIA: Tentei compilar e deu erro, aí entendi a diferença entre inicializar a variável na prática, e inicializar ela na classe (de clique e clique longo)
Aí decidi que preciso revisitar as aulas e listar o que é feito nelas, pra poder ir aplicando no meu programa
Os proximos passos são: fazer a aplicação completa uma vez, e tentar ir refazendo o projeto algumas vezes, até as aulas se tornarem desnecessárias
o app desde o começo.
*/
//Sétimo dia: depois de procrastinar por 4 dias (vergonha hein?) eu consegui implementar apenas uma entidade e criar o banco de dados do aplicativo. Mas não o declarei nem inicializei na main activity ainda, pq tava revisando as aulas.
//Oitavo dia: Eu terminei a construção bonitinha da parte lógica do banco, mas na hora de implementar na view (o de categoria), não deu pra visualizar. Mandei msg no devspace na dh: 30/07/2024 08:37.
//Nono dia: fiquei 1h tentando entender por que a categoria buscada no banco de dados não tá sendo exibida no recyclerview.
//Próximos passos: mandar a informação dos MONY para o rv; criar a dinamicidade de adições e exclusões de categorias, dados, e as telas de escolha de cores, adições e exclusões...
//Décimo dia: eu descobri que ontem não fiz o commit e pull request direito no github e nao ficou verdinho lá.
    //ainda no décimo dia: inserí os dados no banco de dados, e os proximos passos agora são "regras de negocio"....
//decimo primeiro dia: coloquei a categoria fake "+", coloquei a ação de clique sem eficiencia: criando uma caralhada de lista
    /*
    * Ainda no décimo primeiro dia, aula "plus click para adicionar categoria" - aos 8:35
    * aqui por algum motivo, quando as informações só aparecem quando eu compilo pela SEGUNDA VEZ, na primeira o app fica tôdo vazio
    * Tentar entender com os membros do devspace por que isso está acontecendo. Seria bom entender isso antes de ir pra parte de arquitetura/entendimento de threads
    * --Eu mesmo tirei minha dúvida: utilizar suspend function aqui talvez seja o ideal.
    * O que é uma suspend function? Uma função que quando é chamada, ela interrompe a coroutine (fluxo de execução principal) e roda "em segundo plano", e depois de sua execução, a coroutine volta a ser executada.
    * Decisão: ainda falta entender no detalhe o que é uma suspend function, mas eu vi que na parte de arquitetura (bloco 27) eu vou ver isso no detalhe.
    * */
//Dia 12: dei uma travada pra poder entender o que é suspend function e pra saber que vou ter aula disso mais pra frente em arquitetura
    //continuar: Bloco 24, aula "Criando uma bottom sheet", do começo.
//Dia 13: 06/08/2024 - terça-feira Eu criei apenas a bottom sheet category, e a recuperei. Trata-se de uma view que faz um "toastyyy", apenas isso.
//Dia 14: 07/08/2024 - quarta-feira utilizei a bottom sheet (ação de clique passado pra classe que estende a padrão da bottom sheet) e nessa ação de clique, definimos que uma categoria é criada, inserida no banco de dados e recuperada pra UI.
    //Ainda no dia 14: coloquei o float action button no layout principal, setei um id pra ele, e parei na aula "bottom sheet de tarefas", em 1 minuto.
}