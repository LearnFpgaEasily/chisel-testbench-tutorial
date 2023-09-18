import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec


class AdderNBitsSpec extends AnyFlatSpec with ChiselScalatestTester {
  for(n <- 1 to 8){
    "AdderNBits" should s"correctly add two $n-bit numbers" in {
      test(new AdderNBits(n)).withAnnotations(Seq(WriteVcdAnnotation)) {dut =>
        for (a <- 0 until Math.pow(2,n).toInt) {
          for (b <- 0 until Math.pow(2,n).toInt) {
            dut.io.a.poke(a)
            dut.io.b.poke(b)
            println("==========")
            println("a = " + a.toString )
            println("b = " + b.toString )
            println("==========")
            dut.clock.step()
            val result = dut.io.sum.peekInt()
            println("==========")
            println("result = " + result.toString)
            println("==========")
            assert(result == a+b)
          }
        }
      }
    }
  }
}

