#!/bin/bash

# Variáveis de conexão
DB_NAME="campusresolve"
DB_USER="ifpe"
DB_PASS="ifpe"

# Dados do usuário a ser inserido
MATRICULA="90909090"
NOME="Usuário Builk Insert"
SENHA="3pShjd45Lw#"


# Dados do Servidor
MATRICULA_SERVIDOR="80808080"
NOME_SERVIDOR="Servidor Builk Insert"
SENHA_SERVIDOR="5Ojsh#rtlss3"
CARGO="Servidor Teste - Triagem"
SETOR="TI Teste"
FUNCAO="Triagem"

# Inserir novo usuário
insert_user_query="INSERT INTO USUARIO (MATRICULA_USUARIO, NOME_USUARIO, SENHA_USUARIO) VALUES ('$MATRICULA', '$NOME', '$SENHA');"

# Executa a inserção do usuário e captura o ID gerado
user_id=$(mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -se "$insert_user_query; SELECT LAST_INSERT_ID();")

if [ -z "$user_id" ]; then
    echo "Erro ao inserir o usuário."
    exit 1
fi

echo "Usuário inserido com sucesso. ID do usuário: $user_id"

# Inserir novo Servidor
insert_servidor_query="INSERT INTO SERVIDOR (CARGO_SERVIDOR, FUNCAO_SERVIDOR, MATRICULA_SERVIDOR, NOME_SERVIDOR, SENHA_SERVIDOR, SETOR_SERVIDOR) VALUES ('$CARGO', '$FUNCAO', '$MATRICULA_SERVIDOR', '$NOME_SERVIDOR', '$SENHA_SERVIDOR', '$SETOR');"
# Executa a inserção do servidor e captura o ID gerado
servidor_id=$(mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -se "$insert_servidor_query; SELECT LAST_INSERT_ID();")

if [ -z "$servidor_id" ]; then
    echo "Erro ao inserir o servidor."
    exit 1
fi

ASSEDIO="Gostaria de registrar uma denúncia de assédio moral que sofri durante o período de estágio na universidade. O supervisor do laboratório constantemente me expunha a situações humilhantes e desnecessárias diante dos meus colegas. Além de questionar publicamente minha competência em tarefas simples, ele também fazia comentários depreciativos sobre minha aparência e minhas habilidades. Essas situações ocorreram repetidamente, causando grande desconforto e afetando meu desempenho. Espero que medidas sejam tomadas para que outros alunos não passem pela mesma situação e que a instituição promova um ambiente de trabalho mais respeitoso e acolhedor"
AGRADECIMENTO="Venho por meio deste canal expressar meu sincero agradecimento ao setor de atendimento estudantil e aos professores do curso de Engenharia. Durante o semestre passado, passei por um período difícil devido a questões pessoais, e a equipe foi extremamente compreensiva. Eles me orientaram quanto às possibilidades de flexibilização das atividades acadêmicas e me ofereceram apoio psicológico. Sem essa ajuda, provavelmente teria desistido de continuar meus estudos. Gostaria de parabenizar a universidade por incentivar essa cultura de empatia e comprometimento com o bem-estar dos alunos."
ALIMENTACAO="Gostaria de relatar problemas relacionados à qualidade e à variedade das refeições no restaurante universitário. Frequentemente, os alimentos não estão frescos, e o cardápio tem se tornado repetitivo, com poucas opções saudáveis. Além disso, já presenciei casos em que a temperatura dos alimentos estava inadequada, o que pode representar um risco para a saúde dos estudantes. Sabemos da importância de uma boa alimentação para o desempenho acadêmico e esperamos que a administração possa avaliar e melhorar os serviços prestados, promovendo um ambiente mais saudável para todos"
CONDUTA="Gostaria de registrar uma queixa formal sobre a conduta inadequada de um professor do curso de Direito. Durante as aulas, o docente faz comentários ofensivos sobre alunos que discordam de sua opinião e se recusa a discutir pontos de vista diferentes. Em diversas ocasiões, ele ameaçou reprovar alunos com base em preferências pessoais e não no desempenho acadêmico. Esse comportamento tem gerado um clima de medo e insegurança na turma, dificultando o aprendizado. Acreditamos que a universidade deve intervir para garantir um ambiente de ensino mais justo e profissional."
DISCRIMINACAO="Venho por meio desta relatar um episódio de discriminação que presenciei na biblioteca da universidade. Um colega estrangeiro foi tratado de maneira rude por um funcionário, que se recusou a auxiliá-lo apenas porque ele não falava português fluentemente. O estudante ficou visivelmente desconfortável e sem orientação. Acreditamos que atitudes como essa não refletem os valores da universidade e pedimos que a instituição invista em treinamento para seus funcionários, a fim de promover um ambiente mais inclusivo e respeitoso para todos, independentemente de sua nacionalidade."
ALIMENTACAO="Gostaria de registrar minha insatisfação em relação aos serviços de alimentação fornecidos pela cantina do campus. Nos últimos meses, tenho notado uma série de problemas recorrentes que afetam diretamente a qualidade da experiência dos estudantes e servidores que dependem deste espaço para fazer suas refeições diárias. Em primeiro lugar, há uma grande falta de variedade no cardápio oferecido. As opções de refeições são extremamente limitadas e, frequentemente, repetitivas, o que acaba obrigando muitos a buscar alternativas fora da universidade. Isso é especialmente prejudicial para alunos de baixa renda que não possuem condições de arcar com custos adicionais de alimentação fora do campus. Além disso, não há qualquer tipo de informação clara sobre os ingredientes utilizados, o que é um risco para pessoas com restrições alimentares específicas, como alérgicos, veganos e vegetarianos.

