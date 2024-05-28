# Requirements 
1. java 17

# How to run
1. Build
```zsh
./mvnw clean package
```
2. Run 
```zsh
java -jar ./target/game-1.0-SNAPSHOT.jar --config ./src/test/resources/config.json --betting-amount 100
```