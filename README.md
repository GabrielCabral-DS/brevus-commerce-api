###  📝 CartCheck

🛒 Brevus Commerce API
API REST em Java Spring Boot para gestão de vendas, clientes, produtos e pagamentos com integração real ao gateway Efí Bank.
O sistema simula um ambiente de vendas profissional, com controle de estoque, regras de negócio, autenticação JWT e processamento de pagamentos assíncronos via webhook.

- ADMIN: Acesso total
- CLIENT: Acesso limitado, apenas para compra de produtos
- SELLER: Acesso limitado, para gerenciar

:large_blue_circle: [Linkedin](https://www.linkedin.com/in/gabriel-cabral-878482262/)

### ⚙️ Funcionalidades

👤 Usuários e Segurança

- [x] Cadastro e autenticação de usuários
- [x] Controle de acesso com Spring Security + JWT
- [x] Perfis de acesso: ADMINISTRADOR, VENDEDOR e CLIENTE

🧑‍💼 Clientes

- [x] Cadastro de clientes
- [x] Histórico de compras por cliente
- [x] Relatório de clientes que mais compraram

📦 Produtos & Estoque

- [x] Cadastro de produtos e categorias
- [x] Controle automático de estoque
- [x] Bloqueio de venda sem estoque
- [x] Devolução de estoque em cancelamento de venda

🛒 Vendas 

- [x] Criação de venda para cliente
- [x] Adição de múltiplos produtos por venda
- [x] Cálculo automático do valor total
- [x] Status da venda: PENDENTE, AGUARDANDO_PAGAMENTO, PAGO, CANCELADO
- [x] Venda não pode ser alterada após pagamento confirmado

💳 Pagamentos (Efí Bank)

- [x] Integração real com API da Efí Bank
- [x] Geração de cobrança PIX
- [x] Armazenamento de txid, status e dados da cobrança
- [x] Atualização automática de pagamento via Webhook
- [x] Estoque é baixado somente após confirmação do pagamento
- [x] Tratamento de falhas e cancelamentos

📊 Relatórios

- [x] Faturamento total
- [x] Produtos mais vendidos
- [x] Faturamento por cliente

🧩 Infra & Qualidade

- [x] Documentação com Swagger/OpenAPI
- [x] Validações de entrada
- [x] Conversão entre entidade e DTO com MapStruct
- [x] Testes unitários com JUnit 5 e Mockito
- [x] Migração de banco com Flyway
- [x] Containerização com Docker

---
### 🧱 Arquitetura do Projeto

- Arquitetura em camadas seguindo boas práticas de mercado:
- Controller → Recebe requisições HTTP
- Service → Regras de negócio (venda, estoque, pagamento)
- Integration → Comunicação com API Efí
- Webhook Controller → Processamento de eventos de pagamento
- Repository → Acesso ao banco de dados
- Entity / DTO → Modelagem dos dados
- Security → Autenticação JWT e controle por roles
- Config → Certificados, WebClient, segurança
  
---
### ⚙️ Tecnologias Utilizadas

Tecnologia	              Finalidade
Java 21	          ->      Linguagem
Spring Boot	      ->      Framework principal
Spring Security   ->      JWT	Autenticação e autorização
Spring Data JPA	  ->      Persistência
PostgreSQL	      ->      Banco de dados
Flyway	          ->      Versionamento do banco
MapStruct	        ->      Conversão Entity ↔ DTO
WebClient	        ->      Integração com API Efí
Swagger/OpenAPI	  ->      Documentação
JUnit 5 + Mockito	->      Testes
Docker	          ->      Ambiente isolado
Maven	            ->      Build
GitHub Actions	  ->      CI/CD

### 🗃️ Arquitetura do Banco de Dados
[<img alt="Modelo-ER" src="https://lh3.googleusercontent.com/pw/AP1GczOJ0Yh48RvyFyPTkmmgRv3qPRlQAL6WNPotdmBe8lxCQTjVjFhXnQ2jthnu80_pycT1rPeYt4E3MHWMCXkn5wYLrU3pGGqLYJc9GEmgB2ca5M9sp-XNXPwpKQWinqCvOJADCGdVz6NFOlqW7xBPJUYf=w945-h945-s-no-gm?authuser=0"/>](SpringBoot)

---
### ✅ Testes Automatizados CI/CD GitHub Actions!

#### 📸 Resultado dos testes executados com sucesso:

[<img alt="CI/CD" src=""/>](SpringBoot)


---
[<img alt="CI/CD" src=""/>](SpringBoot)