Outro ponto preocupante é a higiene do local. Várias vezes presenciei mesas sujas e bandejas acumuladas nos balcões de devolução, o que dá uma péssima impressão e pode representar risco à saúde dos usuários. Também foi constatada, em mais de uma ocasião, a presença de insetos na área de atendimento e nos arredores da cozinha, o que é totalmente inaceitável. Ao conversar com outros estudantes, fiquei sabendo de relatos de colegas que passaram mal após consumir alguns dos alimentos vendidos aqui, o que levanta dúvidas sobre a qualidade e o armazenamento correto dos produtos.

O atendimento é outro aspecto que precisa de melhoria. Os funcionários da cantina, embora pareçam estar sempre sobrecarregados, muitas vezes demonstram falta de treinamento e empatia no trato com os estudantes. Não é incomum ouvir respostas ríspidas para perguntas simples sobre o cardápio ou os preços. Além disso, o tempo de espera nas filas é excessivo, mesmo em horários em que não há tanta demanda. A gestão do fluxo de clientes precisa ser repensada, talvez com mais pontos de pagamento ou sistemas de autoatendimento.

Outro problema crítico é o preço das refeições. Embora a universidade afirme que os valores são acessíveis, muitos estudantes discordam. Uma refeição completa, com prato principal, bebida e sobremesa, chega a custar mais do que o esperado para um ambiente universitário, onde o público-alvo inclui uma parcela significativa de alunos que dependem de bolsas e outros auxílios financeiros. Seria essencial uma revisão nos preços praticados, além da implementação de políticas que garantam isenção ou descontos para os estudantes em situação de vulnerabilidade.

Além disso, a cantina não oferece opções suficientes para pessoas com necessidades dietéticas específicas. Por exemplo, não há refeições sem glúten ou opções mais saudáveis e balanceadas para quem precisa seguir uma dieta específica por questões de saúde. A inclusão de opções variadas beneficiaria não apenas os estudantes que têm restrições alimentares, mas também todos aqueles que desejam manter uma alimentação equilibrada.

Por fim, gostaria de sugerir que a administração da cantina realizasse consultas e pesquisas regulares com os alunos para entender melhor suas necessidades e expectativas. Acredito que ouvir o público-alvo seria fundamental para promover mudanças positivas e melhorar a satisfação de todos que utilizam os serviços de alimentação. Também seria interessante adotar medidas mais sustentáveis, como a redução de embalagens descartáveis e a promoção de campanhas para a conscientização do desperdício de alimentos.

Encaminho esta denúncia com a esperança de que as questões aqui relatadas sejam levadas em consideração e tratadas com a seriedade necessária. A alimentação é uma parte essencial da rotina universitária e deve ser vista como uma prioridade pela gestão. A melhoria nos serviços da cantina trará benefícios não apenas para os estudantes, mas para toda a comunidade acadêmica, promovendo um ambiente mais acolhedor e saudável para todos. Agradeço a atenção e aguardo um retorno sobre as medidas que serão tomadas para solucionar os problemas mencionados. Vou colocar qui mais algumas palavras para poder completar cinco mil, meu deus aina faltam mil caracteres vou repetir a mesma palavra algumas vezes de modo que encha bastante liguiça, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste,teste, teste,teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste, teste. Agora."


# Tipos de denúncia
declare -a tipos=("Elogio" "Reclamacao" "Solicitacao" "Sugestao" "Denuncia")

# Tipos de estado
declare -a estados=("N" "P" "E" "R" "A" "I")

# Locais
declare -a locais=("Capus Recife" "Campus Jaboatão" "Campus Caruaru" "Campus Olinda")

# Setor
declare -a setores=("Coordenação" "Reitoria" "TI")

