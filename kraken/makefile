all:
	make dev
	make clean

dev: submodule
	find . -name "*.java" > sources.txt
	javac @sources.txt
	rm sources.txt
	rm -rf ebhack/org
	cp -rf org ebhack/org
	jar cvfe Kraken.jar Kraken *

submodule:
	git submodule init
	git submodule update

clean:
	find . -maxdepth 1 -name "*.class" -delete
	cd leviathanyaml && $(MAKE) clean
	cd ebhack && $(MAKE) clean
	rm -rf ebhack/org

clean-all:
	make clean
	rm -f Kraken.jar
