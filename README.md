# Refatoração do Sistema de Olimpíadas

Este projeto passou por uma refatoração com o objetivo de aplicar os princípios SOLID, sem alterar o comportamento original do sistema.

---

## Modificações Realizadas

### 1. Criação da camada Repository
Foram criadas classes responsáveis pelo armazenamento e acesso aos dados:

- ParticipanteRepository  
- ProvaRepository  
- QuestaoRepository  
- TentativaRepository  

Essas classes substituem as listas que antes estavam na classe `App`.

---

### 2. Criação da camada Service
Foram criadas classes responsáveis pelas regras de negócio:

- ParticipanteService  
- ProvaService  
- ProvaAplicacaoService  

As validações e lógicas foram movidas da classe `App` para esses serviços.

---

### 3. Refatoração da classe App
A classe `App` foi simplificada e agora:

- Não armazena mais dados diretamente  
- Não contém regras de negócio  
- Apenas faz interação com o usuário (entrada e saída)  
- Utiliza os services e repositories  

---

## Princípios SOLID Aplicados

- **SRP (Single Responsibility)**  
  Separação entre dados (Repository), lógica (Service) e interface (App)

- **DIP (Dependency Inversion)**  
  Uso de injeção de dependência entre App, Services e Repositories

---

## Resultado

- Código mais organizado  
- Redução de acoplamento  
- Melhor separação de responsabilidades  
- Maior facilidade de manutenção  