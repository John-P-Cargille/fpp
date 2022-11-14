// ======================================================================
// \title  EmptyPortAc.hpp
// \author Generated by fpp-to-cpp
// \brief  hpp file for Empty port
// ======================================================================

#ifndef EmptyPortAc_HPP
#define EmptyPortAc_HPP

#include "FpConfig.hpp"
#include "Fw/Comp/PassiveComponentBase.hpp"
#include "Fw/Port/InputPortBase.hpp"
#include "Fw/Port/OutputPortBase.hpp"
#include "Fw/Types/Serializable.hpp"
#include "Fw/Types/StringType.hpp"

//! Input Empty port
//! An empty port
class InputEmptyPort :
  public Fw::InputPortBase
{

  public:

    // ----------------------------------------------------------------------
    // Constants
    // ----------------------------------------------------------------------

    enum {
      //! The size of the serial representations of the port arguments
      SERIALIZED_SIZE = 0
    };

  public:

    // ----------------------------------------------------------------------
    // Types
    // ----------------------------------------------------------------------

    //! The port callback function type
    typedef void (*CompFuncPtr)(
      Fw::PassiveComponentBase* callComp,
      NATIVE_INT_TYPE portNum
    );

  public:

    // ----------------------------------------------------------------------
    // Input Port Member functions
    // ----------------------------------------------------------------------

    //! Constructor
    InputEmptyPort();

    //! Initialization function
    void init();

    //! Register a component
    void addCallComp(
        Fw::PassiveComponentBase* callComp, //!< The containing component
        CompFuncPtr funcPtr //!< The port callback function
    );

    //! Invoke a port interface
    void invoke();

  private:

#if FW_PORT_SERIALIZATION == 1

    //! Invoke the port with serialized arguments
    Fw::SerializeStatus invokeSerial(Fw::SerializeBufferBase& _buffer);

#endif

  private:

    // ----------------------------------------------------------------------
    // Member variables
    // ----------------------------------------------------------------------

    //! The pointer to the port callback function
    CompFuncPtr m_func;

};

//! Output Empty port
//! An empty port
class OutputEmptyPort :
  public Fw::OutputPortBase
{

  public:

    // ----------------------------------------------------------------------
    // Output Port Member functions
    // ----------------------------------------------------------------------

    //! Constructor
    OutputEmptyPort();

    //! Initialization function
    void init();

    //! Register an input port
    void addCallPort(
        InputEmptyPort* callPort //!< The input port
    );

    //! Invoke a port interface
    void invoke();

  private:

    // ----------------------------------------------------------------------
    // Member variables
    // ----------------------------------------------------------------------

    //! The pointer to the input port
    InputEmptyPort* m_port;

};

#endif
