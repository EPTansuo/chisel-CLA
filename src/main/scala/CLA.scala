package CLA

import chisel3._
import chisel3.util._
import chisel3.experimental._
import _root_.circt.stage.ChiselStage


class CLA(w: Int) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(w.W))
    val b = Input(UInt(w.W))
    val s = Output(UInt(w.W))
  })

  val g = Wire(Vec(w, Bool()))
  val p = Wire(Vec(w, Bool()))
  dontTouch(g)
  dontTouch(p)
  for( i <- 0 until w) {
    g(i) := io.a(i) & io.b(i)  // generate
    p(i) := io.a(i) | io.b(i)  // propagate
  }

  // carry 
  val c = Wire(Vec(w, Bool()))
  dontTouch(c)
  for (i <- 0 until w) {
    if(i == 0) {
      c(i) := g(i) | (p(i) & 0.U) // No carry in, set to 0
    } else {
      c(i) := g(i) | (p(i) & c(i-1))
    } 
  }
  dontTouch(c)
  // sum 
  val sum = Wire(Vec(w, Bool()))
  for (i <- 0 until w) {
    if(i == 0){
      sum(i) := io.a(i) ^ io.b(i) ^ 0.U
    } else{
     sum(i) := io.a(i) ^ io.b(i) ^ c(i-1)
    }
  }
  io.s := sum.asUInt
}

object CLA extends App {
  // val firtoolOptions = Array("--disable-annotation-unknown")
  val firtoolOptions = Array("--lowering-options=" + List(
        // make yosys happy
        // see https://github.com/llvm/circt/blob/main/docs/VerilogGeneration.md
        "disallowLocalVariables",
        "disallowPackedArrays",
        "locationInfoStyle=wrapInAtSquareBracket"
    ).reduce(_ + "," + _),
    "--disable-annotation-unknown")
  ChiselStage.emitSystemVerilogFile(new CLA(16), args, firtoolOptions)
}

