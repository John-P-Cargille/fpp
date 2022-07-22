// ======================================================================
// \title  FormatSerializableAc.cpp
// \author Generated by fpp-to-cpp
// \brief  cpp file for Format struct
// ======================================================================

#include "cstdio"
#include "cstring"

#include "FormatSerializableAc.hpp"
#include "Fw/Types/Assert.hpp"
#include "Fw/Types/StringUtils.hpp"

// ----------------------------------------------------------------------
// Constructors
// ----------------------------------------------------------------------

Format ::
  Format() :
    Serializable(),
    m1(0),
    m2(0),
    m3(0),
    m4(0),
    m5(0),
    m6(0),
    m7(0),
    m8(0),
    m9(0),
    m10(0),
    m11(0.0f),
    m12(0.0f),
    m13(0.0f),
    m14(0.0f),
    m15(0.0f),
    m16(0.0f),
    m17(0.0f)
{

}

Format ::
  Format(
      I32 m1,
      U32 m2,
      I32 m3,
      U32 m4,
      I32 m5,
      U32 m6,
      I32 m7,
      U32 m8,
      I32 m9,
      U32 m10,
      F32 m11,
      F32 m12,
      F32 m13,
      F32 m14,
      F32 m15,
      F32 m16,
      F32 m17
  ) :
    Serializable(),
    m1(m1),
    m2(m2),
    m3(m3),
    m4(m4),
    m5(m5),
    m6(m6),
    m7(m7),
    m8(m8),
    m9(m9),
    m10(m10),
    m11(m11),
    m12(m12),
    m13(m13),
    m14(m14),
    m15(m15),
    m16(m16),
    m17(m17)
{

}

Format ::
  Format(const Format& obj) :
    Serializable(),
    m1(obj.m1),
    m2(obj.m2),
    m3(obj.m3),
    m4(obj.m4),
    m5(obj.m5),
    m6(obj.m6),
    m7(obj.m7),
    m8(obj.m8),
    m9(obj.m9),
    m10(obj.m10),
    m11(obj.m11),
    m12(obj.m12),
    m13(obj.m13),
    m14(obj.m14),
    m15(obj.m15),
    m16(obj.m16),
    m17(obj.m17)
{

}

// ----------------------------------------------------------------------
// Operators
// ----------------------------------------------------------------------

Format& Format ::
  operator=(const Format& obj)
{
  if (this == &obj) {
    return *this;
  }

  set(obj.m1, obj.m2, obj.m3, obj.m4, obj.m5, obj.m6, obj.m7, obj.m8, obj.m9, obj.m10, obj.m11, obj.m12, obj.m13, obj.m14, obj.m15, obj.m16, obj.m17);
  return *this;
}

bool Format ::
  operator==(const Format& obj) const
{
  return (
    (this->m1 == obj.m1) &&
    (this->m2 == obj.m2) &&
    (this->m3 == obj.m3) &&
    (this->m4 == obj.m4) &&
    (this->m5 == obj.m5) &&
    (this->m6 == obj.m6) &&
    (this->m7 == obj.m7) &&
    (this->m8 == obj.m8) &&
    (this->m9 == obj.m9) &&
    (this->m10 == obj.m10) &&
    (this->m11 == obj.m11) &&
    (this->m12 == obj.m12) &&
    (this->m13 == obj.m13) &&
    (this->m14 == obj.m14) &&
    (this->m15 == obj.m15) &&
    (this->m16 == obj.m16) &&
    (this->m17 == obj.m17)
  );
}

bool Format ::
  operator!=(const Format& obj) const
{
  return !(*this == obj);
}

#ifdef BUILD_UT

std::ostream& operator<<(std::ostream& os, const Format& obj) {
  Fw::String s;
  obj.toString(s);
  os << s.toChar();
  return os;
}

#endif

// ----------------------------------------------------------------------
// Member functions
// ----------------------------------------------------------------------

Fw::SerializeStatus Format ::
  serialize(Fw::SerializeBufferBase& buffer) const
{
  Fw::SerializeStatus status;

  status = buffer.serialize(this->m1);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m2);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m3);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m4);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m5);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m6);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m7);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m8);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m9);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m10);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m11);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m12);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m13);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m14);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m15);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m16);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.serialize(this->m17);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }

  return status;
}

Fw::SerializeStatus Format ::
  deserialize(Fw::SerializeBufferBase& buffer)
{
  Fw::SerializeStatus status;

  status = buffer.deserialize(this->m1);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m2);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m3);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m4);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m5);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m6);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m7);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m8);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m9);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m10);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m11);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m12);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m13);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m14);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m15);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m16);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }
  status = buffer.deserialize(this->m17);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }

  return status;
}

#if FW_SERIALIZABLE_TO_STRING || BUILD_UT

