package MyModule

import chisel3._
import chisel3.util._
// import chisel3.stage._
import chisel3.experimental._
import _root_.circt.stage.ChiselStage

class MyBlackBox extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val clock = Input(Clock())
    val reset = Input(Bool())
    val input1 = Input(UInt(32.W))
    val output1 = Output(UInt(32.W))
  })
}


class MyModule extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(32.W))
    val out = Output(UInt(32.W))
  })

  // 实例化 BlackBox
  val blackBox = Module(new MyBlackBox)

  // 连接信号
  blackBox.io.clock := clock
  blackBox.io.reset := reset
  blackBox.io.input1 := io.in
  io.out := blackBox.io.output1
}

object MyModule extends App {
  // val firtoolOptions = Array("--disable-annotation-unknown")
  val firtoolOptions = Array("--lowering-options=" + List(
        // make yosys happy
        // see https://github.com/llvm/circt/blob/main/docs/VerilogGeneration.md
        "disallowLocalVariables",
        "disallowPackedArrays",
        "locationInfoStyle=wrapInAtSquareBracket"
    ).reduce(_ + "," + _),
    "--disable-annotation-unknown")
  ChiselStage.emitSystemVerilogFile(new MyModule, args, firtoolOptions)
}

