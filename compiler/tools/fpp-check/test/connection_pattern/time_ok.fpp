module Fw {
  port Time
}
module M {
  passive component Time {
    sync input port timeGetIn: Fw.Time
    #sync input port timeGetIn1: Fw.Time
  }
  passive component C {
    time get port timeGetOut
  }
  instance timeSource: Time base id 0x100
  instance c: C base id 0x100
  topology T {
    instance timeSource
    time connections instance timeSource
  }
}
