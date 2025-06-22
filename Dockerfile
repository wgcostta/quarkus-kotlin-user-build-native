####
# Dockerfile para aplicação Quarkus com GraalVM Native
#
# Build stage
####
FROM quay.io/quarkus/ubi-quarkus-graalvmce-builder-image:22.3-java17 AS build
COPY --chown=quarkus:quarkus gradlew /code/gradlew
COPY --chown=quarkus:quarkus gradle /code/gradle
COPY --chown=quarkus:quarkus build.gradle.kts /code/
COPY --chown=quarkus:quarkus settings.gradle.kts /code/
COPY --chown=quarkus:quarkus gradle.properties /code/

USER quarkus
WORKDIR /code
COPY src /code/src

# Build da aplicação nativa
RUN ./gradlew build -Dquarkus.package.type=native

####
# Runtime stage
####
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/

# Copiar o executável nativo
COPY --from=build /code/build/*-runner /work/application

# Definir permissões
RUN chmod 775 /work/application

# Expor porta
EXPOSE 8080

# Definir usuário não-root
USER 1001

# Comando de execução
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]