# 🚀 Guia de Início Rápido

Este guia te ajudará a executar o projeto em menos de 5 minutos!

## ⚡ Opção 1: Execução Rápida com Docker

```bash
# 1. Clone o repositório
git clone <seu-repositorio>
cd quarkus-kotlin-mongo-app

# 2. Execute tudo com um comando
make compose-up
# ou
docker-compose up --build
```

**Pronto!** A aplicação estará rodando em:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui
- MongoDB UI: http://localhost:8081 (admin/admin123)

## 🛠️ Opção 2: Desenvolvimento Local

```bash
# 1. Subir apenas o MongoDB
make mongo-only
# ou
docker-compose -f docker-compose.dev.yml up mongodb-dev -d

# 2. Executar aplicação em modo desenvolvimento
make dev
# ou
./gradlew quarkusDev
```

## 🧪 Testando a API

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"João Silva","email":"joao@teste.com","age":30}'

# Listar usuários
curl http://localhost:8080/api/users

# Buscar por email
curl http://localhost:8080/api/users/email/joao@teste.com
```

## 🐳 Comandos Úteis com Make

```bash
make help              # Lista todos os comandos
make build             # Build do projeto
make test              # Executa testes
make docker-build      # Build da imagem Docker
make compose-up        # Sobe todos os serviços
make compose-down      # Para todos os serviços
make logs-app          # Logs da aplicação
make swagger           # Abre documentação
make health            # Verifica saúde da app
```

## 🔧 Estrutura de Pastas

```
quarkus-kotlin-mongo-app/
├── src/main/kotlin/com/example/
│   ├── dto/           # DTOs de entrada/saída
│   ├── entity/        # Entidades MongoDB
│   ├── resource/      # Controllers REST
│   └── service/       # Lógica de negócio
├── src/main/resources/
│   └── application.properties
├── docker-compose.yml
├── Dockerfile
├── Makefile
└── README.md
```

## 🚀 Build Nativo (GraalVM)

```bash
# Build nativo local (requer GraalVM)
make build-native

# Build nativo com Docker
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true

# Executar binário nativo
./build/quarkus-kotlin-mongo-app-1.0.0-SNAPSHOT-runner
```

## 📊 Monitoramento

- **Health Check**: http://localhost:8080/health
- **Métricas**: http://localhost:8080/health/metrics (se habilitado)
- **Logs**: `make logs-app` ou `docker-compose logs quarkus-app`

## 🐛 Troubleshooting

### Problema: Porta já em uso
```bash
# Verificar o que está usando a porta
lsof -i :8080
# Matar processo se necessário
kill -9 <PID>
```

### Problema: MongoDB não conecta
```bash
# Verificar se MongoDB está rodando
make status
# Reiniciar serviços
make restart
```

### Problema: Build falha
```bash
# Limpar e rebuildar
make clean build
# Verificar logs detalhados
./gradlew build --info
```

## 🎯 Próximos Passos

1. **Explore a API**: Acesse http://localhost:8080/swagger-ui
2. **Modifique o código**: Use `make dev` para hot reload
3. **Adicione features**: Crie novos endpoints seguindo o padrão
4. **Execute testes**: `make test` para garantir qualidade
5. **Deploy**: Use a imagem Docker para produção

## 📚 Links Úteis

- [Documentação Quarkus](https://quarkus.io/guides/)
- [Kotlin com Quarkus](https://quarkus.io/guides/kotlin)
- [MongoDB Panache](https://quarkus.io/guides/mongodb-panache)
- [GraalVM Native](https://quarkus.io/guides/building-native-image)

## ❓ Ajuda

Se encontrar problemas:
1. Verifique os logs: `make logs-app`
2. Consulte o README.md completo
3. Abra uma issue no repositório

**Divirta-se codificando! 🎉**