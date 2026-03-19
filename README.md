###  📝 CartCheck

🛒 Brevus Commerce API

API REST e Interface Administrativa desenvolvidas em Java 21 e Spring Boot para gestão completa de vendas, produtos e pagamentos, com integração real ao gateway Efí Bank.

O sistema simula um ambiente de vendas profissional, unindo uma arquitetura robusta de backend com uma interface dinâmica renderizada via Thymeleaf e estilizada com Tailwind CSS.

- ADMIN: Acesso total
- CLIENT: Acesso limitado, apenas para compra de produtos
- SELLER: Acesso limitado, para gerenciar


### ⚙️ Funcionalidades

👤 Usuários e Segurança

- [x] Cadastro e autenticação de usuários (Spring Security + JWT).
- [x] Controle de acesso baseado em Roles: ADMIN (Total), SELLER (Gestão) e CLIENT (Compras).
- [x] Proteção de rotas e persistência de sessão segura.

🎨 Interface & Visualização (Front-end)

- [x] Thymeleaf Templates: Renderização de páginas dinâmicas no servidor.
- [x] Tailwind CSS: Design moderno, responsivo e com suporte a Dark/Light Mode.
- [x] Dashboard Administrativo: Visualização de métricas e status de vendas em tempo real.
- [x] Fluxo de Checkout: Interface visual para seleção de produtos e geração de cobranças.

🧑‍💼 Clientes

- [x] Cadastro de clientes
- [x] Histórico de compras por cliente
- [x] Relatório de pedidos
- [x] Comprar produtos 

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
- [x] Geração de cobrança CARTÃO
- [x] Armazenamento de charge_id, status e dados da cobrança
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

### ⚙️ Tecnologias Utilizadas

- Tecnologia	              Finalidade
- Java 21	          ->      Linguagem
- Spring Boot	      ->      Framework principal
- Spring Security   ->      JWT	Autenticação e autorização
- Spring Data JPA	  ->      Persistência
- PostgreSQL	      ->      Banco de dados
- Flyway	          ->      Versionamento do banco
- MapStruct	        ->      Conversão Entity ↔ DTO
- WebClient	        ->      Integração com API Efí
- Swagger/OpenAPI	  ->      Documentação
- JUnit 5 + Mockito	->      Testes
- Docker	          ->      Ambiente isolado
- Maven	            ->      Build
- GitHub Actions	  ->      CI/CD

---
### 🗃️ Arquitetura do Banco de Dados
[<img alt="Modelo-ER" src="https://lh3.googleusercontent.com/pw/AP1GczNWMlvCK6Pr0hDLelbGxI2Ejbu4vvAHzTo52XK7f7TPUjwz8NiAtmx_xjkVji7iOUPuWOo3K50GSghWDt4Mx3o5wi7wkPP2uZC4vTbGxExY-YKOCJVZEGZ3aHTBeRsY1fSiqrc3TuDMTwzCbam3flmI=w649-h960-s-no?authuser=1"/>](SpringBoot)

---
### ✅ Testes Automatizados CI/CD GitHub Actions!

#### 📸 Resultado dos testes executados com sucesso:

[<img alt="CI/CD" src="https://lh3.googleusercontent.com/pw/AP1GczNsg7RMArcEFJfE3GL4IBF4pJ1AX7D-c4lwuE8UAJwul7K_83TjNoj6bTogjnmskAdckfgStYyBegM8Go5Jem3KrxpAUsw5zgxyr-drOh9A5vth3cZJLVSBeqN8mGE24GeXMZmXhmEfIE2BN7Zn9qdO=w1440-h725-s-no?authuser=1"/>](SpringBoot)

---

🚀 Como Executar o Projeto

1 - Clone o repositório:

- git clone https://github.com/GabrielCabral-DS/brevus-commerce-api.git

2 - Configure as Credenciais:

- Renomeie os arquivos,adicione as variáveis de ambiente e insira sua chave da Efí Bank e o caminho do certificado .p12.

3 - Suba o Ambiente com Docker:

- Escreva os comandos no terminal:
- 1 - ls
- 2- chmod +x start.sh
- 3- ./start.sh

4 - Acesse a aplicação:

- Dashboard: http://localhost:8080/login
- Swagger: http://localhost:8080/swagger-ui.html
---

🔗 Conecte-se comigo: :large_blue_circle: [Linkedin](https://www.linkedin.com/in/gabriel-cabral-878482262/)
