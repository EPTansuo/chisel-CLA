TOP = CLA
MAIN = CLA.CLA
BUILD_DIR = ./build
TOPNAME = CLA
TOP_V = $(BUILD_DIR)/verilog/$(TOPNAME).v

SCALA_FILE = $(shell find ./src -name '*.scala')


verilog: $(SCALA_FILE)
	@mkdir -p $(BUILD_DIR)/verilog
	./mill -i $(TOP).runMain $(MAIN) -td $(BUILD_DIR)/verilog

test: $(SCALA_FILE)
	./mill -i $(TOP).test.testOnly $(TOP).CLATest
formal: $(SCALA_FILE)
	./mill -i $(TOP).test.testOnly $(TOP).CLAFormalTest

clean:
	-rm -rf $(BUILD_DIR) logs

clean_mill:
	-rm -rf out target  test_run_dir project

clean_all: clean clean_mill

.PHONY: clean clean_all clean_mill verilog
