package CLA

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
import scala.math._


class CLATest extends AnyFlatSpec with ChiselScalatestTester {

  "CLA" should "a + b == s if Overflow)" in {
    test(new CLA(16)) { dut =>
      for( i <- 0 to pow(2,8).toInt){
        for( j <- 0 until pow(2,8).toInt){
          dut.io.a.poke(i.U)
          dut.io.b.poke(j.U)
          val expected = i+j
          dut.io.s.expect((expected).U,
          
          s"\n\tFailed at a=$i, b=$j: expected s=${expected}, got=${dut.io.s.peek().litValue}")
        }
      }
    }
  }

  it should "s == a + b - MAXOUT if overflow" in {
    test(new CLA(16)) { dut =>
      for (i <- (pow(2,15)+pow(2,14)).toInt until pow(2,16).toInt) {
        dut.io.a.poke(i.U)
        dut.io.b.poke(i.U)
        val expected = i*2-pow(2,16).toInt
        dut.io.s.expect(expected.U,
        s"\n\tFailed at a=$i, b=$i: expected s=${expected}, got=${dut.io.s.peek().litValue}")
      }
    }
  }
}