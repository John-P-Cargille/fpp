// ======================================================================
// \title  StringArrayArrayAc.hpp
// \author Generated by fpp-to-cpp
// \brief  hpp file for StringArray array
// ======================================================================

#ifndef StringArrayArrayAc_HPP
#define StringArrayArrayAc_HPP

#include "FpConfig.hpp"
#include "Fw/Types/Serializable.hpp"
#include "Fw/Types/String.hpp"
#include "String2ArrayAc.hpp"

//! An array of arrays of strings
class StringArray :
  public Fw::Serializable
{

  public:

    // ----------------------------------------------------------------------
    // Types
    // ----------------------------------------------------------------------

    //! The element type
    typedef String2 ElementType;

  public:

    // ----------------------------------------------------------------------
    // Constants
    // ----------------------------------------------------------------------

    enum {
      //! The size of the array
      SIZE = 5,
      //! The size of the serial representation
      SERIALIZED_SIZE = SIZE * String2::SERIALIZED_SIZE,
    };

  public:

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    //! Constructor (default value)
    StringArray();

    //! Constructor (user-provided value)
    StringArray(
        const ElementType (&a)[SIZE] //!< The array
    );

    //! Constructor (single element)
    StringArray(
        const ElementType& e //!< The element
    );

    //! Constructor (multiple elements)
    StringArray(
        const ElementType& e1, //!< Element 1
        const ElementType& e2, //!< Element 2
        const ElementType& e3, //!< Element 3
        const ElementType& e4, //!< Element 4
        const ElementType& e5 //!< Element 5
    );

    //! Copy Constructor
    StringArray(
        const StringArray& obj //!< The source object
    );

  public:

    // ----------------------------------------------------------------------
    // Operators
    // ----------------------------------------------------------------------

    //! Subscript operator
    ElementType& operator[](
        const U32 i //!< The subscript index
    );

    //! Const subscript operator
    const ElementType& operator[](
        const U32 i //!< The subscript index
    ) const;

    //! Copy assignment operator (object)
    StringArray& operator=(
        const StringArray& obj //!< The source object
    );

    //! Copy assignment operator (raw array)
    StringArray& operator=(
        const ElementType (&a)[SIZE] //!< The source array
    );

    //! Copy assignment operator (single element)
    StringArray& operator=(
        const ElementType& e //!< The element
    );

    //! Equality operator
    bool operator==(
        const StringArray& obj //!< The other object
    ) const;

    //! Inequality operator
    bool operator!=(
        const StringArray& obj //!< The other object
    ) const;

#ifdef BUILD_UT

    //! Ostream operator
    friend std::ostream& operator<<(
        std::ostream& os, //!< The ostream
        const StringArray& obj //!< The object
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

#if FW_ARRAY_TO_STRING || BUILD_UT

    //! Convert array to string
    void toString(
        Fw::StringBase& sb //!< The StringBase object to hold the result
    ) const;

#endif

  private:

    // ----------------------------------------------------------------------
    // Member variables
    // ----------------------------------------------------------------------

    //! The array elements
    ElementType elements[SIZE];

};

#endif
