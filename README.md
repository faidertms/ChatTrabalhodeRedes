# ChatTrabalhodeRedes
Chat Trabalho de Redes
Existe 2 branch (um para a parte servidor e outra para parte Cliente)

Equipe:
Rodrigo Gomes - 1320661/9
Thiago Sales  - 1410702/9

Linguagem usada: Java
-Versão do Java deve conter a biblioteca do javaFX

Execução do programa
-Executar servidor
-Executar cliente e coloca o seu nick
-A sala inicial será aberta no qual não há administrador por padrão
-Na interface é possível visualizar um comboBox com as salas diponiveis e no lado um botão para criar a sala, quando este é clicado o servidor irá criar uma nova sala publica no qual a pessoa que criou vai ser o administrador
-No lado direito da tela, tem uma lista de usuários conectado na sala e com seus respectivos status (pela cor).
-Existem 2 botões de saída, um para sair de apenas uma sala e outra para sair de todas as outras voltando para tela de “login”. Caso a seja a última sala e o usuário aperte o botão sairSala o mesmo será redirecionado para tela de “login”, se o usuário realmente deseja sair da aplicação o botão X da janela deverá ser usado.
-Existe um botão de kick presente para todo usuário, mas apenas terá ação para o administrador, ou seja, mesmo que o usuário que não é o administrador clique, a ação não será aceita pelo servidor.
-Para executar um kick primeiro a pessoa deve selecionar o usuário na lista da sala e depois apertar o botão kick.
-Caso a pessoa clique 2 vezes no nome de outro usuário, uma sala privada será criada com os 2 usuários.
-A interface do chat privado tem menos ação, nela o usuário não pode conectar a outras salas a partir dela, sendo unicamente exclusiva para conversar com outro usuário de sua escolha.
-No servidor caso deseje remover uma pessoa, basta selecionar e apertar em remover.

Funcionalidade Implementadas
- Interface gráfica para aplicação cliente e poder enviar mensagem privada.
- Interface gráfica para aplicação servidor mostrando todos os usuários com suas informações, podendo remover do servidor.
- Usuário pode criar uma nova sala de bate papo publica no qual ele é o administrador podendo dar um kick em qualquer usuário presente nessa sala.
- Usuário pode entrar em diversas salas publicas ao mesmo tempo.
- Na sala de bate papo é mostrado uma coloração que indica o status do usuário, verde para disponível, amarelo para ocupado, vermelho para ausente.

