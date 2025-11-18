FROM eclipse-temurin:17-jdk
WORKDIR /app

# Mavenを使ってビルドするために、まずMavenをインストール
RUN apt-get update && apt-get install -y maven

# プロジェクトファイルをすべてコピー
COPY . .

# MavenでJARファイルをビルド
RUN mvn clean package -DskipTests

# ビルドされたJARファイルを起動
ENTRYPOINT ["java", "-jar", "target/kadai_002-0.0.1-SNAPSHOT.jar"]
