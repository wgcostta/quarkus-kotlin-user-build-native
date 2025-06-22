# Makefile para projeto Quarkus Kotlin MongoDB

.PHONY: help build run dev test clean docker-build docker-run docker-stop native install

# Variáveis
PROJECT_NAME = quarkus-kotlin-mongo-app
DOCKER_IMAGE = $(PROJECT_NAME):latest
COMPOSE_FILE = docker-compose.yml

# Comando padrão
help: ## Mostra esta ajuda
	@echo "Comandos disponíveis:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}'

install: ## Instala dependências do projeto
	@echo "📦 Instalando dependências..."
	./gradlew dependencies
	@echo "✅ Dependências instaladas!"

build: ## Builda o projeto
	@echo "🔨 Buildando o projeto..."
	./gradlew build
	@echo "✅ Build concluído!"

build-native: ## Builda versão nativa com GraalVM
	@echo "🚀 Buildando versão nativa..."
	./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
	@echo "✅ Build nativo concluído!"

run: ## Executa a aplicação localmente
	@echo "🏃 Executando aplicação..."
	./gradlew quarkusRun

dev: ## Executa em modo desenvolvimento com hot reload
	@echo "🔥 Iniciando modo desenvolvimento..."
	./gradlew quarkusDev

test: ## Executa os testes
	@echo "🧪 Executando testes..."
	./gradlew test
	@echo "✅ Testes concluídos!"

test-coverage: ## Executa testes com cobertura
	@echo "📊 Executando testes com cobertura..."
	./gradlew test jacocoTestReport
	@echo "✅ Relatório de cobertura gerado!"

clean: ## Limpa arquivos de build
	@echo "🧹 Limpando arquivos de build..."
	./gradlew clean
	@echo "✅ Limpeza concluída!"

docker-build: ## Builda a imagem Docker
	@echo "🐳 Buildando imagem Docker..."
	docker build -t $(DOCKER_IMAGE) .
	@echo "✅ Imagem Docker criada: $(DOCKER_IMAGE)"

docker-run: ## Executa apenas a aplicação com Docker
	@echo "🚀 Executando aplicação com Docker..."
	docker run -p 8080:8080 --rm $(DOCKER_IMAGE)

compose-up: ## Sobe todos os serviços com Docker Compose
	@echo "🐳 Subindo serviços com Docker Compose..."
	docker-compose -f $(COMPOSE_FILE) up --build
	@echo "✅ Serviços executando!"

compose-up-detached: ## Sobe serviços em background
	@echo "🐳 Sub