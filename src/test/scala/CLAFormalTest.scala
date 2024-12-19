package CLA

import chisel3._
import chisel3.util._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec
import scala.math._

class CLAFormalTestBench(bitwidth: Int) extends Module{
    val io = IO(new Bundle{
        val a = Input(UInt(bitwidth.W))
        val b = Input(UInt(bitwidth.W))
        val equal = Output(Bool())
    })
    val dut = Module(new CLA(bitwidth))
    dut.io.a := io.a
    dut.io.b := io.b
    io.equal := dut.io.s === io.a + io.b
    assert(io.equal === true.B, "ACL: a + b != s")
}


class CLAFormalTest extends AnyFlatSpec with ChiselScalatestTester with Formal{

    "CLA" should "a + b == s" in {
        verify(new CLAFormalTestBench(16), Seq(BoundedCheck(1)))
    }

}