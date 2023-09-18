import chisel3._
import chisel3.util._

class FullAdder extends Module{
    val io = IO(new Bundle{
        val a      = Input(Bool())
        val b      = Input(Bool())
        val cin    = Input(Bool())
        val sum    = Output(Bool())
        val cout   = Output(Bool()) 
    })
    io.sum  := io.a ^ io.b ^ io.cin
    io.cout := (io.a & io.b) | (io.cin & (io.a ^ io.b))
}


class AdderNBits(n: Int) extends Module{
    val io = IO(new Bundle{
        val a   = Input(UInt(n.W))
        val b   = Input(UInt(n.W))
        val sum = Output(UInt((n+1).W))
    })

    val adders = Array.fill(n)(Module(new FullAdder))
    val w_sum  = Wire(Vec(n+1,UInt(1.W)))
    adders(0).io.cin := 0.U
    w_sum(n)         := adders(n-1).io.cout
    for(i <- 0 until n){
        adders(i).io.a     := io.a(i)
        adders(i).io.b     := io.b(i)
        w_sum(i)           := adders(i).io.sum
    }
    for(i <- 0 until n-1) {
        adders(i+1).io.cin := adders(i).io.cout
    }
    io.sum := RegNext(w_sum.asUInt)
}


object Main extends App{
    (new chisel3.stage.ChiselStage).emitVerilog(new AdderNBits(4),  Array("--target-dir", "build/artifacts/netlist/"))
}