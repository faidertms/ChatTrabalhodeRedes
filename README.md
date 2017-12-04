# Chat Trabalho de Redes
Chat Trabalho de Redes
- Existe 2 branch (um para a parte servidor e outra para parte Cliente).

Equipe:
- Rodrigo Gomes - 1320661/9
- Thiago Sales  - 1410702/9

Linguagem usada: Java
- Versão do Java deve conter a biblioteca do javaFX
- O Eclipse foi a IDE utilizada.

Execução do programa
- Executar servidor
- Executar cliente e coloca o seu nick.
- A sala inicial será aberta no qual não há administrador por padrão.
- Na interface é possível visualizar um comboBox com as salas diponiveis e no lado um botão para criar a sala, quando este é clicado o servidor irá criar uma nova sala publica no qual a pessoa que criou vai ser o administrador.
- No lado direito da tela, tem uma lista de usuários conectado na sala e com seus respectivos status (pela cor).
- Existem 2 botões de saída, um para sair de apenas uma sala e outra para sair de todas as outras voltando para tela de “login”. Caso a seja a última sala e o usuário aperte o botão sairSala o mesmo será redirecionado para tela de “login”, se o usuário realmente deseja sair da aplicação o botão X da janela deverá ser usado.
- Existe um botão de kick presente para todo usuário, mas apenas terá ação para o administrador, ou seja, mesmo que o usuário que não é o administrador clique, a ação não será aceita pelo servidor.
- Para executar um kick primeiro a pessoa deve selecionar o usuário na lista da sala e depois apertar o botão kick.
- Caso a pessoa clique 2 vezes no nome de outro usuário, uma sala privada será criada com os 2 usuários.
- A interface do chat privado tem menos ação, nela o usuário não pode conectar a outras salas a partir dela, sendo unicamente exclusiva para conversar com outro usuário de sua escolha.
- No servidor caso deseje remover uma pessoa, basta selecionar e apertar em remover.

Funcionalidades Implementadas
- CRIPTOGRAFIA RSA/DES
- Interface gráfica para aplicação cliente e poder enviar mensagem privada.
- Interface gráfica para aplicação servidor mostrando todos os usuários com suas informações, podendo remover do servidor.
- Usuário pode criar uma nova sala de bate papo publica no qual ele é o administrador podendo dar um kick em qualquer usuário presente nessa sala.
- Usuário pode entrar em diversas salas publicas ao mesmo tempo.
- Na sala de bate papo é mostrado uma coloração que indica o status do usuário, verde para disponível, amarelo para ocupado, vermelho para ausente.

Funcionameto do programa
- ControllerFactory e FactoryController é a classe responsável por armazenar e manipular os nodes(stages, scene e controller) do JavaFx, apartir dela pode-se criar ou manipular qualquer tela em qualquer outra tela.
- Funcionamento do cliente(código):
Criação de uma tela login com suas respectivas ações no LoginController.
Login só tem uma unica ação, quando o usuário adiciona um nome e clica em entrar, se ele já existir no servidor, um aviso será emitido, caso não tenha nome igual, uma nova tela(LayoutController) será aberta, passando para essa tela a conexão aberta anteriormente e o nome do usuário, como é a primeira vez, uma thread (chatHandler) será criada  para controlar as chegadas das mensagens, assim que essa mensagem chega essa thread cria uma outra thread(GuiHandler) para manipular a mensagem e o GUI da tela de acordo com o tipo da mensagem, essa thread é executada uma unica vez diferente do chatHandler que continua ate o usuário finalizar a execução.
Existe dois tipos de Layout, o normal que é para salas públicas e o individual que é para chat particular. O normal tem diversas ações como sair da sala, sair total, criar nova sala, entrar em outra sala (essa cria uma nova tela com a sala selecionada pelo usuário), abrirConversa(conversa privada com outro usuário), enviar mensagem(essa mensagem so vai pra sala da tela), kick caso seja administrador da sala e atualizar estado(status) do usuário. No individual so tem duas ações, enviar mensagem e sair conversa, bem simples, lembrando todos compartilham a mesma conexão(socket).
- Funcionamento do servidor(código):
Criação de um serverSocket e uma sala inicial sem administrador logo nenhum usuário pode dar kick nesta sala, após isso o servidor fica em loop esperando novas conexões, cada conexão aceita é tratada por uma thread , ou seja, mil usuários igual mil threads, dentro dessa thread(clientHandler), ele será um loop esperando por mensagens enviado pelo usuário e irá tratar de acordo com tipo de mensagem lembrando que essas threads compartilham as mesma lista de salas e de usuários totais, dentro de cada sala tem um lista de usuários ativo nela e seu administrador, e dentro do usuário tem seu socket, ObjectoutputStream, nome , seu ip e a lista de sala no qual esse usuário esta conectado.
Servidor tem um Gui simples, que roda em uma thread a parte da que manipula as conexão do servidor, nesse gui é possivel remover qualquer usuário do servidor fechando seu socket, antes disso uma mensagem é enviada pro usuário avisando que foi removido pelo administrador do servidor.
