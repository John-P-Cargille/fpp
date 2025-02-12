// ======================================================================
// \title  SSerializableAc.cpp
// \author Generated by fpp-to-cpp
// \brief  cpp file for S struct
// ======================================================================

#include "cstdio"
#include "cstring"

#include "Fw/Types/Assert.hpp"
#include "Fw/Types/StringUtils.hpp"
#include "SSerializableAc.hpp"

// ----------------------------------------------------------------------
// Constructors
// ----------------------------------------------------------------------

S ::
  S() :
    Serializable(),
    x(0)
{

}

S ::
  S(U32 x) :
    Serializable(),
    x(x)
{

}

S ::
  S(const S& obj) :
    Serializable(),
    x(obj.x)
{

}

// ----------------------------------------------------------------------
// Operators
// ----------------------------------------------------------------------

S& S ::
  operator=(const S& obj)
{
  if (this == &obj) {
    return *this;
  }

  set(obj.x);
  return *this;
}

bool S ::
  operator==(const S& obj) const
{
  return (this->x == obj.x);
}

bool S ::
  operator!=(const S& obj) const
{
  return !(*this == obj);
}

#ifdef BUILD_UT

std::ostream& operator<<(std::ostream& os, const S& obj) {
  Fw::String s;
  obj.toString(s);
  os << s.toChar();
  return os;
}

#endif

// ----------------------------------------------------------------------
// Member functions
// ----------------------------------------------------------------------

Fw::SerializeStatus S ::
  serialize(Fw::SerializeBufferBase& buffer) const
{
  Fw::SerializeStatus status;

  status = buffer.serialize(this->x);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }

  return status;
}

Fw::SerializeStatus S ::
  deserialize(Fw::SerializeBufferBase& buffer)
{
  Fw::SerializeStatus status;

  status = buffer.deserialize(this->x);
  if (status != Fw::FW_SERIALIZE_OK) {
    return status;
  }

  return status;
}

#if FW_SERIALIZABLE_TO_STRING || BUILD_UT

void S ::
  toString(Fw::StringBase& sb) const
{
  static const char* formatString =
    "( "
    "x = %" PRIu32 ""
    " )";

  char outputString[FW_SERIALIZABLE_TO_STRING_BUFFER_SIZE];
  (void) snprintf(
    outputString,
    FW_SERIALIZABLE_TO_STRING_BUFFER_SIZE,
    formatString,
    this->x
  );

  outputString[FW_SERIALIZABLE_TO_STRING_BUFFER_SIZE-1] = 0; // NULL terminate
  sb = outputString;
}

#endif

// ----------------------------------------------------------------------
// Setter functions
// ----------------------------------------------------------------------

void S ::
  set(U32 x)
{
  this->x = x;
}

void S ::
  setx(U32 x)
{
  this->x = x;
}
