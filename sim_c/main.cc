#include <stdint.h>
#include <stdio.h>
#include "VMyModule.h"
#include "verilated.h"

extern "C" void call_c_function(uint32_t input1, uint32_t* output1) {
    uint32_t in_val = input1;
    uint32_t out_val = in_val + 1; // 示例处理
    *output1 = out_val;
    printf("hello, this is a test func.\n");
}

static VMyModule* top = NULL;

void step() {
    top->clock = 0;
    top->eval();
    top->clock = 1;
    top->eval();
}

void reset(int n) {
    top->reset = 1;
    while (n--) {
        step();
    }
    top->reset = 0;
}

int main(int argc, char* argv[]) {
    Verilated::commandArgs(argc, argv);
    top = new VMyModule;

    reset(1);  // 初始化

    // 设置输入信号
    for (int i = 0; i < 10; ++i) {
        top->io_in = uint32_t(i); // 更新输入信号
        step(); // 进行一步仿真

        // 打印输出结果
        printf("Input: %u, Output: %d\n", top->io_in, top->io_out); // 这里其实在打印地址
    }

    delete top; // 释放资源
    return 0;
}


