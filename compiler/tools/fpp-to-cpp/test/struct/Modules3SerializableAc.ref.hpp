// ======================================================================
// \title  Modules3SerializableAc.hpp
// \author Generated by fpp-to-cpp
// \brief  hpp file for Modules3 struct
// ======================================================================

#ifndef Modules3SerializableAc_HPP
#define Modules3SerializableAc_HPP

#include "FpConfig.hpp"
#include "Fw/Types/Serializable.hpp"
#include "Fw/Types/String.hpp"
#include "Modules2SerializableAc.hpp"

class Modules3 :
  public Fw::Serializable
{

  public:

    // ----------------------------------------------------------------------
    // Types
    // ----------------------------------------------------------------------

    //! The array member types
    typedef M::Modules2 Type_of_arr[3];

  public:

    // ----------------------------------------------------------------------
    // Constants
    // ----------------------------------------------------------------------

    enum {
      //! The size of the serial representation
      SERIALIZED_SIZE =
        M::Modules2::SERIALIZED_SIZE +
        M::Modules2::SERIALIZED_SIZE * 3
    };

  public:

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    //! Constructor (default value)
    Modules3();

    //! Member constructor
    Modules3(
        const M::Modules2& x,
        const Type_of_arr& arr
    );

    //! Copy constructor
    Modules3(
        const Modules3& obj //!< The source object
    );

    //! Member constructor (scalar values for arrays)
    Modules3(
        const M::Modules2& x,
        const M::Modules2& arr
    );

  public:

    // ----------------------------------------------------------------------
    // Operators
    // ----------------------------------------------------------------------

    //! Copy assignment operator
    Modules3& operator=(
        const Modules3& obj //!< The source object
    );

    //! Equality operator
    bool operator==(
        const Modules3& obj //!< The other object
    ) const;

    //! Inequality operator
    bool operator!=(
        const Modules3& obj //!< The other object
    ) const;

#ifdef BUILD_UT

    //! Ostream operator
    friend std::ostream& operator<<(
        std::ostream& os, //!< The ostream
        const Modules3& obj //!< The object
    );

#endif

  public:

    // ----------------------------------------------------------------------
    // Member functions
    // ----------------------------------------------------------------------

    //! Serialization
    Fw::SerializeStatus serialize(
        Fw::SerializeBufferBase& buffer //!< The serial buffer
    ) const;

    //! Deserialization
    Fw::SerializeStatus deserialize(
        Fw::SerializeBufferBase& buffer //!< The serial buffer
    );

#if FW_SERIALIZABLE_TO_STRING || BUILD_UT

    //! Convert struct to string
    void toString(
        Fw::StringBase& sb //!< The StringBase object to hold the result
    ) const;

#endif

    // ----------------------------------------------------------------------
    // Getter functions
    // ----------------------------------------------------------------------

    //! Get member x
    M::Modules2& getx()
    {
      return this->x;
    }

    //! Get member x (const)
    const M::Modules2& getx() const
    {
      return this->x;
    }

    //! Get member arr
    Type_of_arr& getarr()
    {
      return this->arr;
    }

    //! Get member arr (const)
    const Type_of_arr& getarr() const
    {
      return this->arr;
    }

    // ----------------------------------------------------------------------
    // Setter functions
    // ----------------------------------------------------------------------

    //! Set all members
    void set(
        const M::Modules2& x,
        const Type_of_arr& arr
    );

    //! Set member x
    void setx(const M::Modules2& x);

    //! Set member arr
    void setarr(const Type_of_arr& arr);

  protected:

    // ----------------------------------------------------------------------
    // Member variables
    // ----------------------------------------------------------------------

    M::Modules2 x;
    M::Modules2 arr[3];

};

#endif
