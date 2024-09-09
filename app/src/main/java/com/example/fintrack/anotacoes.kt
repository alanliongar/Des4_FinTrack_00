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
    /*
    Dia 15: Estudei 3h - 11/08/2024 09:50:00 - Eu inserí a ação do FAB, coloquei função pra inserir a tarefa do usuário no banco de dados, defini o código do clique na lista de tarefas, modifiquei o código de create(orupdate)taskbottomsheet, e coloquei
        pra quando atualizar (clicar na lista), mostrar pro usuário tanto o texto quanto a categoria no spinner e os componentes de ui (escrito update)....
        -Ainda no dia 15: adicionei mais uma hora de estudos (às 13:40), e nela, incluí o código pra atualizar a tarefa decentemente. Parei nos 7:11 da aula "Atualizando tarefa na base de dados"

    Dia 16 -28/08/2024 - Estudei 2h: 10:45 as 12:45 e até agora eu passei quase 1h entendendo que o createbottomsheet é uma classe, e essa classe tem apenas as declarações
    dos botões, tanto de update, create e delete, e os códigos das ações desses botões ficam na mainctivity.
    Também coloquei o código pra deletar "tarefas", assim como atualizar (separado de criar), etc...
     */

    /*
    * Dia 17 - 29/08/2024 - 2h30m de estudos: BS de info, Relação entre tabelas com foreignkey, listando todas e filtrando por categorias - direto da base de dados (com queries)
    * Regra pra selecionar categorias (com queries), selecionando tudo (all) com query, e seleção de spinner sem "+" e sem "ALL"
    *
    * Próximos passos: remover o pré-selecionado do spinner e colocar alguma frase pro usuário, e fazer o estado de vazio do app pro usuário começar a mexer nele.
    * */

    /*
    * Dia 18: eu apenas aprendi que o celular quando faz o backup, precisa que a base seja deletada forçosamente.
    * */

    /*Dia 19: 4h de estudos: fiz os retoques "finais" do app, segundo as aulas, falta agora fazer os ajustes próprios do app fintrack.*/
    /*Dia 20: Eu consegui estudar por 1h até agora, jajá vou começar outra sessão. Não andei nada nesse app, mas aprendi como faz o viewbinding pra nao precisar ficar recuperando id*/
    /*
    * Dia 21: Inserí dois recyclerview, e agora preciso saber como faço pra usar eles. O Room não aceita tipo RES, ele aceita somente o tipo INT, preciso entender como corrigir.
    * 1 commit a mais pq esqueci de ajustar
    * */

    /*
    * Dia 22: Hoje aprendi bastante coisa, terminei de implementar os dois recyclerview das cores e dos ícones pro usuário;
    * aprendi como funciona o lifeCycleOwner e o lifecycleScope, assim como as diferenças pro globalscope e pro suspendfunction
    * implementei isso pra buscar a categoria diretamente do banco de dados se disponível, preciso tratar os casos do banco vazio ainda.
    * O que falta? - o textview populado com o total das entradas financeiras (filtradas) na tela;
    * Falta deixar o app tôdo bonitão pra fechar o código e publicar no github a versão final.
    * */


}