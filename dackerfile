# Etapa de build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Clona a branch especificada via ARG
ARG BRANCH=stage
RUN apt-get update && apt-get install -y git
RUN git clone -b ${BRANCH} https://github.com/MathLaurentino/mooc-backend.git .

# Compila o projeto (gera o .jar)
RUN ./mvnw clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia o .jar gerado
COPY --from=build /app/target/*.jar app.jar

# Define variáveis e expõe porta
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
