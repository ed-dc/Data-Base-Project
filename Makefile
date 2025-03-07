SRC_DIR := src/enchere
BIN_DIR := bin
SRC := $(shell find $(SRC_DIR) -name "*.java")
JDBC_JAR := /opt/sqldeveloper-20.4.1/jdbc/lib/ojdbc8.jar

# Option de compilation
JAVAC := javac
JFLAGS := -d $(BIN_DIR) -cp $(JDBC_JAR):$(BIN_DIR)

# Règle par défaut
all: $(BIN_DIR)
	$(JAVAC) $(JFLAGS) $(SRC)

# Compilation de tous les fichiers
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

run: all
	@echo "Classpath utilisé : $(JDBC_JAR):$(BIN_DIR)"
	java -cp "$(JDBC_JAR):$(BIN_DIR)" enchere.Main

# Nettoyage des fichiers compilés
clean:
	@rm -rf $(BIN_DIR)

.PHONY: all clean
