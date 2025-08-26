# Estágio 1: Build da Aplicação com Maven
# Usamos uma imagem oficial do Maven com JDK 21 para compilar o projeto.
# O alias 'builder' nos permite referenciar este estágio posteriormente.
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo pom.xml para o contêiner para baixar as dependências
COPY pom.xml .

# Baixa todas as dependências do projeto. Isso otimiza o cache do Docker.
RUN mvn dependency:go-offline

# Copia todo o código-fonte do projeto para o diretório de trabalho
COPY src ./src

# Compila a aplicação e empacota em um arquivo .jar
# O comando -DskipTests pula a execução dos testes para agilizar o build.
RUN mvn package -DskipTests

# Estágio 2: Execução da Aplicação
# Usamos uma imagem leve, contendo apenas o Java Runtime Environment (JRE),
# para tornar a imagem final menor e mais segura.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho dentro do contêiner final
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build para o contêiner final
# A localização do .jar pode variar, ajuste o nome se necessário.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta em que a aplicação Spring Boot roda
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for executado
ENTRYPOINT ["java", "-jar", "app.jar"]