void Format ::
  toString(Fw::StringBase& sb) const
{
  static const char* formatString =
    "( "
    "m1 = %" PRIi32 ", "
    "m2 = %" PRIu32 ", "
    "m3 = %" PRIi32 ", "
    "m4 = %" PRIu32 ", "
    "m5 = %" PRIx32 ", "
    "m6 = %" PRIx32 ", "
    "m7 = %c, "
    "m8 = %c, "
    "m9 = %" PRIo32 ", "
    "m10 = %" PRIo32 ", "
    "m11 = %e, "
    "m12 = %f, "
    "m13 = %g, "
    "m14 = %.3e, "
    "m15 = %.3f, "
    "m16 = %.3g, "
    "m17 = %.3g%%"
    " )";

  char outputString[FW_SERIALIZABLE_TO_STRING_BUFFER_SIZE];
  (void) snprintf(
    outputString,
    FW_SERIALIZABLE_TO_STRING_BUFFER_SIZE,
    formatString,
    this->m1,
    this->m2,
    this->m3,
    this->m4,
    this->m5,
    this->m6,
    this->m7,
    this->m8,
    this->m9,
    this->m10,
    this->m11,
    this->m12,
    this->m13,
    this->m14,
    this->m15,
    this->m16,
    this->m17
  );

  outputString[FW_SERIALIZABLE_TO_STRING_BUFFER_SIZE-1] = 0; // NULL terminate
  sb = outputString;
}

#endif

// ----------------------------------------------------------------------
// Getter functions
// ----------------------------------------------------------------------

I32 Format ::
  getm1() const
{
  return this->m1;
}

U32 Format ::
  getm2() const
{
  return this->m2;
}

I32 Format ::
  getm3() const
{
  return this->m3;
}

U32 Format ::
  getm4() const
{
  return this->m4;
}

I32 Format ::
  getm5() const
{
  return this->m5;
}

U32 Format ::
  getm6() const
{
  return this->m6;
}

I32 Format ::
  getm7() const
{
  return this->m7;
}

U32 Format ::
  getm8() const
{
  return this->m8;
}

I32 Format ::
  getm9() const
{
  return this->m9;
}

U32 Format ::
  getm10() const
{
  return this->m10;
}

F32 Format ::
  getm11() const
{
  return this->m11;
}

F32 Format ::
  getm12() const
{
  return this->m12;
}

F32 Format ::
  getm13() const
{
  return this->m13;
}

F32 Format ::
  getm14() const
{
  return this->m14;
}

F32 Format ::
  getm15() const
{
  return this->m15;
}

F32 Format ::
  getm16() const
{
  return this->m16;
}

F32 Format ::
  getm17() const
{
  return this->m17;
}

// ----------------------------------------------------------------------
// Setter functions
// ----------------------------------------------------------------------

void Format ::
  set(
      I32 m1,
      U32 m2,
      I32 m3,
      U32 m4,
      I32 m5,
      U32 m6,
      I32 m7,
      U32 m8,
      I32 m9,
      U32 m10,
      F32 m11,
      F32 m12,
      F32 m13,
      F32 m14,
      F32 m15,
      F32 m16,
      F32 m17
  )
{
  this->m1 = m1;
  this->m2 = m2;
  this->m3 = m3;
  this->m4 = m4;
  this->m5 = m5;
  this->m6 = m6;
  this->m7 = m7;
  this->m8 = m8;
  this->m9 = m9;
  this->m10 = m10;
  this->m11 = m11;
  this->m12 = m12;
  this->m13 = m13;
  this->m14 = m14;
  this->m15 = m15;
  this->m16 = m16;
  this->m17 = m17;
}

void Format ::
  setm1(I32 m1)
{
  this->m1 = m1;
}

void Format ::
  setm2(U32 m2)
{
  this->m2 = m2;
}

void Format ::
  setm3(I32 m3)
{
  this->m3 = m3;
}

void Format ::
  setm4(U32 m4)
{
  this->m4 = m4;
}

void Format ::
  setm5(I32 m5)
{
  this->m5 = m5;
}

void Format ::
  setm6(U32 m6)
{
  this->m6 = m6;
}

void Format ::
  setm7(I32 m7)
{
  this->m7 = m7;
}

void Format ::
  setm8(U32 m8)
{
  this->m8 = m8;
}

void Format ::
  setm9(I32 m9)
{
  this->m9 = m9;
}

void Format ::
  setm10(U32 m10)
{
  this->m10 = m10;
}

void Format ::
  setm11(F32 m11)
{
  this->m11 = m11;
}

void Format ::
  setm12(F32 m12)
{
  this->m12 = m12;
}

void Format ::
  setm13(F32 m13)
{
  this->m13 = m13;
}

void Format ::
  setm14(F32 m14)
{
  this->m14 = m14;
}

void Format ::
  setm15(F32 m15)
{
  this->m15 = m15;
}

void Format ::
  setm16(F32 m16)
{
  this->m16 = m16;
}

void Format ::
  setm17(F32 m17)
{
  this->m17 = m17;
}