# Descrições
declare -a descricoes=("$ASSEDIO" "$AGRADECIMENTO" "$ALIMENTACAO" "$CONDUTA" "$DISCRIMINACAO" "$ALIMENTACAO")

# Assunto
declare -a assunto=("Assedio" "Agradecimento" "Alimentação" "Bolsas" "Conduta Docente" "Descriminação" "Outro")

# Inserir 100 denúncias
for i in {1..100}
do
    tipo_denuncia=${tipos[$((i % 5))]}
    assunto_denuncia=${assunto[$((i % 7))]}
    descricao_denuncia=${descricoes[$((i % 6))]}
    data_criacao=$(date +'%Y-%m-%d %H:%M:%S')
    data_denuncia=$(date -d "$i days ago" +'%Y-%m-%d')
    estado_denuncia=${estados[$((i % 6))]}
    local_denuncia=${locais[$((i % 4))]}
    setor_denuncia=${setores[$((i % 3))]}

    insert_denuncia_query="INSERT INTO DENUNCIA (ASSUNTO_DENUNCIA, DATA_CRIACAO_DENUNCIA, DATA_DENUNCIA, DESCRICAO_DENUNCIA, ESTADO_DENUNCIA, LOCAL_DENUNCIA, TIPO_DENUNCIA, SERVIDOR_ID, USUARIO_ID) 
                           VALUES ('$assunto_denuncia', '$data_criacao', '$data_denuncia', '$descricao_denuncia', '$estado_denuncia', '$local_denuncia', '$tipo_denuncia', $servidor_id, $user_id);"

    # Executa a inserção da denúncia
    mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -e "$insert_denuncia_query"
done
echo "100 denúncias inseridas com sucesso."

########################################## INFO EXTRAS ############################################

INFO1="A presente ocorrência relata uma situação que envolve procedimentos internos que precisam ser avaliados. O objetivo principal é garantir a conformidade com as políticas institucionais e evitar possíveis falhas no processo. A documentação anexa apresenta detalhes completos do ocorrido, incluindo todas as etapas envolvidas no processo de análise e resolução. A colaboração de todos os envolvidos será essencial para uma solução rápida e eficiente."
INFO2="Este relato apresenta um evento ocorrido fora das dependências da instituição, mas que teve impacto direto em atividades relacionadas. A ocorrência foi registrada no dia XX/XX/XXXX às XX
, e os envolvidos já foram informados sobre a necessidade de providências imediatas. Aguardamos o retorno oficial para dar andamento aos procedimentos necessários e documentar as ações subsequentes."
INFO3="Durante o último período, notamos algumas falhas nos serviços prestados, particularmente no atendimento ao público. Foram identificados casos de atrasos nas respostas e orientações imprecisas. Sugerimos que uma análise seja realizada para verificar possíveis gargalos operacionais e otimizar a comunicação entre setores. Uma pesquisa de satisfação seria uma ferramenta útil para medir a percepção dos usuários e identificar pontos de melhoria. Agradecemos desde já a atenção dedicada ao caso e estamos à disposição para colaborar na implementação das ações necessárias."
INFO4="Por meio deste, solicitamos a disponibilização de recursos adicionais para a execução das atividades programadas para o próximo trimestre. A necessidade identificada inclui materiais específicos e a contratação temporária de serviços especializados, que são essenciais para garantir a entrega dos projetos dentro dos prazos estabelecidos. A falta desses recursos pode comprometer a eficiência e a qualidade das entregas. Anexamos um relatório detalhado com o levantamento das necessidades e as justificativas correspondentes. Contamos com a análise e aprovação das solicitações o mais breve possível para evitar atrasos no cronograma."
INFO5="No dia XX/XX/XXXX, por volta das XX
, foi registrada uma ocorrência significativa que precisa ser tratada com urgência. Esta ocorrência envolveu várias áreas da organização, resultando em impactos que ainda estão sendo avaliados. Inicialmente, a situação parecia estar sob controle, mas, com o decorrer do tempo, notamos que algumas questões ficaram pendentes, gerando desconforto entre os envolvidos. Diante disso, é fundamental que todas as informações sejam cuidadosamente registradas para que possamos tomar as medidas adequadas.

Durante a primeira etapa da investigação, conversamos com as partes envolvidas e reunimos depoimentos que fornecem uma visão mais clara do que ocorreu. A análise preliminar sugere que alguns procedimentos padrão não foram seguidos conforme o esperado. Além disso, houve falhas na comunicação interna que contribuíram para a escalada do problema. Identificamos também a necessidade de reforçar a capacitação da equipe em determinados processos para evitar que situações semelhantes se repitam no futuro.

Como parte do plano de ação, sugerimos as seguintes medidas:

