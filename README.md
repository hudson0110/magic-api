#Correções da Fase 1

Criar DTOs para requests e responses.

Criar estrutura padronizada de ErrorResponse.

Separar validações de payload das validações de banco de dados.

Adicionar logs utilizando Logger nos principais fluxos da aplicação.

Adicionar novos cenários de testes.

#Evolução da Estrutura de Stack

Alterar o atributo stack para receber uma lista de objetos.

Criar entidade Stack.

Criar nova tabela stacks no banco de dados.

Configurar relacionamento entre Usuário e Stack.

#Validações de Stack

Implementar validação customizada para o atributo stack.

Não permitir elementos nulos ou vazios.

Validar campo name obrigatório com tamanho entre 1 e 32 caracteres.

Validar campo level obrigatório com valores entre 1 e 10.

#Novos Endpoints
Implementar endpoint GET /api/users/{userId}/stacks.

#Paginação e Ordenação
Implementar paginação na listagem de usuários.

Implementar ordenação na listagem de usuários.

#Padronização da API
Ajustar payloads e responses para o padrão snake_case.

Garantir compatibilidade com os padrões definidos no API Playbook.

#Padronização de Erros
Implementar retorno de erros padronizado contendo error e description.

Não retornar informações internas do sistema em mensagens de erro.

#Testes
Validar objetos completos nos testes.

Validar retorno de erros no formato padronizado.

Utilizar @ParameterizedTest.

Implementar testes na camada Service.

Cobrir os novos cenários relacionados à Stack.

Estudo Técnico
Avaliar diferentes versões de UUID e seus impactos na aplicação.
