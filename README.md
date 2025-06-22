# Quarkus Kotlin MongoDB API

Uma API REST moderna para gerenciamento de usuários construída com **Quarkus**, **Kotlin**, **MongoDB** e **GraalVM Native**.

## 🚀 Tecnologias

- **Quarkus 3.6.4** - Framework Java supersônico e subatômico
- **Kotlin** - Linguagem de programação moderna
- **MongoDB** - Banco de dados NoSQL
- **GraalVM Native** - Compilação nativa para startup ultrarrápido
- **Docker & Docker Compose** - Containerização e orquestração
- **Gradle** - Ferramenta de build
- **OpenAPI/Swagger** - Documentação da API

## 📋 Funcionalidades

- ✅ CRUD completo de usuários
- ✅ Validação de dados
- ✅ Busca por email e nome
- ✅ Documentação automática da API (Swagger)
- ✅ Health checks
- ✅ Logging estruturado
- ✅ Build nativo com GraalVM
- ✅ Containerização com Docker

## 🏗️ Arquitetura

```
src/main/kotlin/com/example/
├── dto/           # Data Transfer Objects
├── entity/        # Entidades do MongoDB
├── resource/      # Controllers REST
└── service/       # Lógica de negócio
```

## 🛠️ Pré-requisitos

- **Docker** e **Docker Compose** instalados
- **Java 21** (apenas para desenvolvimento local)
- **Gradle** (incluído via wrapper)

## 🚀 Como Executar

### Opção 1: Docker Compose (Recomendado)

```bash
# Clonar o repositório
git clone <seu-repositorio>
cd quarkus-kotlin-mongo-app

# Executar com Docker Compose
docker-compose up --build
```

### Opção 2: Desenvolvimento Local

```bash
# Subir apenas o MongoDB
docker-compose up mongodb

# Executar a aplicação em modo desenvolvimento
./gradlew quarkusDev
```

### Opção 3: Build Nativo Local

```bash
# Build nativo (requer GraalVM instalado)
./gradlew build -Dquarkus.package.type=native

# Executar o binário nativo
./build/quarkus-kotlin-mongo-app-1.0.0-SNAPSHOT-runner
```

## 📡 Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/users` | Listar todos os usuários |
| GET | `/api/users/{id}` | Buscar usuário por ID |
| GET | `/api/users/email/{email}` | Buscar usuário por email |
| GET | `/api/users/search?name={name}` | Pesquisar usuários por nome |
| POST | `/api/users` | Criar novo usuário |
| PUT | `/api/users/{id}` | Atualizar usuário |
| DELETE | `/api/users/{id}` | Deletar usuário |

### Exemplo de Requisição POST

```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "age": 30
}
```

### Exemplo de Resposta

```json
{
  "id": "65f8a2b1c45d123456789abc",
  "name": "João Silva",
  "email": "joao@email.com",
  "age": 30,
  "created_at": "2024-03-19T10:30:00"
}
```

## 🔍 Monitoramento e Documentação

- **Swagger UI**: http://localhost:8080/swagger-ui
- **Health Check**: http://localhost:8080/health
- **Mongo Express**: http://localhost:8081 (admin/admin123)

## 🐳 Containers

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| quarkus-app | 8080 | API REST principal |
| mongodb | 27017 | Banco de dados MongoDB |
| mongo-express | 8081 | Interface web do MongoDB |

## 🧪 Testes

```bash
# Executar testes
./gradlew test

# Executar testes com relatório
./gradlew test --info
```

## 📊 Performance

### Comparação JVM vs Native

| Métrica | JVM | Native |
|---------|-----|--------|
| Tempo de startup | ~2-3s | ~0.1s |
| Uso de memória | ~100MB | ~30MB |
| Tamanho do executável | ~200MB | ~50MB |

## 🔧 Configuração

### Variáveis de Ambiente

```bash
# MongoDB
QUARKUS_MONGODB_CONNECTION_STRING=mongodb://localhost:27017
QUARKUS_MONGODB_DATABASE=userdb

# Logging
QUARKUS_LOG_LEVEL=INFO

# HTTP
QUARKUS_HTTP_PORT=8080
```

### Profiles

```bash
# Desenvolvimento
./gradlew quarkusDev

# Produção
./gradlew build -Dquarkus.package.type=native
```

## 📝 Estrutura do Projeto

```
quarkus-kotlin-mongo-app/
├── src/
│   ├── main/
│   │   ├── kotlin/com/example/
│   │   │   ├── dto/
│   │   │   │   └── UserDTO.kt
│   │   │   ├── entity/
│   │   │   │   └── User.kt
│   │   │   ├── resource/
│   │   │   │   └── UserResource.kt
│   │   │   └── service/
│   │   │       └── UserService.kt
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── gradle/
├── build.gradle.kts
├── docker-compose.yml
├── Dockerfile
├── init-mongo.js
└── README.md
```

## 🐛 Troubleshooting

### Problema: Container não inicia

```bash
# Verificar logs
docker-compose logs quarkus-app

# Reconstruir imagens
docker-compose up --build --force-recreate
```

### Problema: MongoDB não conecta

```bash
# Verificar se o MongoDB está rodando
docker-compose ps mongodb

# Verificar logs do MongoDB
docker-compose logs mongodb
```

### Problema: Build nativo falha

```bash
# Usar build em container
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🔗 Links Úteis

- [Quarkus Documentation](https://quarkus.io/guides/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [GraalVM Documentation](https://www.graalvm.org/docs/)
- [Docker Documentation](https://docs.docker.com/)

## 🎯 Próximos Passos

- [ ] Implementar autenticação JWT
- [ ] Adicionar cache com Redis
- [ ] Implementar paginação
- [ ] Adicionar métricas com Micrometer
- [ ] Implementar testes de integração
- [ ] Adicionar CI/CD pipeline