Revisão de Procedimentos Internos: Avaliar as falhas no fluxo atual e realizar ajustes conforme necessário.
Treinamento da Equipe: Realizar workshops e cursos para garantir que todos conheçam e sigam os processos definidos.
Melhoria na Comunicação: Implementar ferramentas que facilitem a troca de informações entre setores.
Monitoramento Contínuo: Estabelecer métricas para acompanhar a evolução e garantir a melhoria contínua.
A ocorrência será acompanhada de perto nas próximas semanas para garantir que todas as ações planejadas sejam implementadas corretamente. Além disso, faremos reuniões periódicas com os responsáveis por cada área para avaliar o progresso e discutir ajustes que possam ser necessários. O envolvimento de todos é fundamental para a solução efetiva e para o fortalecimento da confiança interna.

Estamos à disposição para fornecer mais detalhes, caso necessário, e aguardamos orientações adicionais quanto aos próximos passos. Uma comunicação oficial será emitida assim que todas as informações forem analisadas e consolidadas. Contamos com a colaboração de todos para resolver esta situação da melhor maneira possível e para garantir que erros semelhantes não ocorram novamente no futuro."

declare -a infos=("$INFO1" "$INFO2" "$INFO3" "$INFO4" "$INFO5")
declare -a tipos_criador=(0 1)

query_get_ids="SELECT ID FROM DENUNCIA LIMIT 30;"

mapfile -t denuncia_ids < <(mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -N -e "$query_get_ids")

if [ ${#denuncia_ids[@]} -lt 30 ]; then
	echo "Erro: Menos de 30 IDs encontrados na tabela DENUNCIA."
	exit 1
fi

query_get_ids_criadores_info_adicional_servidor="SELECT d.SERVIDOR_ID FROM DENUNCIA d JOIN SERVIDOR s ON d.SERVIDOR_ID = s.ID_SERVIDOR WHERE d.SERVIDOR_ID IS NOT NULL AND s.FUNCAO_SERVIDOR = 'Triagem' LIMIT 2;"
query_get_ids_criadores_info_adicional_usuario="SELECT USUARIO_ID FROM DENUNCIA WHERE USUARIO_ID IS NOT NULL LIMIT 30;"

mapfile -t ciradores_info_extra_servidor_ids < <(mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -N -e "$query_get_ids_criadores_info_adicional_servidor")
mapfile -t criadores_info_extra_usuario_ids < <(mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -N -e "$query_get_ids_criadores_info_adicional_usuario")

if [ ${#ciradores_info_extra_servidor_ids[@]} -eq 0 ] || [ ${#criadores_info_extra_usuario_ids[@]} -eq 0 ]; then
	echo "Erro: Não foram encontrados IDs suficientes de servidores ou usuários."
	exit 1
fi

# Inserir 100 informacoes dicionais as denúncias
for i in {1..100}
do
    data_modificacao=$(date +'%Y-%m-%d %H:%M:%S')
    id_denuncia=${denuncia_ids[$((i % ${#denuncia_ids[@]}))]}
    info_adicional=${infos[$((i % ${#infos[@]}))]}

    tipos_criador_denuncia=${tipos_criador[$((i % 2))]}
    if [ "$tipos_criador_denuncia" -eq 0 ]; then
	    id_criador_denuncia=${ciradores_info_extra_servidor_ids[$((i % ${#ciradores_info_extra_servidor_ids[@]}))]}
	    nome_criador_info_adicional=$NOME_SERVIDOR
    else
	    id_criador_denuncia=${criadores_info_extra_usuario_ids[$((i % ${#criadores_info_extra_usuario_ids[@]}))]}
	    nome_criador_info_adicional=$NOME
    fi

    insert_info_query="INSERT INTO INFORMACAOADICIONAL (DATA_MODIFICACAO_INFORMACAO_ADICIONAL, INFORMACAO_ADICIONAL, DENUNCIA_ID, TIPO_CRIADOR_INFORMACAO_ADICIONAL, ID_CRIADOR_INFORMACAO_ADICIONAL, NOME_CRIADOR_INFORMACAO_ADICIONAL) VALUES ('$data_modificacao', '$info_adicional', '$id_denuncia', '$tipos_criador_denuncia', '$id_criador_denuncia', '$nome_criador_info_adicional');"

    # Executa a inserção das infos
    mysql -u$DB_USER -p$DB_PASS -D$DB_NAME -e "$insert_info_query"
done

echo "100 informações extras inseridas com sucesso."

echo "Usuario: Matricula:$MATRICULA Senha:$SENHA"
echo "Servidor: Matricula:$MATRICULA_SERVIDOR Senha:$SENHA_SERVIDOR"
